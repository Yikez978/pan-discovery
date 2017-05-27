package org.alcibiade.pandiscovery.db.command;

import org.alcibiade.pandiscovery.db.model.DatabaseField;
import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.alcibiade.pandiscovery.db.service.DiscoveryService;
import org.alcibiade.pandiscovery.db.service.ExportService;
import org.alcibiade.pandiscovery.db.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default run action command for DB scans.
 */
@Component
@Profile("default")
public class DiscoveryCommand implements ApplicationRunner {
    private Logger logger = LoggerFactory.getLogger(DiscoveryCommand.class);

    private DiscoveryService discoveryService;
    private Set<ExportService> exportServices;
    private MessageService messageService;

    @Autowired(required = false)
    public DiscoveryCommand(DiscoveryService discoveryService, Set<ExportService> exportServices, MessageService messageService) {
        this.discoveryService = discoveryService;
        this.exportServices = exportServices;
        this.messageService = messageService;
    }

    @Autowired(required = false)
    public DiscoveryCommand(DiscoveryService discoveryService, Set<ExportService> exportServices) {
        this.discoveryService = discoveryService;
        this.exportServices = exportServices;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        DiscoveryReport report = discoveryService.runDiscovery(applicationArguments.getSourceArgs());

        logger.info("Results:", report);

        for (DatabaseField field : report) {
            logger.info("  {} - {}", field, report.getMatches(field));
        }

        List<File> reportFiles = exportServices.stream()
                .map(svc -> svc.export(report))
                .collect(Collectors.toList());

        if (messageService != null) {
            messageService.sendReports(report, reportFiles);
        }
    }
}
