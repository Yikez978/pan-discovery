package org.alcibiade.pandiscovery.fs;

import org.alcibiade.pandiscovery.scan.Detector;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Threaded file scanning service.
 */
@Component
public class FileScanningService {
    private final Set<Detector> cardDetectors;
    private final FsCsvExportService exportService;
    private Logger logger = LoggerFactory.getLogger(FileScanningService.class);
    private Tika tika = new Tika();

    @Autowired
    public FileScanningService(Set<Detector> cardDetectors, FsCsvExportService exportService) {
        this.cardDetectors = cardDetectors;
        this.exportService = exportService;
    }

    @Async
    public Future<Void> scan(Path path) {
        logger.debug(" - {}", path);

        try (Reader reader = tika.parse(path)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            int result = bufferedReader.lines().mapToInt(this::scan).sum();
            exportService.register(path, result);
        } catch (IOException e) {
            logger.warn("Failed to scan {} : {}", path, e.getLocalizedMessage());
        }

        return null;
    }

    private int scan(String line) {
        int matches = cardDetectors.stream().mapToInt(detector -> detector.detectMatch(line) == null ? 0 : 1).sum();

        if (logger.isTraceEnabled()) {
            logger.trace("{}", String.format("%3d - %s", matches, line));
        }

        return matches;
    }
}
