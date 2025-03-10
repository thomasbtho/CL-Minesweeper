package minesweeper.model;

public class Cell {
    private final int x;
    private final int y;

    private boolean isMine;
    private int adjacentMines;
    private CellState state;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.isMine = false;
        this.adjacentMines = 0;
        this.state = CellState.HIDDEN;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isMine() {
        return this.isMine;
    }

    public int getAdjacentMines() {
        return this.adjacentMines;
    }

    public CellState getState() {
        return this.state;
    }

    public boolean isRevealed() {
        return this.state == CellState.REVEALED;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public void reveal(boolean forceReveal) {
        if (this.state == CellState.HIDDEN || forceReveal) {
            this.state = CellState.REVEALED;
        }
    }

    public void toggleFlag() {
        if (this.state == CellState.HIDDEN) {
            this.state = CellState.FLAGGED;
        } else if (this.state == CellState.FLAGGED) {
            this.state = CellState.HIDDEN;
        }
    }
}
