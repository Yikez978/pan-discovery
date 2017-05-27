package org.alcibiade.pandiscovery.db.dao;

import org.alcibiade.pandiscovery.db.model.DatabaseTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validate schemas to process against a blacklist.
 */
@Component
public class SchemaBlacklist {
    private Set<String> blacklist;

    @Value("${pan-discovery.blacklist.schema:SYS,SYSMAN,SYSTEM,ORDSYS,EXFSYS,MDSYS," +
            "OLAPSYS,WMSYS,APPQOSSYS,CTXSYS,SQLTXPLAIN}")
    public void setBlacklist(String values) {
        blacklist = Arrays.stream(values.split(","))
                .map(name -> name.trim().toUpperCase())
                .collect(Collectors.toSet());
    }

    public boolean acceptsSchema(DatabaseTable table) {
        // First check blacklist
        boolean accepted = !blacklist.contains(table.getOwner().toUpperCase());

        if (accepted) {
            accepted = !table.getName().contains("$") && !table.getName().contains("=");
        }

        return accepted;
    }
}
