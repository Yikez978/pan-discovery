package org.alcibiade.pandiscovery.scan;

/**
 * Common interface for PAN detectors.
 */
public interface Detector {

    /**
     * Check a text value for potential matches.
     *
     * @param text the text to parse
     * @return a result object containing a matching type, or null if no match found
     */
    DetectionResult detectMatch(String text);
}
