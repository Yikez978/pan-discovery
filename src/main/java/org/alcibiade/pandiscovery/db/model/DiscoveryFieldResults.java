package org.alcibiade.pandiscovery.db.model;

import org.alcibiade.pandiscovery.scan.CardType;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Results for a single field.
 */
public class DiscoveryFieldResults {
    public static final int SAMPLE_SIZE = 20;

    private Map<CardType, Long> matchesByCardType;

    private Set<DiscoveryMatch> sampleMatches;

    public DiscoveryFieldResults() {
        matchesByCardType = new EnumMap<>(CardType.class);
        sampleMatches = new HashSet<>();

        for (CardType type : CardType.values()) {
            matchesByCardType.put(type, 0L);
        }
    }

    public void addMatch(DiscoveryMatch discoveryMatch) {
        Long count = matchesByCardType.get(discoveryMatch.getCardType());
        matchesByCardType.put(discoveryMatch.getCardType(), count + 1);

        if (sampleMatches.size() < SAMPLE_SIZE) {
            sampleMatches.add(discoveryMatch);
        }
    }

    public Map<CardType, Long> getMatchesByCardType() {
        return matchesByCardType;
    }

    public Set<DiscoveryMatch> getSampleMatches() {
        return sampleMatches;
    }

    @Override
    public String toString() {
        return "DiscoveryFieldResults{" +
                "matchesByCardType=" + matchesByCardType +
                ", " + sampleMatches.size() + " samples}";
    }
}
