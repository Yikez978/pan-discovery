package org.alcibiade.pandiscovery.db.model;

/**
 * Path to a database field.
 */
public class DatabaseField implements Comparable<DatabaseField> {

    private DatabaseTable table;

    private String name;

    public DatabaseField(DatabaseTable table, String name) {
        this.table = table;
        this.name = name;
    }

    public DatabaseTable getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return table.toString() + '.' + name;
    }

    @Override
    public int compareTo(DatabaseField otherField) {
        int result = table.compareTo(otherField.table);

        if (result == 0) {
            result = name.compareTo(otherField.name);
        }

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseField that = (DatabaseField) o;

        return table.equals(that.table) && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = table.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
