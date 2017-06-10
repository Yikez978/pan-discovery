package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for filesystem checks.
 */
@Component
@Profile("default")
public class FsDiscoveryRunner implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(FsDiscoveryRunner.class);
    private ScanningService scanningService;
    private FsCsvExportService exportService;
    private RuntimeParameters runtimeParameters;
    private List<String> paths = new ArrayList<>();

    @Autowired
    public FsDiscoveryRunner(ScanningService scanningService,
                             FsCsvExportService exportService,
                             RuntimeParameters runtimeParameters) {
        this.scanningService = scanningService;
        this.exportService = exportService;
        this.runtimeParameters = runtimeParameters;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (applicationArguments.getSourceArgs().length == 0) {
            paths.add(".");
        } else {
            paths.addAll(applicationArguments.getNonOptionArgs());
        }

        runtimeParameters.setVerbose(applicationArguments.containsOption("verbose"));
    }

    public void runScan() {
        Stopwatch stopwatch = new Stopwatch();
        scanningService.scan(paths);

        logger.info("Found {} possible PAN occurrences in {} files in {}",
            exportService.getPansDetected(),
            exportService.getFilesExplored(),
            stopwatch.elapsedTimeAsString()
        );

        logger.info("Report written to {}", exportService.getCsvFilePath());
    }
}
