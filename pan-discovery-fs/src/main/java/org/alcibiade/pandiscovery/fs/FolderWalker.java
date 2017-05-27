package org.alcibiade.pandiscovery.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

/**
 * Explore paths as a stream.
 */
public class FolderWalker {
    private Logger logger = LoggerFactory.getLogger(FolderWalker.class);

    private List<String> paths;

    public FolderWalker(List<String> paths) {
        this.paths = paths;
    }

    public Stream<Path> walk() {
        return paths.stream()
                .map(pathString -> {
                    Path p = Paths.get(pathString);
                    logger.info("Scanning folder {}", p);
                    try {
                        return Files.walk(p).filter(path -> Files.isRegularFile(path));
                    } catch (IOException e) {
                        throw new IllegalStateException("IO error while walking folder " + p, e);
                    }
                })
                .reduce(Stream::concat)
                .orElseGet(Stream::empty);
    }

}
