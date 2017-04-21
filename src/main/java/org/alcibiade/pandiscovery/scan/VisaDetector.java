package org.alcibiade.pandiscovery.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detect VISA cards.
 */
@Component
public class VisaDetector implements Detector {
    private Logger logger = LoggerFactory.getLogger(VisaDetector.class);
    private Pattern cardPattern = Pattern.compile("4[0-9]{12}[0-9]{3}");

    private TextStripper textStripper;
    private Luhn luhn;

    @Autowired
    public VisaDetector(TextStripper textStripper, Luhn luhn) {
        this.textStripper = textStripper;
        this.luhn = luhn;
    }

    @Override
    public CardType detectMatch(String text) {
        String strippedText = textStripper.strip(text);

        CardType result = null;
        Matcher matcher = cardPattern.matcher(strippedText);

        int index = 0;

        while (result == null && matcher.find(index)) {
            String group = matcher.group();
            index = matcher.start() + 1;
            if (luhn.check(group)) {
                result = CardType.VISA;
            }
        }

        return result;
    }
}
