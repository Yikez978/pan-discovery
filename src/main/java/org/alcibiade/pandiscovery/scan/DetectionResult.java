package org.alcibiade.pandiscovery.scan;

/**
 * Result of a single detection run.
 */
public class DetectionResult {

    private CardType cardType;

    public DetectionResult(CardType cardType) {
        this.cardType = cardType;
    }

    public CardType getCardType() {
        return cardType;
    }
}
