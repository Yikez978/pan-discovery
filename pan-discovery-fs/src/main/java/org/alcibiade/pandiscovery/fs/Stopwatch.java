package org.alcibiade.pandiscovery.fs;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Measure elapsed time.
 */
public class Stopwatch {

    private LocalDateTime timeStart;

    public Stopwatch() {
        timeStart = LocalDateTime.now();
    }

    public String elapsedTimeAsString() {
        LocalDateTime timeEnd = LocalDateTime.now();
        long min = ChronoUnit.MINUTES.between(timeStart, timeEnd);
        return "" + min + " minutes";
    }

}
