package org.alcibiade.pandiscovery.db;

import org.alcibiade.pandiscovery.db.service.DiscoveryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    public void testScan() {
        logger.debug("Scanning a sample database {}", discoveryService);
    }
}
