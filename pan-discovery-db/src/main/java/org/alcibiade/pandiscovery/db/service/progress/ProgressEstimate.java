package org.alcibiade.pandiscovery.db.service.progress;

/**
 * Single estimate.
 */
public class ProgressEstimate {

    private long tsStart;

    private long tsNow;

    private long totalRows;

    private long processedRows;

    public ProgressEstimate(long processedRows, long totalRows, long tsStart, long tsNow) {
        this.processedRows = processedRows;
        this.totalRows = totalRows;
        this.tsStart = tsStart;
        this.tsNow = tsNow;
    }

    public int getProgressPercent() {
        return (int) (100 * processedRows / totalRows);
    }

    public String getElapsedTime() {
        return durationToString(tsNow - tsStart);
    }

    public String getRemainingTime() {
        if (processedRows == 0) {
            return "N/A";
        }

        long elapsed = tsNow - tsStart;
        long remainingMs = elapsed * (totalRows - processedRows) / processedRows;
        return durationToString(remainingMs);
    }

    private String durationToString(long l) {
        long mm = (l / 1000 / 60) % 60;
        long hh = l / 1000 / 3600;

        return String.format("%3dh:%02dmin", hh, mm);
    }

}
