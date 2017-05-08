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
    private Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);

    private final FsCsvExportService exportService;

    @Autowired
    public ProgressMonitor(FsCsvExportService exportService) {
        this.exportService = exportService;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 30000)
    public void displayProgress() {
        logger.info("Progress: {} files scanned, {} PAN occurrences found",
                exportService.getFilesExplored(),
                exportService.getPansDetected());
    }
}
