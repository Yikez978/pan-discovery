package org.alcibiade.pandiscovery.fs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Scanning service implementation.
 */
@Component
public class ScanningService {

    private final FileScanningService fileScanningService;

    @Autowired
    public ScanningService(FileScanningService fileScanningService) {
        this.fileScanningService = fileScanningService;
    }

    public void scan(List<String> paths) {
        FolderWalker directoryStream = new FolderWalker(paths);

        directoryStream.walk()
                .parallel()
                .forEach(fileScanningService::scan);
    }
}
