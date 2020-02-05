import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by Aleksei Grishkov on 13.02.16.
 *
 * This class represents a percolation experiment on a N*N grid
 */
public class Percolation {

    private int n; // grid size
    private int openSites;
    private boolean[][] grid;
    private WeightedQuickUnionUF ufTopBottom, ufTop; // union-find helper structure
    private int vBottom, vTop; // virtual top and virtual bottom respectively

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be above 0");
        }
        this.n = n;
        this.grid = new boolean[n + 1][n + 1];
        this.ufTopBottom = new WeightedQuickUnionUF(n * n + 2);
        this.ufTop = new WeightedQuickUnionUF(n * n + 2);
        this.vBottom = 0;
        this.vTop = n * n + 1;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRanges(row, col);
        if (grid[row][col]) return;

        grid[row][col] = true;
        int site1D = xyTo1D(row, col);
        linkToNeighbor(site1D, row - 1, col);
        linkToNeighbor(site1D, row + 1, col);
        linkToNeighbor(site1D, row, col - 1);
        linkToNeighbor(site1D, row, col + 1);
        if (row == 1) {
            ufTopBottom.union(site1D, vTop);
            ufTop.union(site1D, vTop);
        }
        if (row == n) {
            ufTopBottom.union(site1D, vBottom);
        }
        openSites++;
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRanges(row, col);
        return grid[row][col];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRanges(row, col);
        return isOpen(row, col) && ufTop.connected(xyTo1D(row, col), vTop);
    }

    public int numberOfOpenSites() {
        return openSites;
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
        return (x - 1) * n + y;
    }

    private boolean withinGrid(int i, int j) {
        return (i >= 1 && i <= n) && (j >= 1 && j <= n);
    }

    private void checkRanges(int i, int j) {
        checkRange(i, 1, n);
        checkRange(j, 1, n);
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
