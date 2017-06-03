package org.alcibiade.pandiscovery.db.dao;

/**
 * Result of a single scan operation.
 */
public class ScanResult {
    private long rowCount;
    private long matches;

    public ScanResult(long rowCount, long matches) {
        this.rowCount = rowCount;
        this.matches = matches;
    }

    public long getRowCount() {
        return rowCount;
    }

    public long getMatches() {
        return matches;
    }

    @Override
    public String toString() {
        return "" + matches + " matches out of " + rowCount + "rows";
    }
}
