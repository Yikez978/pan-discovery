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

    private TextStripper textStripper;

    private Luhn luhn;

    @Autowired
    public MasterCardDetector(TextStripper textStripper, Luhn luhn) {
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
                result = CardType.MASTERCARD;
            }
        }

        return result;
    }
}
