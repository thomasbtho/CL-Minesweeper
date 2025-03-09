package minesweeper;

import java.util.*;

public class Minesweeper implements Runnable {
    private Cell[][] cells;
    private Cell[] mines;
    private final List<Cell> markedCells;

    private final int fieldSize;
    private int minesNumber;
    private boolean firstMove = true;

    private GameStatus gameStatus = GameStatus.PLAYING;

    public Minesweeper(int fieldSize) {
        this.fieldSize = fieldSize;
        this.markedCells = new ArrayList<>();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many mines do you want on the field? ");
        int mines = scanner.nextInt();
        scanner.nextLine();

        this.minesNumber = mines;
        this.mines = new Cell[this.minesNumber];

        this.initMinefield();
        this.printMinefield();

        String[] coordinates;
        String cmd;

        gameLoop:
        while (this.gameStatus == GameStatus.PLAYING) {
            System.out.print("Set/unset mines marks or claim a cell as free: ");
            String input = scanner.nextLine();

            coordinates = input.split(" ");
            int x;
            int y;
            try {
                x = Integer.parseInt(coordinates[0]) - 1;
                y = Integer.parseInt(coordinates[1]) - 1;
                cmd = coordinates[2];
            } catch (NumberFormatException e) {
                System.out.println("Error: wrong coordinates!");
                continue;
            }

            if (x < 0 || x >= this.fieldSize || y < 0 || y >= this.fieldSize) {
                System.out.println("Coordinates should be from 1 to " + this.fieldSize);
                continue;
            }

            switch (cmd) {
                case "mine":
                    this.mark(y, x);
                    break;
                case "free":
                    if (!this.explore(y, x)) {
                        break gameLoop;
                    }
                    break;
                default:
                    System.out.println("Error: wrong command!");
                    break;
            }
        }

        switch (this.gameStatus) {
            case WON:
                System.out.println("Congratulations! You found all the mines!");
                break;
            case LOST:
                this.printMinefield();
                System.out.println("You stepped on a mine and failed!");
                break;
            default:
                System.out.println("You won!");
                break;
        }

        scanner.close();
    }

    private void mark(int y, int x) {
        Cell cell = this.cells[y][x];

        if (cell.isExplored()) {
            if (cell.getExploredState() == Cell.FREE_CELL) {
                System.out.println("This is an explored free cell!");
            } else {
                System.out.println("There is a number here!");
            }
            return;
        }

        char unexploredState = cell.getUnexploredState();
        if (unexploredState == Cell.UNEXPLORED_CELL) {
            cell.setUnexploredState(Cell.UNEXPLORED_MARKED_CELL);
            markedCells.add(cell);
        } else if (unexploredState == Cell.UNEXPLORED_MARKED_CELL) {
            cell.setUnexploredState(Cell.UNEXPLORED_CELL);
            markedCells.remove(cell);
        }

        this.minesMarkedWinCondition();
        this.printMinefield();
    }

    private void minesMarkedWinCondition() {
        int markedMines = (int) this.markedCells.stream().filter(cell -> cell.getExploredState() == Cell.MINE).count();
        int markedSafeCells = this.markedCells.size() - markedMines;

        if (markedMines == this.minesNumber && markedSafeCells == 0) {
            this.gameStatus = GameStatus.WON;
        }
    }

    private void setMinesExplored() {
        Cell mine;
        for (int i = 0; i < this.minesNumber; i++) {
            mine = this.mines[i];
            mine.setExplored(true);
        }
    }

    private boolean explore(int y, int x) {
        if (this.firstMove) {
            this.placeMines(y, x);
            this.placeOtherCells();
            this.firstMove = false;
        }

        // If there is a mine here, the game is over
        Cell cell;
        for (int i = 0; i < this.minesNumber; i++) {
            cell = this.mines[i];
            if (cell == this.cells[y][x]) {
                this.gameStatus = GameStatus.LOST;
                setMinesExplored();
                return false;
            }
        }

        // If the cell is already explored, do nothing
        cell = this.cells[y][x];
        if (cell.isExplored()) {
            System.out.println("This cell has already been explored!");
            return true;
        }

        this.floodExplore(cell);

        this.allSafeCellsAreExploredWinCondition();
        this.printMinefield();

        return true;
    }

    /**
     * Explores the minefield starting from the given cell using a flood fill algorithm.
     * This method marks the cell as explored and, if the cell is a free cell,
     * it continues exploring unexplored neighboring free cells.
     * If the cell is marked, it will be unmarked.
     *
     * @param cell The starting cell for exploration.
     */
    private void floodExplore(Cell cell) {
        Deque<Cell> queue = new ArrayDeque<>();
        queue.push(cell);

        char exploredState;

        int cellX;
        int cellY;

        while (!queue.isEmpty()) {
            cell = queue.pop();
            cellX = cell.getX();
            cellY = cell.getY();
            exploredState = cell.getExploredState();

            cell.setExplored(true);

            // If the cell is marked, unmark it
            if (cell.getUnexploredState() == Cell.UNEXPLORED_MARKED_CELL) {
                cell.setUnexploredState(Cell.UNEXPLORED_CELL);
                this.markedCells.remove(cell);
            }

            // If this is a number cell, do nothing
            if (exploredState != Cell.FREE_CELL) {
                continue;
            }

            // If this is a free cell, add its unexplored neighbors to the queue
            int[] directions = get4Directions(cellY, cellX);
            int up = directions[0];
            int down = directions[1];
            int left = directions[2];
            int right = directions[3];

            boolean canGoUp = up >= 0;
            boolean canGoDown = down < this.fieldSize;
            boolean canGoLeft = left >= 0;
            boolean canGoRight = right < this.fieldSize;

            // top left
            if (canGoUp && canGoLeft && !this.cells[up][left].isExplored()) {
                queue.push(this.cells[up][left]);
            }

            // top
            if (canGoUp && !this.cells[up][cellX].isExplored()) {
                queue.push(this.cells[up][cellX]);
            }

            // top right
            if (canGoUp && canGoRight && !this.cells[up][right].isExplored()) {
                queue.push(this.cells[up][right]);
            }

            // right
            if (canGoRight && !this.cells[cellY][right].isExplored()) {
                queue.push(this.cells[cellY][right]);
            }

            // bottom right
            if (canGoDown && canGoRight && !this.cells[down][right].isExplored()) {
                queue.push(this.cells[down][right]);
            }

            // bottom
            if (canGoDown && !this.cells[down][cellX].isExplored()) {
                queue.push(this.cells[down][cellX]);
            }

            // bottom left
            if (canGoDown && canGoLeft && !this.cells[down][left].isExplored()) {
                queue.push(this.cells[down][left]);
            }

            // left
            if (canGoLeft && !this.cells[cellY][left].isExplored()) {
                queue.push(this.cells[cellY][left]);
            }
        }
    }

    private void allSafeCellsAreExploredWinCondition() {
        int exploredFreeCells = 0;

        for (int i = 0; i < this.fieldSize; i++) {
            for (int j = 0; j < this.fieldSize; j++) {
                if (this.cells[i][j].isExplored()) {
                    exploredFreeCells++;
                }
            }
        }

        if (exploredFreeCells == this.fieldSize * this.fieldSize - this.minesNumber) {
            this.gameStatus = GameStatus.WON;
        }
    }

    private void initMinefield() {
        this.cells = new Cell[this.fieldSize][this.fieldSize];

        for (int i = 0; i < this.fieldSize; i++) {
            for (int j = 0; j < this.fieldSize; j++) {
                this.cells[i][j] = new Cell(j, i);
            }
        }
    }

    private void placeMines(int y, int x) {
        Random random = new Random();
        Cell mine = null;

        int numberOfMines = 0;
        int i;
        int j;

        while (numberOfMines < this.minesNumber) {
            i = random.nextInt(this.fieldSize);
            j = random.nextInt(this.fieldSize);

            if (i == y && j == x) {
                continue;
            }

            for (int k = 0; k < this.minesNumber; k++) {
                mine = this.mines[k];
                if (mine == this.cells[i][j]) {
                    break;
                }
            }

            if (mine == null) {
                this.mines[numberOfMines++] = this.cells[i][j];
            }
        }
    }

    private void placeOtherCells() {
        Cell mine = null;
        for (int i = 0; i < this.fieldSize; i++) {
            for (int j = 0; j < this.fieldSize; j++) {
                for (int k = 0; k < this.minesNumber; k++) {
                    mine = this.mines[k];
                    if (mine == this.cells[i][j]) {
                        mine.setExploredState(Cell.MINE);
                        break;
                    }
                    mine = null;
                }

                if (mine == null) {
                    int mines = minesAround(i, j);
                    if (mines == 0) {
                        this.cells[i][j].setExploredState(Cell.FREE_CELL);
                    } else {
                        this.cells[i][j].setExploredState(Character.forDigit(mines, 10));
                    }
                }
            }
        }
    }

    /**
     * Get the coordinates of four directions (up, down, left, right)
     * from a given cell.
     *
     * @param i the row index of the cell
     * @param j the column index of the cell
     * @return an array of four integers, where the first element is the row index
     * of the cell above, the second element is the row index of the cell below,
     * the third element is the column index of the cell to the left, and the
     * fourth element is the column index of the cell to the right.
     */
    private int[] get4Directions(int i, int j) {
        int[] directions = new int[4];
        directions[0] = i - 1; // up
        directions[1] = i + 1; // down
        directions[2] = j - 1; // left
        directions[3] = j + 1; // right
        return directions;
    }

    private int minesAround(int i, int j) {
        int mines = 0;

        int[] directions = get4Directions(i, j);

        int up = directions[0];
        int down = directions[1];
        int left = directions[2];
        int right = directions[3];

        boolean canGoUp = up >= 0;
        boolean canGoDown = down < this.fieldSize;
        boolean canGoLeft = left >= 0;
        boolean canGoRight = right < this.fieldSize;

        Cell mine;

        for (int k = 0; k < this.minesNumber; k++) {
            mine = this.mines[k];

            // top left
            if (canGoUp && canGoLeft && this.cells[up][left] == mine) {
                mines++;
            }

            // top
            if (canGoUp && this.cells[up][j] == mine) {
                mines++;
            }

            // top right
            if (canGoUp && canGoRight && this.cells[up][right] == mine) {
                mines++;
            }

            // right
            if (canGoRight && this.cells[i][right] == mine) {
                mines++;
            }

            // bottom right
            if (canGoDown && canGoRight && this.cells[down][right] == mine) {
                mines++;
            }

            // bottom
            if (canGoDown && this.cells[down][j] == mine) {
                mines++;
            }

            // bottom left
            if (canGoDown && canGoLeft && this.cells[down][left] == mine) {
                mines++;
            }

            // left
            if (canGoLeft && this.cells[i][left] == mine) {
                mines++;
            }
        }

        return mines;
    }

    public void printMinefield() {
        StringBuilder sb = new StringBuilder();

        sb.append(" |");
        for (int i = 0; i < this.fieldSize; i++) {
            sb.append(i + 1);
        }
        sb.append("|\n");

        sb.append("-|").append("-".repeat(this.fieldSize)).append("|\n");

        Cell cell;
        for (int i = 0; i < this.cells.length; i++) {
            sb.append(i + 1).append("|");
            for (int j = 0; j < this.cells[i].length; j++) {
                cell = this.cells[i][j];
                if (cell.isExplored()) {
                    sb.append(cell.getExploredState());
                } else {
                    sb.append(cell.getUnexploredState());
                }
            }
            sb.append("|\n");
        }

        sb.append("-|").append("-".repeat(this.fieldSize)).append("|");

        System.out.println();
        System.out.println(sb);
    }
}
