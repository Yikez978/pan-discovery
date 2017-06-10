package org.alcibiade.pandiscovery.scan.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Accumulate digits read from text sequences.
 */
public class DigitAccumulator {
    private List<String> sequences = new ArrayList<>();
    private int[] queue;
    private boolean[] seqStart;
    private int size = 0;
    private boolean onDelimiter = true;

    private int sequenceLength;

    public DigitAccumulator(int sequenceLength) {
        this.sequenceLength = sequenceLength;
        queue = new int[sequenceLength];
        seqStart = new boolean[sequenceLength];
    }

    public void consumeCharacter(int c) {
        boolean isDigit = Character.isDigit(c);

        /*
         * Process the inbound character.
         */

        if (isDigit) {
            // Digits are accumulated

            // If full, shift buffer
            if (size == sequenceLength) {
                shift();
            }

            // Append the new value
            queue[size] = c;
            seqStart[size] = onDelimiter;
            size++;
        } else if (onDelimiter) {
            // Two delimiters ends a possible sequence;
            size = 0;
        } else {

            /*
             * Assess if the current buffer is a valid sequence.
             */

            if (size == sequenceLength) {
                int countSeqStarts = 0;
                for (int i = 0; i < sequenceLength; i++) {
                    if (seqStart[i]) {
                        countSeqStarts++;
                    }
                }

                if (seqStart[0] && countSeqStarts <= sequenceLength / 4) {
                    String s = new String(queue, 0, sequenceLength);
                    sequences.add(s);
                }

                shift();
            }
        }

        onDelimiter = !isDigit;
    }

    private void shift() {
        for (int i = 1; i < sequenceLength; i++) {
            queue[i - 1] = queue[i];
            seqStart[i - 1] = seqStart[i];
        }

        size--;
    }

    public List<String> getSequences() {
        return sequences;
    }
}
