package minesweeper.ui;

import minesweeper.model.Board;
import minesweeper.model.Cell;

public class ConsoleRenderer implements Renderer {
    @Override
    public void render(Board board) {
        int width = board.getWidth();
        int height = board.getHeight();
        Cell[][] cells = board.getCells();

        int digits = (int) Math.floor(Math.log10(Math.max(width, height))) + 2;

        StringBuilder sb = new StringBuilder();

        sb.append(" ".repeat(digits)).append("|");
        for (int i = 0; i < width; i++) {
            sb.append(String.format("%" + digits + "d", i + 1));
        }
        sb.append("|\n");

        sb.append("-".repeat(digits)).append("|").append("-".repeat(digits).repeat(width)).append("|\n");

        for (int row = 0; row < height; row++) {
            sb.append(String.format("%" + digits + "d", row + 1)).append("|");
            for (int col = 0; col < width; col++) {
                sb.append(String.format("%" + digits + "s", this.getCellSymbol(cells[row][col])));
            }
            sb.append("|\n");
        }

        sb.append("-".repeat(digits)).append("|").append("-".repeat(digits).repeat(width)).append("|");

        System.out.println();
        System.out.println(sb);
    }

    private String getCellSymbol(Cell cell) {
        switch (cell.getState()) {
            case HIDDEN -> {
                return ".";
            }
            case FLAGGED -> {
                return "*";
            }
            case REVEALED -> {
                if (cell.isMine()) {
                    return "X";
                }
                int adjacentMines = cell.getAdjacentMines();
                if (adjacentMines > 0) {
                    return String.valueOf(adjacentMines);
                }
                return "/";
            }
            default -> {
                return "?";
            }
        }
    }
}
