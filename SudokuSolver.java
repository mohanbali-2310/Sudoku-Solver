import java.util.Random;

public class SudokuSolver {
    private static final int GRID_SIZE = 9;

    public static boolean solveBoard(int[][] board, SudokuVisualizer visualizer, int delay) throws InterruptedException {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= GRID_SIZE; num++) {
                        if (isValidPlacement(board, num, row, col)) {
                            board[row][col] = num;
                            visualizer.updateCell(row, col, num);  // Update the cell in the GUI
                            Thread.sleep(delay);  // Delay for animation

                            if (solveBoard(board, visualizer, delay)) {
                                visualizer.solvedCell(row, col, num);  // Mark cell as part of the solution
                                return true;
                            } else {
                                board[row][col] = 0;
                                visualizer.backtrackCell(row, col);  // Mark cell as backtracked
                                Thread.sleep(delay);  // Delay for animation
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isValidPlacement(int[][] board, int num, int row, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        int localBoxRow = row - row % 3;
        int localBoxCol = col - col % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxCol; j < localBoxCol + 3; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void randomFill(int[][] board) {
        Random rand = new Random();
        for (int i = 0; i < 15; i++) {
            int row = rand.nextInt(GRID_SIZE);
            int col = rand.nextInt(GRID_SIZE);
            int num = rand.nextInt(GRID_SIZE) + 1;
            if (board[row][col] == 0 && isValidPlacement(board, num, row, col)) {
                board[row][col] = num;
            }
        }
    }

    public static void clearBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col] = 0;
            }
        }
    }

    public static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("-----------");
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print("|");
                }
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
