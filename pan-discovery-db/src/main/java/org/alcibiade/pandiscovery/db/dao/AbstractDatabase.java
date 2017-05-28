package org.alcibiade.pandiscovery.db.dao;

import org.alcibiade.pandiscovery.db.model.DatabaseField;
import org.alcibiade.pandiscovery.db.model.DatabaseTable;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.scan.DetectionResult;
import org.alcibiade.pandiscovery.scan.Detector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCountCallbackHandler;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Database access abstraction
 */
@Component
public class AbstractDatabase {

    private final int fetchSize;
    private Logger logger = LoggerFactory.getLogger(AbstractDatabase.class);
    private JdbcTemplate jdbcTemplate;
    private String dbName = "DB";
    private SortedSet<DatabaseTable> allTables = new TreeSet<>();

    @Autowired
    public AbstractDatabase(
        @Value("${pan-discovery.db.fetchsize:100}") int fetchSize,
        JdbcTemplate jdbcTemplate) {
        this.fetchSize = fetchSize;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() throws MetaDataAccessException {
        final String[] objectTypes = {"TABLE"};

        JdbcUtils.extractDatabaseMetaData(jdbcTemplate.getDataSource(), databaseMetaData -> {
            ResultSet tablesResultSet = databaseMetaData.getTables(
                null, null, null, objectTypes);

            while (tablesResultSet.next()) {
                String owner = tablesResultSet.getString("TABLE_SCHEM");
                String name = tablesResultSet.getString("TABLE_NAME");
                BigDecimal rows = BigDecimal.ONE;
                DatabaseTable dbTable = new DatabaseTable(owner, name, rows);
                allTables.add(dbTable);
            }

            return null;
        });
    }

    @Transactional(readOnly = true)
    public String getDatabaseName() {
        return dbName;
    }

    @Transactional(readOnly = true)
    public SortedSet<DatabaseTable> getAllTables(String prefix) {
        return this.allTables;
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
                        DetectionResult result = detector.detectMatch(value);
                        if (result != null) {
                            DatabaseField field = new DatabaseField(table, getColumnNames()[col]);
                            report.report(field, result.getCardType(), value);
                            logger.debug("Reporting {} as {} in {}", value, result.getCardType(), field);
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
