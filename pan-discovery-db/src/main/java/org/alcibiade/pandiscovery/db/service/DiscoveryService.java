package org.alcibiade.pandiscovery.db.service;

import org.alcibiade.pandiscovery.db.dao.AbstractDatabase;
import org.alcibiade.pandiscovery.db.dao.ScanResult;
import org.alcibiade.pandiscovery.db.model.DatabaseTable;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.db.service.progress.ProgressCalculator;
import org.alcibiade.pandiscovery.db.service.progress.ProgressEstimate;
import org.alcibiade.pandiscovery.db.service.threads.DiscoveryThreadFactory;
import org.alcibiade.pandiscovery.scan.Detector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Discovery service.
 */
@Component
public class DiscoveryService {
    private Logger logger = LoggerFactory.getLogger(DiscoveryService.class);

    private int threads;
    private AbstractDatabase abstractDatabase;
    private Set<Detector> detectors;

    @Autowired
    public DiscoveryService(@Value("${pan-discovery.discovery.threads:24}") int threads,
                            AbstractDatabase abstractDatabase,
                            Set<Detector> detectors) {
        this.threads = threads;
        this.abstractDatabase = abstractDatabase;
        this.detectors = detectors;
    }

    @Transactional(readOnly = true)
    public DiscoveryReport runDiscovery(String... args) {
        String name = abstractDatabase.getDatabaseName();
        DiscoveryReport report = new DiscoveryReport(name);
        Set<DatabaseTable> tables = abstractDatabase.getAllTables(args.length == 0 ? null : args[0]);

        try {
            ProgressCalculator progressCalculator = new ProgressCalculator(tables);

            ExecutorService executorService = Executors.newFixedThreadPool(threads, new DiscoveryThreadFactory());

            List<DiscoveryTask> tasks = tables.stream()
                .map(table -> new DiscoveryTask(table, report, progressCalculator))
                .collect(Collectors.toList());

            executorService.invokeAll(tasks);
            executorService.shutdown();
            executorService.awaitTermination(100L, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new IllegalStateException("Execution interrupted", e);
        }

        return report;
    }

    private class DiscoveryTask implements Callable<Void> {
        private DatabaseTable table;
        private DiscoveryReport report;
        private ProgressCalculator progressCalculator;

        public DiscoveryTask(DatabaseTable table, DiscoveryReport report, ProgressCalculator progressCalculator) {
            this.table = table;
            this.report = report;
            this.progressCalculator = progressCalculator;
        }

        @Override
        public Void call() {
            ScanResult result = abstractDatabase.scan(table, detectors, report);

            ProgressEstimate estimateAfterTable = progressCalculator.getEstimateAfterTable(table);
            logger.info(String.format("[%3d%%|E:%s|R:%s]%12d rows read from %s%s",
                estimateAfterTable.getProgressPercent(),
                estimateAfterTable.getElapsedTime(),
                estimateAfterTable.getRemainingTime(),
                result.getRowCount(), table,
                result.getMatches() == 0 ? "" : ", " + result.getMatches() + " matches found"
            ));

            return null;
        }
    }
}
