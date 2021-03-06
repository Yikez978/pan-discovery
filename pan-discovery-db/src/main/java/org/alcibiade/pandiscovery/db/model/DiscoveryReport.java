package org.alcibiade.pandiscovery.db.model;

import org.alcibiade.pandiscovery.scan.CardType;

import java.util.*;

/**
 * Discovery execution report.
 * <p>
 * Warning, this is a mutable object.
 */
public class DiscoveryReport implements Iterable<DatabaseField> {
    private Date reportDateStart;
    private String databaseName;
    private Map<DatabaseField, DiscoveryFieldResults> matches;

    public DiscoveryReport(String databaseName) {
        this.databaseName = databaseName;
        this.matches = new TreeMap<>();
        this.reportDateStart = new Date();
    }

    public synchronized void report(DatabaseField field, CardType cardType, String rawValue) {
        if (!matches.containsKey(field)) {
            matches.put(field, new DiscoveryFieldResults());
        }

        matches.get(field).addMatch(new DiscoveryMatch(cardType, rawValue));
    }

    @Override
    public Iterator<DatabaseField> iterator() {
        return matches.keySet().iterator();
    }


    public boolean isEmpty() {
        return matches.isEmpty();
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public Date getReportDateStart() {
        return reportDateStart;
    }

    public Set<DatabaseField> getFields() {
        return matches.keySet();
    }

    public DiscoveryFieldResults getMatches(DatabaseField field) {
        return matches.get(field);
    }
}
