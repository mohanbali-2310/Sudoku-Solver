import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuVisualizer extends JFrame {
    private static final int GRID_SIZE = 9;
    private JTextField[][] cells = new JTextField[GRID_SIZE][GRID_SIZE];
    private int[][] board = new int[GRID_SIZE][GRID_SIZE];
    private int delay = 100;  // Default to MEDIUM speed

    public SudokuVisualizer() {
        setTitle("Sudoku Solver Visualizer");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        Border thickBorder = BorderFactory.createLineBorder(Color.BLACK, 3);
        Border thinBorder = BorderFactory.createLineBorder(Color.GRAY, 1);
        Border outerBorder = BorderFactory.createLineBorder(Color.BLUE, 3);

        // Colors for the 3x3 subgrids
        Color[] subgridColors = {new Color(230, 230, 250), new Color(240, 255, 240), new Color(255, 250, 205)};
        
        boardPanel.setBorder(outerBorder);  // Set the outer border for the entire grid

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setFont(new Font("Arial", Font.BOLD, 20));
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                
                // Determine the background color based on the subgrid
                int subgridIndex = (row / 3) * 3 + (col / 3);
                cells[row][col].setBackground(subgridColors[subgridIndex % subgridColors.length]);
                
                // Determine the border based on the cell position
                if ((row == 2 || row == 5) && (col == 2 || col == 5)) {
                    cells[row][col].setBorder(BorderFactory.createCompoundBorder(thickBorder, thinBorder));
                } else if (row == 2 || row == 5) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(3, 1, 3, 1, Color.BLACK));
                } else if (col == 2 || col == 5) {
                    cells[row][col].setBorder(BorderFactory.createMatteBorder(1, 3, 1, 3, Color.BLACK));
                } else {
                    cells[row][col].setBorder(thinBorder);
                }

                boardPanel.add(cells[row][col]);
            }
        }

        JPanel controlPanel = new JPanel(new FlowLayout());
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solveSudoku());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearBoard());
        JButton randomFillButton = new JButton("Random Fill");
        randomFillButton.addActionListener(e -> randomFill());

        JButton slowButton = new JButton("SLOW");
        slowButton.addActionListener(e -> setSpeed(300));
        JButton mediumButton = new JButton("MEDIUM");
        mediumButton.addActionListener(e -> setSpeed(100));
        JButton fastButton = new JButton("FAST");
        fastButton.addActionListener(e -> setSpeed(30));

        controlPanel.add(solveButton);
        controlPanel.add(clearButton);
        controlPanel.add(randomFillButton);
        controlPanel.add(slowButton);
        controlPanel.add(mediumButton);
        controlPanel.add(fastButton);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void solveSudoku() {
        readBoardFromGUI();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                SudokuSolver.solveBoard(board, SudokuVisualizer.this, delay);
                return null;
            }

            @Override
            protected void done() {
                updateBoard();
                JOptionPane.showMessageDialog(SudokuVisualizer.this, "Solving completed.");
            }
        }.execute();
    }

    private void clearBoard() {
        SudokuSolver.clearBoard(board);
        updateBoard();
    }

    private void randomFill() {
        clearBoard();
        SudokuSolver.randomFill(board);
        updateBoard();
    }

    private void readBoardFromGUI() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getText().isEmpty()) {
                    board[row][col] = 0;
                } else {
                    board[row][col] = Integer.parseInt(cells[row][col].getText());
                }
            }
        }
    }

    public void updateCell(int row, int col, int num) {
        SwingUtilities.invokeLater(() -> {
            if (num == 0) {
                cells[row][col].setText("");
                cells[row][col].setBackground(getSubgridColor(row, col));
            } else {
                cells[row][col].setText(String.valueOf(num));
                cells[row][col].setBackground(new Color(173, 216, 230)); // Light blue for filled cells
            }
        });
    }

    public void backtrackCell(int row, int col) {
        SwingUtilities.invokeLater(() -> {
            cells[row][col].setBackground(new Color(135, 206, 250)); // Sky blue for backtracking
        });
    }

    public void solvedCell(int row, int col, int num) {
        SwingUtilities.invokeLater(() -> {
            cells[row][col].setText(String.valueOf(num));
            cells[row][col].setBackground(new Color(144, 238, 144)); // Light green for solved cells
        });
    }

    private void updateBoard() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    cells[row][col].setText("");
                    cells[row][col].setBackground(getSubgridColor(row, col));
                } else {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    cells[row][col].setBackground(getSubgridColor(row, col));
                }
            }
        }
    }

    private void setSpeed(int newDelay) {
        this.delay = newDelay;
    }

    private Color getSubgridColor(int row, int col) {
        Color[] subgridColors = {new Color(230, 230, 250), new Color(240, 255, 240), new Color(255, 250, 205)};
        int subgridIndex = (row / 3) * 3 + (col / 3);
        return subgridColors[subgridIndex % subgridColors.length];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SudokuVisualizer visualizer = new SudokuVisualizer();
            visualizer.setVisible(true);
        });
    }
}
