package org.alcibiade.pandiscovery.fs;

import org.alcibiade.pandiscovery.scan.Detector;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
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
    private Set<Detector> cardDetectors;
    private FsCsvExportService exportService;
    private Tika tika = new Tika();

    @Autowired
    public ScanningService(Set<Detector> cardDetectors, FsCsvExportService exportService) {
        this.cardDetectors = cardDetectors;
        this.exportService = exportService;
    }

    public void scan(List<String> paths) {
        FolderWalker directoryStream = new FolderWalker(paths);

        directoryStream.walk(path -> {
            logger.debug(" - {}", path);
            scan(path);
        });
    }

    private void scan(Path path) {
        try (Reader reader = tika.parse(path)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            int result = bufferedReader.lines().mapToInt(this::scan).sum();
            exportService.register(path, result);
        } catch (IOException e) {
            logger.warn("Failed to scan {} : {}", path, e.getLocalizedMessage());
        }
    }

    private int scan(String line) {
        int matches = cardDetectors.stream().mapToInt(detector -> detector.detectMatch(line) == null ? 0 : 1).sum();

        if (logger.isTraceEnabled()) {
            logger.trace("{}", String.format("%3d - %s", matches, line));
        }

        return matches;
    }
}
