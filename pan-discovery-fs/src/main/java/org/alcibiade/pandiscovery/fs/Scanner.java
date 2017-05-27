package org.alcibiade.pandiscovery.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application entry point.
 */

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"org.alcibiade.pandiscovery.fs", "org.alcibiade.pandiscovery.scan"})
@EnableScheduling
public class Scanner {

    public static void main(String[] args) throws Exception {
        try (ConfigurableApplicationContext context = SpringApplication.run(Scanner.class, args)) {
            FsDiscoveryRunner runner = context.getBean(FsDiscoveryRunner.class);
            runner.runScan();
        }
    }
}