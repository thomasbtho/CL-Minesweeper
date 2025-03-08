package minesweeper;

public class Cell {
    public static final char MINE = 'X';
    public static final char UNEXPLORED_CELL = '.';
    public static final char UNEXPLORED_MARKED_CELL = '*';
    public static final char FREE_CELL = '/';

    private final int x;
    private final int y;

    private char unexploredState;
    private char exploredState;
    private boolean explored;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.unexploredState = UNEXPLORED_CELL;
        this.explored = false;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public char getUnexploredState() {
        return this.unexploredState;
    }

    public char getExploredState() {
        return this.exploredState;
    }

    public boolean isExplored() {
        return this.explored;
    }

    public void setUnexploredState(char unexploredState) {
        this.unexploredState = unexploredState;
    }

    public void setExploredState(char exploredState) {
        this.exploredState = exploredState;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }
}
