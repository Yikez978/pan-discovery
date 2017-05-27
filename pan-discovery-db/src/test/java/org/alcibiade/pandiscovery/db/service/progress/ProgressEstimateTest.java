package org.alcibiade.pandiscovery.db.service.progress;

import org.assertj.core.api.Assertions;
import org.junit.Test;

/**
 * Test progress estimation calculation.
 */
public class ProgressEstimateTest {

    @Test
    public void testStart() {
        // Progress is 0% after 100ms
        ProgressEstimate estimate = new ProgressEstimate(0, 10, 1000, 1100);
        Assertions.assertThat(estimate.getElapsedTime()).isEqualTo("  0h:00min");
        Assertions.assertThat(estimate.getRemainingTime()).isEqualTo("N/A");
    }

    @Test
    public void testShortPartial() {
        // Progress is 10% after 12s
        ProgressEstimate estimate = new ProgressEstimate(1, 10, 1000, 1000 + 12_000);
        Assertions.assertThat(estimate.getElapsedTime()).isEqualTo("  0h:00min");
        Assertions.assertThat(estimate.getRemainingTime()).isEqualTo("  0h:01min");
    }

    @Test
    public void testLongPartial() {
        // Progress is 25% after 1h
        ProgressEstimate estimate = new ProgressEstimate(25, 100, 1000, 1000 + 3600_000);
        Assertions.assertThat(estimate.getElapsedTime()).isEqualTo("  1h:00min");
        Assertions.assertThat(estimate.getRemainingTime()).isEqualTo("  3h:00min");
    }


    @Test
    public void testComplete() {
        // Progress is complete after 1h
        ProgressEstimate estimate = new ProgressEstimate(100, 100, 1000, 1000 + 3600_000);
        Assertions.assertThat(estimate.getElapsedTime()).isEqualTo("  1h:00min");
        Assertions.assertThat(estimate.getRemainingTime()).isEqualTo("  0h:00min");
    }
}
