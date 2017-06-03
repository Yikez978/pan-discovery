package org.alcibiade.pandiscovery.db.service;

import org.alcibiade.pandiscovery.db.model.DatabaseField;
import org.alcibiade.pandiscovery.db.model.DiscoveryFieldResults;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.scan.CardType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Generate CSV report file.
 */
@Component
public class CsvExportService implements ExportService {

    private Logger logger = LoggerFactory.getLogger(CsvExportService.class);

    public File export(DiscoveryReport report) {
        String d = new SimpleDateFormat("yyyy-MM-dd_HHmm").format(report.getReportDateStart());
        String filename = "PAN_Discovery_" + report.getDatabaseName() + "_" + d + ".csv";
        File file = new File(filename);

        logger.info("Report written to {}", filename);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Owner;Table;Field;Rows;PAN Matches" + System.lineSeparator());

            for (DatabaseField field : report) {
                StringBuilder row = new StringBuilder();
                row.append(field.getTable().getOwner());
                row.append(';');
                row.append(field.getTable().getName());
                row.append(';');
                row.append(field.getName());
                row.append(';');
                row.append(field.getTable().getRows());
                row.append(';');

                DiscoveryFieldResults matches = report.getMatches(field);
                long total = 0L;

                for (CardType cardType : CardType.values()) {
                    Long l = matches.getMatchesByCardType().get(cardType);

                    total += l;

                    row.append(l);
                    row.append(';');
                }

                row.append(total);
                row.append(System.lineSeparator());

                writer.write(row.toString());
            }

            writer.close();
            return file;
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write CSV file to " + filename, ex);
        }
    }
}
