package org.alcibiade.pandiscovery.scan;

/**
 * Result of a single detection run.
 */
public class DetectionResult {

    private CardType cardType;
    private String sample;
    private String sampleLine;

    public DetectionResult(CardType cardType, String sample, String sampleLine) {
        this.cardType = cardType;
        this.sample = sample;
        this.sampleLine = sampleLine;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getSample() {
        return sample;
    }

    public String getSampleLine() {
        return sampleLine;
    }
}
