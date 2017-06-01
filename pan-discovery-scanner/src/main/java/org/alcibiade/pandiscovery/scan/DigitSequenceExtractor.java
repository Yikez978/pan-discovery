package org.alcibiade.pandiscovery.scan;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        Pattern sequencePattern = Pattern.compile("(?:^|\\D)(((\\d)(?:\\W?)){" + this.sequenceLength + "})(?:$|\\D)");
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
