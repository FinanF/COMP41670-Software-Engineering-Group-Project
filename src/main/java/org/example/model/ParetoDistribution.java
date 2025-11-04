package org.example.model;

/**
 * Generator for random numbers following a Pareto distribution.
 * The Pareto distribution is a heavy-tailed distribution used to model
 * phenomena like file sizes, city populations, and network traffic durations.
 *
 */
public class ParetoDistribution {
    public int nth;
    public int N;
    public ParetoDistribution(int nth, int N) {
        this.nth = nth;
        this.N = N;
    }
    public double TimeDuration(){
        double random = new Random().nextDouble();
        if((double) nth /N <0.8){
            double time= 2.0+(5.0-2.0)*random;
            return time*(double) nth /N ;
        }else{
            double time= 6.0+(10.0-6.0)*random;
            return time*(double) nth /N ;
        }
    }
}
