package org.alcibiade.pandiscovery.scan;

/**
 * Common interface for PAN detectors.
 */
public interface Detector {

    /**
     * Check a text value for potential matches.
     *
     * @param text the text to parse
     * @return a matching type, or null if no match found
     */
    CardType detectMatch(String text);
}
