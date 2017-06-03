package org.alcibiade.pandiscovery.fs;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test custom directory walker.
 */
public class FolderWalkerTest {

    @Test
    public void testBrowsing() {
        List<String> paths = new ArrayList<>();
        paths.add("samples");
        FolderWalker walker = new FolderWalker(paths);

        Assertions.assertThat(walker.walk().count()).isEqualTo(12);
    }

    @Test
    public void testMultiple() {
        List<String> paths = new ArrayList<>();
        paths.add("samples");
        paths.add("samples");
        FolderWalker walker = new FolderWalker(paths);

        Assertions.assertThat(walker.walk().count()).isEqualTo(24);
    }
}
