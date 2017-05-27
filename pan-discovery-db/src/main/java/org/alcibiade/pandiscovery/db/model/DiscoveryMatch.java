package org.alcibiade.pandiscovery.db.model;

import org.alcibiade.pandiscovery.scan.CardType;

/**
 * Single discovery match.
 */
public class DiscoveryMatch {

    private CardType cardType;

    private String rawValue;

    public DiscoveryMatch(CardType cardType, String rawValue) {
        this.cardType = cardType;
        this.rawValue = rawValue;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getRawValue() {
        return rawValue;
    }
}
