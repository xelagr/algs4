import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by Aleksei Grishkov on 13.02.16.
 *
 * This class is used for gathering percolation statistics.
 * It can be run via main() method with arguments N (grid size) and T (number of experiments).
 */
public class PercolationStats {

    private double[] percolationThresholds;
    private int n, t;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("Both N and T should be above 0");
        }
        this.n = n;
        this.t = t;
        percolationThresholds = new double[t];
        for (int i = 0; i < t; i++) {
            percolationThresholds[i] = runExperiment(new Percolation(n));
        }
    }

    private double runExperiment(Percolation p) {
        int opened = 0;
        do {
            int i = StdRandom.uniform(1, n + 1);
            int j = StdRandom.uniform(1, n + 1);
            if (!p.isOpen(i, j)) {
                p.open(i, j);
                opened++;
            }
        }
        while (!p.percolates());
        return (double) opened / (n * n);
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
        return mean() - (1.96 * stddev() / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(t));
    }

    public static void main(String[] args) {
        Stopwatch sw = new Stopwatch();
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
        System.out.println("Total time: " + sw.elapsedTime());
    }

}
