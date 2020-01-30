import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by Aleksei Grishkov on 13.02.16.
 *
 * This class represents a percolation experiment on a N*N grid
 */
public class Percolation {

    private int N; // grid size
    private boolean[][] grid;
    private WeightedQuickUnionUF ufTopBottom, ufTop; // union-find helper structure
    private int vBottom, vTop; // virtual top and virtual bottom respectively

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N should be above 0");
        }
        this.N = N;
        this.grid = new boolean[N + 1][N + 1];
        this.ufTopBottom = new WeightedQuickUnionUF(N * N + 2);
        this.ufTop = new WeightedQuickUnionUF(N * N + 2);
        this.vBottom = 0;
        this.vTop = N * N + 1;
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        checkRanges(i, j);
        grid[i][j] = true;
        int site1D = xyTo1D(i, j);
        linkToNeighbor(site1D, i - 1, j);
        linkToNeighbor(site1D, i + 1, j);
        linkToNeighbor(site1D, i, j - 1);
        linkToNeighbor(site1D, i, j + 1);
        if (i == 1) {
            ufTopBottom.union(site1D, vTop);
            ufTop.union(site1D, vTop);
        }
        if (i == N) {
            ufTopBottom.union(site1D, vBottom);
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        checkRanges(i, j);
        return grid[i][j];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        checkRanges(i, j);
        return isOpen(i, j) && ufTop.connected(xyTo1D(i, j), vTop);
    }

    // does the system percolate?
    public boolean percolates() {
        return ufTopBottom.connected(vBottom, vTop);
    }

    private void linkToNeighbor(int site1D, int ni, int nj) {
        if (withinGrid(ni, nj) && isOpen(ni, nj)) {
            ufTopBottom.union(site1D, xyTo1D(ni, nj));
            ufTop.union(site1D, xyTo1D(ni, nj));
        }
    }

    private int xyTo1D(int x, int y) {
        return (x - 1) * N + y;
    }

    private boolean withinGrid(int i, int j) {
        return (i >= 1 && i <= N) && (j >= 1 && j <= N);
    }

    private void checkRanges(int i, int j) {
        checkRange(i, 1, N);
        checkRange(j, 1, N);
    }

    private void checkRange(int index, int min, int max) {
        if (index < min || index > max) {
            throw new IndexOutOfBoundsException(String.format("Index should be in [%d, %d]", min, max));
        }
    }

    public static void main(String[] args) {
        final int N = 20;
        Percolation p = new Percolation(N);
        int opened = 0;
        do {
            int i = StdRandom.uniform(1, N + 1);
            int j = StdRandom.uniform(1, N + 1);
            if (!p.isOpen(i, j)) {
                p.open(i, j);
                opened++;
                StdOut.printf("%3d %3d\n", i, j);
            }
        }
        while (!p.percolates());
        StdOut.println("The percolation threshold is " + (double) opened / (N * N));
    }

}
