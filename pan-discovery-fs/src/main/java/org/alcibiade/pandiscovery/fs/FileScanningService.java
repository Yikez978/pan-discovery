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
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Threaded file scanning service.
 */
@Component
public class FileScanningService {
    private final Set<Detector> cardDetectors;
    private final FsCsvExportService exportService;
    private Logger logger = LoggerFactory.getLogger(FileScanningService.class);
    private Tika tika = new Tika();
    private RuntimeParameters runtimeParameters;
    private Pattern ignoredFiles = Pattern.compile(".*\\.pack");
    private Set<String> ignoredMediaTypes = Collections.singleton("application/zlib");

    @Autowired
    public FileScanningService(Set<Detector> cardDetectors,
                               FsCsvExportService exportService,
                               RuntimeParameters runtimeParameters) {
        this.cardDetectors = cardDetectors;
        this.exportService = exportService;
        this.runtimeParameters = runtimeParameters;
    }

    public void scan(Path path) {
        if (runtimeParameters.isVerbose()) {
            logger.debug("Scanning {}", path);
        }

        if (ignoredFiles.matcher(path.getFileName().toString()).matches()) {
            logger.trace("Ignoring file {}", path);
            return;
        }

        String mediaType;

        try {
            mediaType = tika.detect(path);
            if (ignoredMediaTypes.contains(mediaType)) {
                logger.trace("Ignoring file {} of type {}", path, mediaType);
                return;
            }
        } catch (IOException e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }

            logger.warn("Failed detect type of {} : {}", path, t.getLocalizedMessage());
            return;
        }

        try (Reader reader = tika.parse(path)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            ScanResult result = bufferedReader.lines()
                .map(this::scan)
                .reduce(
                    ScanResult.EMPTY,
                    ScanResult::reduce
                );

            exportService.register(path, result.getMatches(), mediaType, result.getSample(), result.getSampleLine());
        } catch (IOException | UncheckedIOException e) {
            Throwable t = e;
            while (t.getCause() != null) {
                t = t.getCause();
            }

            logger.warn("Failed to scan {} : {}", path, t.getLocalizedMessage());
        }
    }

    private ScanResult scan(String line) {

        ScanResult result = cardDetectors.stream()
            .map(detector -> detector.detectMatch(line))
            .filter(Objects::nonNull)
            .map(m -> new ScanResult(m.getSample(), m.getSampleLine(), 1))
            .reduce(
                ScanResult.EMPTY,
                ScanResult::reduce
            );

        if (logger.isTraceEnabled()) {
            logger.trace("{}", String.format("%3d - %s", result.getMatches(), line));
        }

        return result;
    }

    private static class ScanResult {

        public static ScanResult EMPTY = new ScanResult(null, null, 0);
        private String sample;
        private String sampleLine;
        private long matches;

        public ScanResult(String sample, String sampleLine, long matches) {
            this.sample = sample;
            this.matches = matches;
            this.sampleLine = sampleLine;
        }

        public static ScanResult reduce(ScanResult r1, ScanResult r2) {
            String sample = r1.getSample() != null ? r1.getSample() : r2.getSample();
            String sampleLine = r1.getSample() != null ? r1.getSampleLine() : r2.getSampleLine();
            return new ScanResult(sample, sampleLine, r1.getMatches() + r2.getMatches());
        }

        public String getSample() {
            return sample;
        }

        public String getSampleLine() {
            return sampleLine;
        }

        public long getMatches() {
            return matches;
        }

        @Override
        public String toString() {
            return "ScanResult{" +
                "sample='" + sample + '\'' +
                ", matches=" + matches +
                '}';
        }
    }

}
