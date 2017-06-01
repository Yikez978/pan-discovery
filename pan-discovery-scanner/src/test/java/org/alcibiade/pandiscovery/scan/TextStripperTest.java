package org.alcibiade.pandiscovery.scan;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class TextStripperTest {

    @Test
    public void testStripper() {
        TextStripper stripper = new TextStripper();
        Assertions.assertThat(stripper.strip("123")).isEqualTo("123");
        Assertions.assertThat(stripper.strip("a1b2c3")).isEqualTo("123");
    }
}
