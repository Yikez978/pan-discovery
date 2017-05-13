package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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

        directoryStream.walk().parallel().forEach(path -> {
            logger.debug("Scheduling {}", path);
            fileScanningService.scan(path);
        });
    }
}
