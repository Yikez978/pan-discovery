package org.alcibiade.pandiscovery.db.model;

import java.math.BigDecimal;

/**
 * Table abstraction
 */
public class DatabaseTable implements Comparable<DatabaseTable> {

    private String owner;

    private String name;

    private BigDecimal rows;

    public DatabaseTable(String owner, String name, BigDecimal rows) {
        this.owner = owner;
        this.name = name;
        this.rows = rows == null ? BigDecimal.ZERO : rows;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public long getRows() {
        return rows.longValue();
    }

    @Override
    public String toString() {
        return owner + '.' + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseTable that = (DatabaseTable) o;

        return owner.equals(that.owner) && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(DatabaseTable otherTable) {
        int result = owner.compareTo(otherTable.owner);

        if (result == 0) {
            result = name.compareTo(otherTable.name);
        }

        return result;
    }
}
