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
        Stream<Path> result = Stream.empty();

        for (String pathString : paths) {
            Path p = Paths.get(pathString);
            logger.info("Scanning folder {}", p);

            try {
                Stream<Path> filePaths = Files.walk(p).filter(path -> Files.isRegularFile(path));
                result = Stream.concat(result, filePaths);
            } catch (IOException e) {
                throw new IllegalStateException("IO error while walking folder " + p, e);
            }
        }

        return result;
    }

}
