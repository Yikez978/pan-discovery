package org.alcibiade.pandiscovery.scan;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VisaDetectorTest {

    @Test
    public void testVisaDetection() {
        Detector detector = new VisaDetector(new TextStripper(), new Luhn());
        Assertions.assertThat(detector.detectMatch("Hello")).isNull();
        Assertions.assertThat(detector.detectMatch("5417057527335935")).isNull();
        Assertions.assertThat(detector.detectMatch("6011145506624417")).isNull();
        Assertions.assertThat(detector.detectMatch("6011145506624417")).isNull();
        Assertions.assertThat(detector.detectMatch("+43 662 83 990 660")).isNull();
        Assertions.assertThat(detector.detectMatch("+43 676 8541 8823")).isNull();

        Assertions.assertThat(detector.detectMatch("The card is 4783853934638427, yeah !")).isNotNull();
        Assertions.assertThat(detector.detectMatch("01234567    47838539 34638427 01234567")).isNotNull();
    }
}
