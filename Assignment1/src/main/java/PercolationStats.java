import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by Aleksei Grishkov on 13.02.16.
 *
 * This class is used for gathering percolation statistics.
 * It can be run via main() method with arguments N (grid size) and T (number of experiments).
 */
public class PercolationStats {

    private double[] percolationThresholds;
    private int N, T;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Both N and T should be above 0");
        }
        this.N = N;
        this.T = T;
        percolationThresholds = new double[T];
        for (int i = 0; i < T; i++) {
            percolationThresholds[i] = runExperiment(new Percolation(N));
        }
    }

    private double runExperiment(Percolation p) {
        int opened = 0;
        do {
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            if (!p.isOpen(i, j)) {
                p.open(i, j);
                opened++;
            }
        }
        while (!p.percolates());
        return (double) opened / (N*N);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("Usage: PercolationStats N T");
            StdOut.println("\twhere N - grid size");
            StdOut.println("\twhere T - number of experiments");
            return;
        }
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean                    = %f\n", stats.mean());
        double stddev = stats.stddev();
        StdOut.printf("stddev                  = %f\n", stddev);
        if (Double.isNaN(stddev)) {
            StdOut.println("95% confidence interval = NaN, NaN");
        }
        else {
            StdOut.printf("95%% confidence interval = %f, %f\n", stats.confidenceLo(), stats.confidenceHi());
        }
    }

}
