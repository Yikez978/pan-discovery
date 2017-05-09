package org.alcibiade.pandiscovery.fs;

import org.alcibiade.pandiscovery.scan.Detector;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

/**
 * Scanning service implementation.
 */
@Component
public class ScanningService {

    private Logger logger = LoggerFactory.getLogger(ScanningService.class);

    private final FileScanningService fileScanningService;

    @Autowired
    public ScanningService(FileScanningService fileScanningService) {
        this.fileScanningService = fileScanningService;
    }

    public void scan(List<String> paths) {
        FolderWalker directoryStream = new FolderWalker(paths);

        directoryStream.walk(path -> {
            logger.debug("Scheduling {}", path);
            fileScanningService.scan(path);
        });
    }

}
