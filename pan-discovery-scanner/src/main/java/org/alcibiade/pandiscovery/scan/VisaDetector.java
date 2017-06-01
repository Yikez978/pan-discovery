package org.alcibiade.pandiscovery.scan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detect VISA cards.
 */
@Component
public class VisaDetector implements Detector {
    private Pattern cardPattern = Pattern.compile("4[0-9]{12}[0-9]{3}");
    private DigitSequenceExtractor sequenceExtractor = new DigitSequenceExtractor(16);
    private Luhn luhn;

    @Autowired
    public VisaDetector(Luhn luhn) {
        this.luhn = luhn;
    }

    @Override
    public DetectionResult detectMatch(String text) {
        for (String sequence : sequenceExtractor.extractSequences(text)) {
            Matcher matcher = cardPattern.matcher(sequence);
            if (matcher.matches() && luhn.check(sequence)) {
                return new DetectionResult(CardType.VISA, sequence);
            }
        }

        return null;
    }
}
