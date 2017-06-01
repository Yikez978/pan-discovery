package org.alcibiade.pandiscovery.scan;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MasterCardDetectorTest {

    @Test
    public void testMasterCardDetection() {
        Detector detector = new MasterCardDetector(new Luhn());
        Assertions.assertThat(detector.detectMatch("Hello")).isNull();
        Assertions.assertThat(detector.detectMatch("6011108511292273")).isNull();
        Assertions.assertThat(detector.detectMatch("4155446812519189")).isNull();
        Assertions.assertThat(detector.detectMatch("349302120255005")).isNull();
        Assertions.assertThat(detector.detectMatch(" pnr 22XG9F\n" +
            "tkt 149-2100761728\n" +
            "pls refund ticket")).isNull();

        Assertions.assertThat(detector.detectMatch("5163170283591009")).isNotNull();
        Assertions.assertThat(detector.detectMatch("5207843835205199")).isNotNull();
        Assertions.assertThat(detector.detectMatch("5337043772669555")).isNotNull();
        Assertions.assertThat(detector.detectMatch("5575266590168168")).isNotNull();

        Assertions.assertThat(detector.detectMatch("5575 2665 9016 8168")).isNotNull();
        Assertions.assertThat(detector.detectMatch("5575-2665-9016-8168")).isNotNull();
        Assertions.assertThat(detector.detectMatch("5575  26659016 8168")).isNull();

        Assertions.assertThat(detector.detectMatch("The card is 5207843835205199, yeah !")).isNotNull();
        Assertions.assertThat(detector.detectMatch("01234567    52078438 35205199 01234567")).isNotNull();
    }
}
