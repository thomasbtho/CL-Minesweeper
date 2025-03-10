package minesweeper;

import minesweeper.game.Minesweeper;
import minesweeper.input.ConsoleInputHandler;
import minesweeper.ui.ConsoleRenderer;

public class Main {
    public static void main(String[] args) {
        Minesweeper game = new Minesweeper(new ConsoleInputHandler(), new ConsoleRenderer());
        game.start();
    }
}
