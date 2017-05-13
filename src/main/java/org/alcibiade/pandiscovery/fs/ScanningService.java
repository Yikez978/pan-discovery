package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Scanning service implementation.
 */
@Component
public class ScanningService {

    private final FileScanningService fileScanningService;
    private Logger logger = LoggerFactory.getLogger(ScanningService.class);

    @Autowired
    public ScanningService(FileScanningService fileScanningService) {
        this.fileScanningService = fileScanningService;
    }

    public void scan(List<String> paths) {
        FolderWalker directoryStream = new FolderWalker(paths);

        directoryStream.walk().parallel()
                .map(path -> {
                    logger.debug("Scheduling {}", path);
                    return fileScanningService.scan(path);
                })
                .forEach(future -> {
                    try {
                        logger.debug("Waiting for {}", future);
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new IllegalStateException("Interruption while waiting for execution end.", e);
                    }
                });
    }
}
