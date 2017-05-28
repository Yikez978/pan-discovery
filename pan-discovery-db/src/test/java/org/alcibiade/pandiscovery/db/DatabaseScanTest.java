package org.alcibiade.pandiscovery.db;

import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.db.service.DiscoveryService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Initialize and scan a database.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseScanTest {

    private Logger logger = LoggerFactory.getLogger(DatabaseScanTest.class);

    @Autowired
    private DiscoveryService discoveryService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testScan() {
        logger.debug("Scanning a sample database {}", discoveryService);
        TableGenerator.createTable(jdbcTemplate);
        DiscoveryReport report = discoveryService.runDiscovery();
        Assertions.assertThat(report.getFields()).hasSize(1);
    }
}
