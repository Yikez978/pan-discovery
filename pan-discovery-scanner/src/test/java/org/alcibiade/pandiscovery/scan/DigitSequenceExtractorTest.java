package org.alcibiade.pandiscovery.scan;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DigitSequenceExtractorTest {

    private Logger logger = LoggerFactory.getLogger(DigitSequenceExtractorTest.class);

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


    @Test
    public void testPerformance() {
        List<String> inputSet = new ArrayList<>();
        Random random = new Random();

        while (inputSet.size() < 1000000) {
            StringBuilder text = new StringBuilder();

            while (text.length() < 128) {
                text.append((char) ('0' + random.nextInt(10)));

                if (random.nextInt(10) == 0) {
                    text.append((char) ('a' + random.nextInt(26)));
                }
            }

            inputSet.add(text.toString());
        }

        DigitSequenceExtractor extractor = new DigitSequenceExtractor(16);

        long tsStart = System.currentTimeMillis();
        inputSet.forEach(extractor::extractSequences);
        long tsEnd = System.currentTimeMillis();

        logger.debug("Extraction duration: {}ms", (tsEnd - tsStart));
    }
}
