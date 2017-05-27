package org.alcibiade.pandiscovery.db.service.progress;

import org.alcibiade.pandiscovery.db.model.DatabaseTable;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Local objects to estimate progress. This is stateful hence the synchronization.
 */
public class ProgressCalculator {

    private long tsStart;

    private long totalRows;

    private long processedRows;

    public ProgressCalculator(Set<DatabaseTable> tables) {
        totalRows = tables.stream().collect(Collectors.summingLong(DatabaseTable::getRows));
        processedRows = 0;
        tsStart = System.currentTimeMillis();
    }

    public synchronized ProgressEstimate getEstimateAfterTable(DatabaseTable table) {
        processedRows += table.getRows();
        return getEstimate();
    }

    public ProgressEstimate getEstimate() {
        return new ProgressEstimate(processedRows, totalRows, tsStart, System.currentTimeMillis());
    }
}
