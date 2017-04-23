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
@Profile("!test")
public class FsDiscoveryRunner implements ApplicationRunner {
    private ScanningService scanningService;
    private FsCsvExportService exportService;
    private Logger logger = LoggerFactory.getLogger(FsDiscoveryRunner.class);

    @Autowired
    public FsDiscoveryRunner(ScanningService scanningService, FsCsvExportService exportService) {
        this.scanningService = scanningService;
        this.exportService = exportService;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        List<String> paths = new ArrayList<>();

        if (applicationArguments.getSourceArgs().length == 0) {
            paths.add(".");
        } else {
            paths.addAll(applicationArguments.getNonOptionArgs());
        }

        exportService.setVerbose(applicationArguments.containsOption("verbose"));

        scanningService.scan(paths);

        logger.info("Found {} possible PAN occurrences in {} files",
                exportService.getPansDetected(),
                exportService.getFilesExplored());

        logger.info("Report written to {}", exportService.getCsvFilePath());
    }
}
