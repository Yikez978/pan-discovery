package org.alcibiade.pandiscovery.fs;

import java.nio.file.Path;

/**
 * Visitor for files in the folder trees.
 */
public interface FolderVisitor {
    void visit(Path path);
}
