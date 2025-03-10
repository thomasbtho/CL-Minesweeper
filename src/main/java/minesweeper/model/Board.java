package minesweeper.model;

import minesweeper.game.GameState;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class Board {
    public final static int MIN_WIDTH = 8;
    public final static int MAX_WIDTH = 30;
    public final static int MIN_HEIGHT = 8;
    public final static int MAX_HEIGHT = 24;

    private final int width;
    private final int height;
    private final int numberOfMines;
    private Cell[][] cells;

    public Board(int width, int height, int numberOfMines) {
        this.width = width;
        this.height = height;
        this.numberOfMines = numberOfMines;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Cell[][] getCells() {
        return this.cells;
    }

    public GameState checkGameState() {
        int maxPossibleFreeCells = this.width * this.height - this.numberOfMines;
        int freeCells = 0;

        Cell cell;
        CellState cellState;

        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                cell = this.cells[row][col];
                cellState = cell.getState();

                if (cellState == CellState.REVEALED) {
                    if (cell.isMine()) {
                        return GameState.LOST;
                    }
                    freeCells++;
                }
            }
        }

        if (freeCells == maxPossibleFreeCells) {
            return GameState.WON;
        }

        return GameState.PLAYING;
    }

    public void revealAllCells() {
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                this.cells[row][col].reveal(true);
            }
        }
    }

    public void generateBoard() {
        this.initCells();
        this.placeMines();
        this.calculateAdjacentNumbers();
    }

    public void flagCell(int x, int y) {
        this.cells[y][x].toggleFlag();
    }

    public void revealCell(int x, int y) {
        Cell cell = this.cells[y][x];
        CellState cellState = cell.getState();

        // You can't reveal a flagged cell
        if (cellState == CellState.FLAGGED) {
            System.out.println("This cell is flagged!");
            return;
        }

        cell.reveal(false);

        if (cell.isMine()) {
            return;
        }

        Deque<Cell> stack = new ArrayDeque<>();
        stack.push(cell);

        int cellX;
        int cellY;

        while (!stack.isEmpty()) {
            cell = stack.pop();
            cellX = cell.getX();
            cellY = cell.getY();

            // If this is a number cell, do nothing
            if (cell.getAdjacentMines() > 0) {
                continue;
            }

            // If this is a free cell, push its unrevealed neighbors onto the stack
            int[] directions = get4Directions(cellX, cellY);
            int up = directions[0];
            int down = directions[1];
            int left = directions[2];
            int right = directions[3];

            boolean canGoUp = up >= 0;
            boolean canGoDown = down < this.height;
            boolean canGoLeft = left >= 0;
            boolean canGoRight = right < this.width;

            // top left
            if (canGoUp && canGoLeft && !this.cells[up][left].isRevealed()) {
                this.cells[up][left].reveal(false);
                stack.push(this.cells[up][left]);
            }

            // top
            if (canGoUp && !this.cells[up][cellX].isRevealed()) {
                this.cells[up][cellX].reveal(false);
                stack.push(this.cells[up][cellX]);
            }

            // top right
            if (canGoUp && canGoRight && !this.cells[up][right].isRevealed()) {
                this.cells[up][right].reveal(false);
                stack.push(this.cells[up][right]);
            }

            // right
            if (canGoRight && !this.cells[cellY][right].isRevealed()) {
                this.cells[cellY][right].reveal(false);
                stack.push(this.cells[cellY][right]);
            }

            // bottom right
            if (canGoDown && canGoRight && !this.cells[down][right].isRevealed()) {
                this.cells[down][right].reveal(false);
                stack.push(this.cells[down][right]);
            }

            // bottom
            if (canGoDown && !this.cells[down][cellX].isRevealed()) {
                this.cells[down][cellX].reveal(false);
                stack.push(this.cells[down][cellX]);
            }

            // bottom left
            if (canGoDown && canGoLeft && !this.cells[down][left].isRevealed()) {
                this.cells[down][left].reveal(false);
                stack.push(this.cells[down][left]);
            }

            // left
            if (canGoLeft && !this.cells[cellY][left].isRevealed()) {
                this.cells[cellY][left].reveal(false);
                stack.push(this.cells[cellY][left]);
            }
        }
    }

    private void initCells() {
        this.cells = new Cell[this.height][this.width];
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                this.cells[row][col] = new Cell(col, row);
            }
        }
    }

    private void placeMines() {
        Random random = new Random();

        int mines = 0;
        int col;
        int row;

        while (mines < this.numberOfMines) {
            col = random.nextInt(this.width);
            row = random.nextInt(this.height);

            if (!this.cells[row][col].isMine()) {
                this.cells[row][col].setMine(true);
                mines++;
            }
        }
    }

    private void calculateAdjacentNumbers() {
        Cell cell;
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                cell = this.cells[row][col];
                if (!cell.isMine()) {
                    cell.setAdjacentMines(this.minesAround(cell));
                }
            }
        }
    }

    private int minesAround(Cell cell) {
        int mines = 0;

        int x = cell.getX();
        int y = cell.getY();

        int[] directions = get4Directions(x, y);

        int up = directions[0];
        int down = directions[1];
        int left = directions[2];
        int right = directions[3];

        boolean canGoUp = up >= 0;
        boolean canGoDown = down < this.height;
        boolean canGoLeft = left >= 0;
        boolean canGoRight = right < this.width;

        // top left
        if (canGoUp && canGoLeft && this.cells[up][left].isMine()) {
            mines++;
        }

        // top
        if (canGoUp && this.cells[up][x].isMine()) {
            mines++;
        }

        // top right
        if (canGoUp && canGoRight && this.cells[up][right].isMine()) {
            mines++;
        }

        // right
        if (canGoRight && this.cells[y][right].isMine()) {
            mines++;
        }

        // bottom right
        if (canGoDown && canGoRight && this.cells[down][right].isMine()) {
            mines++;
        }

        // bottom
        if (canGoDown && this.cells[down][x].isMine()) {
            mines++;
        }

        // bottom left
        if (canGoDown && canGoLeft && this.cells[down][left].isMine()) {
            mines++;
        }

        // left
        if (canGoLeft && this.cells[y][left].isMine()) {
            mines++;
        }

        return mines;
    }

    /**
     * Get the coordinates of four directions (up, down, left, right)
     * from a given cell.
     *
     * @param x the column index of the cell
     * @param y the row index of the cell
     * @return an array of four integers, where the first element is the row index
     * of the cell above, the second element is the row index of the cell below,
     * the third element is the column index of the cell to the left, and the
     * fourth element is the column index of the cell to the right.
     */
    private int[] get4Directions(int x, int y) {
        int[] directions = new int[4];
        directions[0] = y - 1; // up
        directions[1] = y + 1; // down
        directions[2] = x - 1; // left
        directions[3] = x + 1; // right
        return directions;
    }
}
