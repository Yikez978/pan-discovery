package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

/**
 * Export filesystem report.
 */
@Component
public class FsCsvExportService {
    private Logger logger = LoggerFactory.getLogger(FsCsvExportService.class);

    private Date reportDateStart;
    private long filesExplored = 0;
    private long pansDetected = 0;
    private Path csvFilePath;
    private boolean verbose = false;

    @PostConstruct
    public void init() {
        reportDateStart = new Date();
        String d = new SimpleDateFormat("yyyy-MM-dd_HHmm").format(reportDateStart);
        String filename = "PAN_Discovery_" + d + ".csv";
        this.csvFilePath = Paths.get(filename);
        logger.info("Results will be logged in {}", this.csvFilePath);
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public Date getReportDateStart() {
        return reportDateStart;
    }

    public long getFilesExplored() {
        return filesExplored;
    }

    public long getPansDetected() {
        return pansDetected;
    }

    public Path getCsvFilePath() {
        return csvFilePath;
    }

    public void register(Path file, int matches) {
        if (verbose) {
            logger.info(String.format("%5d results in %s", matches, file));
        }

        filesExplored += 1;
        pansDetected += matches;

        if (matches > 0) {
            String row = String.format("%s;%d", file.toString(), matches);

            try {
                Files.write(csvFilePath,
                        Collections.singleton(row),
                        StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new IllegalStateException("Could not write report at " + this.csvFilePath, e);
            }
        }
    }
}
