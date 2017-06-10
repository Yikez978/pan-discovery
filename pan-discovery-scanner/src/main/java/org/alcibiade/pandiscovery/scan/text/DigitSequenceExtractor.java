package org.alcibiade.pandiscovery.scan.text;

import java.util.List;

/**
 * Extract digit sequences from a String.
 */
public class DigitSequenceExtractor {
    private int sequenceLength;

    public DigitSequenceExtractor(int sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public DigitSequenceExtractor() {
        this(16);
    }

    public List<String> extractSequences(String text) {

        DigitAccumulator accumulator = new DigitAccumulator(this.sequenceLength);

        text.chars().forEach(accumulator::consumeCharacter);

        accumulator.consumeCharacter(' ');
        return accumulator.getSequences();
    }
}
