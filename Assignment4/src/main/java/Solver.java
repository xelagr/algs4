import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * A solve algorithm for 8-puzzle problem
 *
 * Created by Aleksei Grishkov on 20.03.2016.
 */
public class Solver {

    private int steps;
    private Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("The board must not be null");
        }
        MinPQ<SearchNode> searchNodes = new MinPQ<>();
        MinPQ<SearchNode> twinSearchNodes = new MinPQ<>();
        searchNodes.insert(new SearchNode(initial, 0, null));
        twinSearchNodes.insert(new SearchNode(initial.twin(), 0, null));
        SearchNode min, minTwin;

        do {
            steps++;
            min = addNeigborsAndGetMin(searchNodes);
            minTwin = addNeigborsAndGetMin(twinSearchNodes);
        } while (!min.getBoard().isGoal() && !minTwin.getBoard().isGoal());

        if (min.getBoard().isGoal()) {
            initSolution(min);
        }
//        System.out.println("Steps: " + steps);
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return isSolvable() ? solution.size() - 1 : -1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readShort();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            int i = 0;
            for (Board board : solver.solution()) {
                StdOut.print(board);
                StdOut.printf("manhattan: %d, moves: %d\r\n\r\n", board.manhattan(), i++);
            }
        }
//        System.out.println("steps: " + solver.steps);
//        System.out.println("moves: " + solver.moves());
    }

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        @Override
        public int compareTo(SearchNode node) {
            int diff = board.manhattan() + moves - (node.getBoard().manhattan() + node.moves);
            if (diff == 0) {
                diff = board.manhattan() - node.getBoard().manhattan();
            }
            return diff;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            SearchNode that = (SearchNode) obj;
            return priority() == that.priority();
        }

        @Override
        public int hashCode() {
            return moves;
        }

        private int priority() {
            return board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int getMoves() {
            return moves;
        }
    }

    private void initSolution(SearchNode goal) {
        solution = new Stack<>();
        SearchNode current = goal;
        do {
            solution.push(current.getBoard());
            current = current.getPrevious();
        } while (current != null);
    }

    private SearchNode addNeigborsAndGetMin(MinPQ<SearchNode> searchNodes) {
        SearchNode min = searchNodes.delMin();
        for (Board board : min.getBoard().neighbors()) {
            if (min.getPrevious() == null || !board.equals(min.getPrevious().getBoard())) {
                searchNodes.insert(new SearchNode(board, min.moves+1, min));
            }
        }
        return min;
    }

}
