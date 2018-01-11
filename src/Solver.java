import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Comparator;
import java.lang.IllegalArgumentException;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {

    private class Node {
        private final Node predecessor;
        private final Board board;
        private final int moves;
        private final int score;

        public Node(Board board, int moves, Node predecessor) {
            this.board = board;
            this.moves = moves;
            this.predecessor = predecessor;
            this.score = board.manhattan() + moves;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }
    }

    private int moves;
    private boolean solvable;
    private Node last = null;

    public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
        if(initial == null)
            throw new IllegalArgumentException();

        Comparator<Node> nodeComparator = (Node a, Node b) -> {
            return Integer.compare(a.score, b.score);
        };

        MinPQ<Node> pq = new MinPQ<Node>(1, nodeComparator);
        pq.insert(new Node(initial, 0, null));

        MinPQ<Node> pqAlt = new MinPQ<Node>(1, nodeComparator);
        pqAlt.insert(new Node(initial.twin(), 0, null));

        Node test;
        boolean found, foundAlt;
        do {
            found = step(pq);
            foundAlt = step(pqAlt);
        } while (!(found || foundAlt));

        moves = found ? pq.min().getMoves() : -1;
        solvable = found;
    }

    private boolean step(MinPQ<Node> pq) {
        Node test = pq.min();
        if (test != null && test.getBoard().isGoal()) {
            last = test;
            return true;
        }
        test = pq.delMin();
        for (Board neighbor: test.getBoard().neighbors()) {
            if (null == test.predecessor || !neighbor.equals(test.predecessor.board)) {
                pq.insert(new Node(neighbor, test.moves + 1, test));
            }
        }
        return false;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {                     // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }

    public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
        if(!solvable)
            return null;

        ArrayList<Board> it = new ArrayList<Board>();
        Node tracer = last;
        do {
            it.add(0, tracer.board);
            tracer = tracer.predecessor;
        } while (tracer != null);

        return it;
    }

    public static void main(String[] args) {
//        testAlreadySolved();
//        testOneMoveSimple();
//        testUnsolvableSimple();
//        testBiggerSolvable();
//        testBiggerUnsolvable();
//        testMoves();

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    } // solve a slider puzzle (given below)

    private static final int[][] TESTBLOCKS0 = {{1, 2}, {3, 0}};
    private static final int[][] TESTBLOCKS1 = {{1, 2}, {0, 3}};
    private static final int[][] TESTBLOCKS2 = {{1, 3}, {0, 2}};
    private static final int[][] TESTBLOCKS3 = {
            {1, 2, 3},
            {7, 0, 4},
            {8, 6, 5}
    };
    private static final int[][] TESTBLOCKS4 = {
            {1, 2, 3},
            {4, 5, 6},
            {8, 7, 0}
    };
    private static final int[][] TEST_MOVES = {
            {2,  3,  4,  8},
            {1,  6,  0, 12},
            {5, 10,  7, 11},
            {9, 13, 14, 15}
    };

    private static void testAlreadySolved() {
        Solver s = new Solver(new Board(TESTBLOCKS0));

        System.out.println(s.moves);
        String description = "board already solved";
        String p = s.moves == 0 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testOneMoveSimple() {
        Solver s = new Solver(new Board(TESTBLOCKS1));

        String description = "simple board one move";
        String p = s.moves == 1 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testUnsolvableSimple() {
        Solver s = new Solver(new Board(TESTBLOCKS2));

        String description = "simple board no solution";
        String p = s.moves == -1 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testBiggerSolvable() {
        Solver s = new Solver(new Board(TESTBLOCKS3));

        String description = "bigger board solvable";
        String p = s.moves == 8 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testBiggerUnsolvable() {
        Solver s = new Solver(new Board(TESTBLOCKS4));

        System.out.println(s.moves);
        String description = "bigger board unsolvable";
        String p = s.moves == -1 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testMoves() {
        Solver s = new Solver(new Board(TEST_MOVES));

        System.out.println(s.moves);
        String description = "moves";
        String p = s.moves == 13 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

//    private static void testImmutable() {
//        Board board = new Board(TEST_IMMUTABLE);
//        System.out.println(board.manhattan());
//        board.swapBlocks(TEST_IMMUTABLE, 0,0,1,0);
//        System.out.println(board.manhattan());
//    }

}