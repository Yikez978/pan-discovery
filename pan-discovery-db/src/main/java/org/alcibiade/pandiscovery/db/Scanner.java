package org.alcibiade.pandiscovery.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application entry point.
 */

@SpringBootApplication
@ComponentScan(basePackages = {"org.alcibiade.pandiscovery.db", "org.alcibiade.pandiscovery.scan"})
public class Scanner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Scanner.class, args);
    }
}