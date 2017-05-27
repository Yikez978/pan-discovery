package org.alcibiade.pandiscovery.db.service;

import org.alcibiade.pandiscovery.db.model.DiscoveryReport;

import java.io.File;

/**
 * Behaviours shared by all export services.
 */
public interface ExportService {
    File export(DiscoveryReport report);
}
