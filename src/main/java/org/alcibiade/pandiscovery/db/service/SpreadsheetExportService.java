package org.alcibiade.pandiscovery.db.service;

import org.alcibiade.pandiscovery.db.model.DatabaseField;
import org.alcibiade.pandiscovery.db.model.DiscoveryFieldResults;
import org.alcibiade.pandiscovery.db.model.DiscoveryMatch;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Export to native spreadsheet file.
 */
@Component
public class SpreadsheetExportService implements ExportService {

    private Logger logger = LoggerFactory.getLogger(SpreadsheetExportService.class);

    public File export(DiscoveryReport report) {
        String d = new SimpleDateFormat("yyyy-MM-dd_HHmm").format(report.getReportDateStart());
        String filename = "PAN_Discovery_" + report.getDatabaseName() + "_" + d + ".xlsx";
        File file = new File(filename);
        XSSFWorkbook wb = new XSSFWorkbook();

        createSummarySheet(wb, report);
        createSamplesSheet(wb, report);

        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to write spreadsheet file to " + filename, ex);
        }

        logger.info("Spreadsheet report written to {}", filename);
        return file;
    }

    private void createSamplesSheet(XSSFWorkbook wb, DiscoveryReport report) {
        Sheet sheetSamples = wb.createSheet("Samples");
        sheetSamples.createFreezePane(0, 1, 0, 1);

        int rowIndex = 0;

        Row titleRow = sheetSamples.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("Schema");
        titleRow.createCell(1).setCellValue("Table");
        titleRow.createCell(2).setCellValue("Field");
        titleRow.createCell(3).setCellValue("Card type");
        titleRow.createCell(4).setCellValue("Raw value");

        for (DatabaseField field : report) {
            DiscoveryFieldResults result = report.getMatches(field);

            for (DiscoveryMatch match : result.getSampleMatches()) {
                Row row = sheetSamples.createRow(rowIndex++);
                row.createCell(0).setCellValue(field.getTable().getOwner());
                row.createCell(1).setCellValue(field.getTable().getName());
                row.createCell(2).setCellValue(field.getName());
                row.createCell(3).setCellValue(match.getCardType().toString());
                row.createCell(4).setCellValue(match.getRawValue());
            }
        }

        sheetSamples.autoSizeColumn(0);
        sheetSamples.autoSizeColumn(1);
        sheetSamples.autoSizeColumn(2);
        sheetSamples.autoSizeColumn(3);
        sheetSamples.autoSizeColumn(4);
    }

    private void createSummarySheet(XSSFWorkbook wb, DiscoveryReport report) {
        Sheet sheetSummary = wb.createSheet("Matching Fields");
        sheetSummary.createFreezePane(0, 1, 0, 1);

        int rowIndex = 0;

        Row titleRow = sheetSummary.createRow(rowIndex++);
        titleRow.createCell(0).setCellValue("Schema");
        titleRow.createCell(1).setCellValue("Table");
        titleRow.createCell(2).setCellValue("Field");
        titleRow.createCell(3).setCellValue("Matches");

        for (DatabaseField field : report) {
            Row row = sheetSummary.createRow(rowIndex++);
            row.createCell(0).setCellValue(field.getTable().getOwner());
            row.createCell(1).setCellValue(field.getTable().getName());
            row.createCell(2).setCellValue(field.getName());
            row.createCell(3).setCellValue(
                    report.getMatches(field).getMatchesByCardType().values().stream().reduce(0L, Long::sum)
            );
        }

        sheetSummary.autoSizeColumn(0);
        sheetSummary.autoSizeColumn(1);
        sheetSummary.autoSizeColumn(2);
        sheetSummary.autoSizeColumn(3);
    }
}