import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.List;

/**
 * The board class for 8-puzzle problem
 * The board is a square matrix with an one blank block
 *
 * Created by Aleksei Grishkov on 20.03.2016.
 */
public class Board {

    private static final int MIN_N = 2;
    private static final int MAX_N = 128;

    private final short[][] blocks;
    private final int N;
    private int blankRow;
    private int blankCol;
    private int manhattan;
    private int hamming;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this(optimizeBlockSize(blocks));
    }

    private Board(short[][] blocks) {
        this.N = blocks.length;
        if (N < MIN_N || N >= MAX_N) {
            throw new IllegalArgumentException("2 ≤ N < 128 is required");
        }
        this.blocks = blocks;
        initBlankBlockIndices();
        manhattan = -1;
        hamming = -1;
    }

    private static short[][] optimizeBlockSize(int[][] blocks) {
        int N = blocks.length;
        short[][] optimizedBlocks = new short[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                optimizedBlocks[i][j] = (short) blocks[i][j];
            }
        }
        return optimizedBlocks;
    }

    private void initBlankBlockIndices() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (updateBlankBlockIndices(i, j)) return;
            }
        }
    }

    // board dimension N
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {
        if (hamming == -1) {
            hamming = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (i != blankRow || j != blankCol) {
                        hamming += rowColTo1D(i, j) == blocks[i][j] ? 0 : 1;
                    }
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan == -1) {
            manhattan = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (i != blankRow || j != blankCol) {
                        manhattan += blockManhattanDistance(i, j);
                    }
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twin = boardClone();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int nextJ = (j + 1) % N;
                int nextI = nextJ > j ? i : (i + 1) % N;
                if (blocks[i][j] != 0 && blocks[nextI][nextJ] != 0) {
                    twin.swap(i, j, nextI, nextJ);
                    return twin;
                }
            }
        }
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null || getClass() != y.getClass()) return false;
        Board b = (Board) y;
        if (dimension() != b.dimension()) return false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != b.blocks[i][j]) return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>(4);
        if (blankRow - 1 >= 0) {
            neighbors.add(makeNeighbor(blankRow - 1, blankCol, blankRow, blankCol));
        }
        if (blankRow + 1 < N) {
            neighbors.add(makeNeighbor(blankRow + 1, blankCol, blankRow, blankCol));
        }
        if (blankCol - 1 >= 0) {
            neighbors.add(makeNeighbor(blankRow, blankCol - 1, blankRow, blankCol));
        }
        if (blankCol + 1 < N) {
            neighbors.add(makeNeighbor(blankRow, blankCol + 1, blankRow, blankCol));
        }
        return neighbors;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder().append(N).append("\r\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(" ").append(blocks[i][j]);
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    // unit tests
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board b = new Board(blocks);
        System.out.println("toString: %n" + b.toString());
        System.out.println("dimension: " + b.dimension());
        System.out.println("hamming: " + b.hamming());
        System.out.println("manhattan: " + b.manhattan());
        System.out.println("isGoal: " + b.isGoal());
        System.out.println("twin: %n" + b.twin());
        Iterable<Board> neighbors = b.neighbors();
        int i = 1;
        for (Board neighbor : neighbors) {
            System.out.print(String.format("neighbor%d:%n %s", i++, neighbor));
            System.out.println("manhattan: " + neighbor.manhattan());
            System.out.println();
        }
    }

    private int rowColTo1D(int r, int c) {
        return r*N + c + 1;
    }

    private int blockManhattanDistance(int curRow, int curCol) {
        int targetCol = (blocks[curRow][curCol] - 1) % N;
        int targetRow = (blocks[curRow][curCol] - targetCol - 1) / N;
        return Math.abs(curCol - targetCol) + Math.abs(curRow - targetRow);
    }

    private Board makeNeighbor(int i1, int j1, int i2, int j2) {
        Board neighbor = boardClone();
        neighbor.swap(i1, j1, i2, j2);
        return neighbor;
    }

    private Board boardClone() {
        return new Board(blocksClone(blocks));
    }

    private short[][] blocksClone(short[][] oldBlocks) {
        short[][] newBlocks = oldBlocks.clone();
        for (int i = 0; i < N; i++) {
            newBlocks[i] = oldBlocks[i].clone();
        }
        return newBlocks;
    }

    private void swap(int i1, int j1, int i2, int j2) {
        short tmp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = tmp;
        updateBlankBlockIndices(i1, j1);
        updateBlankBlockIndices(i2, j2);
    }

    private boolean updateBlankBlockIndices(int i, int j) {
        if (blocks[i][j] == 0) {
            blankRow = i;
            blankCol = j;
            return true;
        }
        return false;
    }

}
