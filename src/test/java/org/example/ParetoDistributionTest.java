package org.example;

import org.example.distribution.ParetoDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParetoDistributionTest {
    @Test
    public void samplesHaveCorrectMinimumAndTail() {
        double alpha = 1.5;
        double scale = 1.0;
        ParetoDistribution p = new ParetoDistribution(alpha, scale);

        int N = 100_000;
        double minObserved = Double.POSITIVE_INFINITY;
        int tailCount = 0;
        double tailThreshold = 10.0;

        for (int i = 0; i < N; i++) {
            double x = p.TimeDuration();
            assertTrue("sample >= scale", x >= scale);
            minObserved = Math.min(minObserved, x);
            if (x >= tailThreshold)
                tailCount++;
        }

        // minObserved should be close to scale (not less)
        assertTrue(minObserved >= scale);

        // tailCount fraction should be > 0 for heavy tail (sanity check)
        double tailFrac = tailCount / (double) N;
        assertTrue("non‑zero tail fraction", tailFrac > 0.0001);
    }
}
