package org.alcibiade.pandiscovery.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detect MasterCard cards.
 */
@Component
public class MasterCardDetector implements Detector {
    private Pattern cardPattern = Pattern.compile("(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}");
    private DigitSequenceExtractor sequenceExtractor = new DigitSequenceExtractor(16);
    private Luhn luhn;

    @Autowired
    public MasterCardDetector(Luhn luhn) {
        this.luhn = luhn;
    }

    @Override
    public DetectionResult detectMatch(String text) {
        for (String sequence : sequenceExtractor.extractSequences(text)) {
            Matcher matcher = cardPattern.matcher(sequence);
            if (matcher.matches() && luhn.check(sequence)) {
                return new DetectionResult(CardType.MASTERCARD, sequence);
            }
        }

        return null;
    }
}
