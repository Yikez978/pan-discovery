package org.alcibiade.pandiscovery.scan;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DigitSequenceExtractorTest {

    @Test
    public void testExtration() {
        DigitSequenceExtractor extractor = new DigitSequenceExtractor();

        Assertions.assertThat(extractor.extractSequences("Hello")).isEmpty();
        Assertions.assertThat(extractor.extractSequences(
            "0123456789012345")).containsExactly("0123456789012345");
        Assertions.assertThat(extractor.extractSequences(
            "Hello 0123456789012345")).containsExactly("0123456789012345");
        Assertions.assertThat(extractor.extractSequences(
            "Hello 0123 4567 8901 2345")).containsExactly("0123456789012345");

        Assertions.assertThat(extractor.extractSequences(
            "Hello 0123 4567  8901 2345")).isEmpty();

        Assertions.assertThat(extractor.extractSequences(
            "Hello 0123 4567-8901 2345")).containsExactly("0123456789012345");

        Assertions.assertThat(extractor.extractSequences(
            "0123456789012345 4567890123456789"))
            .containsExactly("0123456789012345", "4567890123456789");

    }

    @Test
    public void testCustomLength() {
        DigitSequenceExtractor extractor = new DigitSequenceExtractor(4);

        Assertions.assertThat(extractor.extractSequences("Hello")).isEmpty();
        Assertions.assertThat(extractor.extractSequences(
            "0123 4567 89012345")).hasSize(2).contains("4567");
    }
}
