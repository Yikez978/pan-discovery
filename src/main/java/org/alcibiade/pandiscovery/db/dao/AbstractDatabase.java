package org.alcibiade.pandiscovery.db.dao;

import org.alcibiade.pandiscovery.db.model.DatabaseField;
import org.alcibiade.pandiscovery.db.model.DatabaseTable;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.scan.CardType;
import org.alcibiade.pandiscovery.scan.Detector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Database access abstraction
 */
@Component
public class AbstractDatabase {

    private final int fetchSize;
    private Logger logger = LoggerFactory.getLogger(AbstractDatabase.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AbstractDatabase(
            @Value("${pan-discovery.db.fetchsize:100}") int fetchSize,
            JdbcTemplate jdbcTemplate) {
        this.fetchSize = fetchSize;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public String getDatabaseName() {
        String dbName = jdbcTemplate.queryForObject("select name from v$database", String.class);
        return dbName;
    }

    @Transactional(readOnly = true)
    public SortedSet<DatabaseTable> getAllTables(String prefix) {
        return jdbcTemplate.queryForList(
                "select OWNER, TABLE_NAME, NUM_ROWS" +
                        " from all_tables" +
                        " where (iot_type is null or iot_type = 'IOT')" +
                        "     and NESTED = 'NO' and secondary = 'N'" +
                        "     and TABLESPACE_NAME is not null"
        ).stream()
                .map(record -> new DatabaseTable(
                        record.get("OWNER").toString(),
                        record.get("TABLE_NAME").toString(),
                        (BigDecimal) record.get("NUM_ROWS")
                ))
                .filter(t -> prefix == null || t.toString().toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    @Transactional(readOnly = true)
    public int scan(DatabaseTable table, Set<Detector> detectors, DiscoveryReport report) {
        RowCountCallbackHandler callback = new RowCountCallbackHandler() {
            @Override
            public void processRow(ResultSet rs, int rowNum) throws SQLException {
                for (int col = 0; col < getColumnCount(); col++) {
                    int type = getColumnTypes()[col];

                    if (type != Types.CHAR && type != Types.VARCHAR) {
                        continue;
                    }

                    String value = rs.getString(col + 1);
                    if (value == null) {
                        continue;
                    }

                    for (Detector detector : detectors) {
                        CardType match = detector.detectMatch(value);
                        if (match != null) {
                            DatabaseField field = new DatabaseField(table, getColumnNames()[col]);
                            report.report(field, match, value);
                            logger.debug("Reporting {} as {} in {}", value, match, field);
                        }
                    }
                }
            }
        };

        try {
            DataSource dataSource = jdbcTemplate.getDataSource();

            if (logger.isTraceEnabled() && dataSource instanceof org.apache.tomcat.jdbc.pool.DataSource) {
                org.apache.tomcat.jdbc.pool.DataSource tcDs = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;

                logger.trace("Reading table {} on pool: Active={}/{}, Idle={}",
                        table, tcDs.getActive(), tcDs.getMaxActive(), tcDs.getIdle());
            }

            jdbcTemplate.setFetchSize(fetchSize);
            jdbcTemplate.query("select * from " + table, callback);
        } catch (DataAccessException e) {
            logger.warn(e.getLocalizedMessage());
        }

        return callback.getRowCount();
    }
}
