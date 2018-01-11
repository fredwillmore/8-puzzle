import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.ArrayList;

public class Board {

    private final int[][] blocks;
    private final int n;

    public Board(int[][] blocks) {
        this.blocks = blocks.clone();
        n = blocks.length;

    }

    public int dimension() {                 // board dimension n
        return n;
    }

    public int hamming() {                   // number of blocks out of place
        int score = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                score += hammingDistance(x, y, blocks[y][x]);
            }
        }
        return score;
    }

    private int hammingDistance(int x, int y, int v) {
        if (v != 0 && (goalCol(v) != x || goalRow(v) != y))
            return 1;
        else
            return 0;
    }

    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        int score = 0;
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                score += manhattanDistance(x, y, blocks[y][x]);
            }
        }
        return score;
    }

    private int manhattanDistance(int x, int y, int v) {
        return v == 0 ? 0 : Math.abs(goalCol(v) - x) + Math.abs(goalRow(v) - y);
    }

    private int goalRow(int value) {
        return (value - 1) / n;
    }

    private int goalCol(int value) {
        return (value - 1) % n;
    }

    private int goalValue(int x, int y) {
        return (y*n + x + 1) % (n*n);
    }

    public boolean isGoal() {                // is this board the goal board?
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != goalValue(j, i)) return false;
            }
        }
        return true;
    }

    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
        return new Board(twinBlocks());
    }

    private int[][] twinBlocks() {
        int[][] b = cloneBlocks();
        int x1 = 0, y1 = 0, x2 = 1, y2 = 0;

        if (b[y1][x1] == 0 || b[y2][x2] == 0) {
            y1 = 1;
            y2 = 1;
        }
        swapBlocks(b, x1, y1, x2, y2);
        return b;
    }

    private void swapBlocks(int[][] b, int x1, int y1, int x2, int y2) {
        int temp = b[y1][x1];
        b[y1][x1] = b[y2][x2];
        b[y2][x2] = temp;
    }

    private int[][] cloneBlocks() {
        int[][] b = new int[n][0];
        for (int i = 0; i < n; i++)
            b[i] = blocks[i].clone();
        return b;
    }

    public boolean equals(Object y) {        // does this board equal y?
        return y == null ? false : this.toString().equals(y.toString());
        // TODO: I don't want to depend on the string representation, but the API for Board doesn't give me access to blocks
    }

    public Iterable<Board> neighbors() {     // all neighboring boards
        ArrayList<Board> it = new ArrayList<Board>();
        int zeroRow = 0, zeroCol = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }
        int[][] neighborBlocks;
        if (zeroCol > 0) {
            neighborBlocks = cloneBlocks();
            swapBlocks(neighborBlocks, zeroCol-1, zeroRow, zeroCol, zeroRow);
            it.add(new Board(neighborBlocks));
        }
        if (zeroCol < n-1) {
            neighborBlocks = cloneBlocks();
            swapBlocks(neighborBlocks, zeroCol+1, zeroRow, zeroCol, zeroRow);
            it.add(new Board(neighborBlocks));
        }
        if (zeroRow > 0) {
            neighborBlocks = cloneBlocks();
            swapBlocks(neighborBlocks, zeroCol, zeroRow-1, zeroCol, zeroRow);
            it.add(new Board(neighborBlocks));
        }
        if (zeroRow < n-1) {
            neighborBlocks = cloneBlocks();
            swapBlocks(neighborBlocks, zeroCol, zeroRow+1, zeroCol, zeroRow);
            it.add(new Board(neighborBlocks));
        }

        return it;
    }

    public String toString() {               // string representation of this board (in the output format specified below)
        int fieldWidth = (int) Math.ceil(Math.log10(n*n)) + 1;
        String str = "" + n + "\n";
        for (int[] row: blocks) {
            for (int value: row) {
                String display = Integer.toString(value);
                str += String.format("%" + fieldWidth + "s", display);
            }
            str += "\n";
        }
        return str;
    }

    public static void main(String[] args) { // unit tests (not graded)
//        testBoard();
//        testEquals();
//        testTwin();
//        testIsGoal();
//        testHamming();
//        testManhattan();
//        testNeighbors();
//        testToString();
        testImmutable();
    }

    private static final int[][] testBlocks0 = {{1, 2}, {0, 3}};
    private static final int[][] testBlocks1 = {{4, 5, 6}, {0, 2, 3}, {7, 8, 1}};
    private static final int[][] testBlocks2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

    private static final int[][] testBlocks3 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
    private static final int[][] testBlocks4 = {{1,  3}, {0,  2}};
    private static final int[][] TEST_TO_STRING = {
            {2,  3,  4,  8},
            {1,  6,  0, 12},
            {5, 10,  7, 11},
            {9, 13, 14, 15}
    };
    private static final int[][] TEST_TWIN = {
            { 0,  1,  2,  3},
            { 5,  6,  7,  4},
            { 9, 10, 11,  8},
            { 13, 14, 15, 12 }
    };

    private static final int[][] TEST_IMMUTABLE = {
            { 1,  4,  3 },
            { 7,  0,  8 },
            { 6,  5,  2 }
    };

    private static void testBoard() {           // construct a board from an n-by-n array of blocks
                                                // (where blocks[i][j] = block in row i, column j)
        Board board = new Board(testBlocks1);
    }

    private static void testEquals() {        // does this board equal y?
        Board board1 = new Board(testBlocks1);
        Board board2 = new Board(testBlocks1.clone());
        Board board3 = new Board(testBlocks3);
        String description = "board1 equals board2";
        String p = board1.equals(board2) ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testTwin() {                    // a board that is obtained by exchanging any pair of blocks
        Board board1 = new Board(TEST_TWIN);
        Board board2 = board1.twin();
        String description = "board2 is twin of board1";
        String p = board1.equals(board2) ? "failed" : "passed";
        System.out.println(p + ": " + description);
        System.out.println("board1: " + board1.toString());
        System.out.println("board2: " + board2.toString());
    }

    private static void testIsGoal() {
        Board board1 = new Board(testBlocks1);
        Board board2 = new Board(testBlocks2);
        String description = "board1 is not goal";
        String p = board1.isGoal() ? "failed" : "passed";
        System.out.println(p + ": " + description);
        description = "board2 is goal";
        p = board2.isGoal() ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testHamming() {
        Board board3 = new Board(testBlocks3);
        String description = "hamming";
        String p = board3.hamming() == 5 ? "passed" : "failed";
        System.out.println(p + ": " + description);
    }

    private static void testNeighbors() {
        Board board3 = new Board(testBlocks3);
        System.out.println(board3);
        System.out.println(board3.neighbors());
        String description = "neighbors";
        String p = board3.hamming() == 5 ? "passed" : "failed";
        System.out.println(p + ": " + description);

    }

    private static void testManhattan() {
        Board board = new Board(testBlocks4);
        System.out.println(board.manhattan());
        String description = "manhattan";
        String p = board.manhattan() == 3 ? "passed" : "failed";
        System.out.println(p + ": " + description);

    }

    private static void testToString() {
        Board board = new Board(TEST_TO_STRING);
        System.out.println(board);
    }

    private static void testImmutable() {
        Board board = new Board(TEST_IMMUTABLE);
        System.out.println(board.manhattan());
        board.swapBlocks(TEST_IMMUTABLE, 0,0,1,0);
        System.out.println(board.manhattan());
    }

}
