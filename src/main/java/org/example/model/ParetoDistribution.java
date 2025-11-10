package org.example.model;

import java.util.Random;

/**
 * Generator for random numbers following a Pareto distribution.
 * PDF: f(x) = α * x_m^α / x^{α+1},  for x ≥ x_m and α > 0
 */
public class ParetoDistribution {

    private final double shape;   // shape parameter
    private final double scale;     // scale parameter
    private final Random random;  // random number generator

    /**
     * Constructor for Pareto distribution.
     *
     * @param shape shape parameter α (>0)
     * @param scale   scale (minimum) parameter scale (>0)
     */
    public ParetoDistribution(double shape, double scale) {
        if (shape <= 0) throw new IllegalArgumentException("Shape parameter α must be > 0");
        if (scale <= 0) throw new IllegalArgumentException("Scale parameter scale must be > 0");

        this.shape = shape;
        this.scale = scale;
        this.random = new Random();
    }

    /**
     * Generates a Pareto-distributed random duration using inverse transform sampling.
     *
     * Formula:  X = scale * (1 - U)^(-1/α),  where U ~ Uniform(0, 1)
     *
     * @return a random Pareto-distributed value ≥ scale
     */
    public double TimeDuration() {
        double U = random.nextDouble(); // uniform(0,1)
        return scale / Math.pow(1 - U, 1.0 / shape);
    }


    // Getters for parameters
    public double getAlpha() { return shape; }
    public double getScale() { return scale; }
}

