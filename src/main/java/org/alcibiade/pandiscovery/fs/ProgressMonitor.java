package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Display progress.
 */
@Component
@Profile("!test")
public class ProgressMonitor {
    private final RuntimeParameters runtimeParameters;
    private final FsCsvExportService exportService;
    private Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);

    @Autowired
    public ProgressMonitor(RuntimeParameters runtimeParameters, FsCsvExportService exportService) {
        this.runtimeParameters = runtimeParameters;
        this.exportService = exportService;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 30000)
    public void displayProgressDigest() {
        if (!runtimeParameters.isVerbose()) {
            logger.info("Progress: {} files scanned, {} PAN occurrences found",
                    exportService.getFilesExplored(),
                    exportService.getPansDetected());
        }
    }
}
