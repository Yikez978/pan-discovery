package org.alcibiade.pandiscovery.scan;

/**
 * Result of a single detection run.
 */
public class DetectionResult {

    private CardType cardType;
    private String sample;

    public DetectionResult(CardType cardType, String sample) {
        this.cardType = cardType;
        this.sample = sample;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getSample() {
        return sample;
    }
}
