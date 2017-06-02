package org.alcibiade.pandiscovery.scan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extract digit sequences from a String.
 */
public class DigitSequenceExtractor {

    private Pattern sequencePattern;

    public DigitSequenceExtractor(int sequenceLength) {
        sequencePattern = Pattern.compile("(?:^|\\D)(((\\d)(?:\\W?)){" + sequenceLength + "})(?:$|\\D)");
    }

    public DigitSequenceExtractor() {
        this(16);
    }

    public List<String> extractSequences(String text) {
        List<String> sequences = new ArrayList<>();

        Matcher matcher = sequencePattern.matcher(text);

        int index = 0;

        while (matcher.find(index)) {
            String group = matcher.group(1);
            index = matcher.start() + 1;
            sequences.add(group.replaceAll("\\D", ""));
        }


        return sequences;
    }
}
