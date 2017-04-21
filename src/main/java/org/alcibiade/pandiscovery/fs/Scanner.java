package org.alcibiade.pandiscovery.fs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application entry point.
 */

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"org.alcibiade.pandiscovery.fs", "org.alcibiade.pandiscovery.scan"})
public class Scanner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Scanner.class, args);
    }
}