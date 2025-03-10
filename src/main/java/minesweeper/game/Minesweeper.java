package minesweeper.game;

import minesweeper.model.ActionType;
import minesweeper.model.Board;
import minesweeper.model.PlayerAction;
import minesweeper.input.UserInputHandler;
import minesweeper.ui.Renderer;

public class Minesweeper {
    private Board board;
    private GameState state;

    private final UserInputHandler userInputHandler;
    private final Renderer renderer;

    public Minesweeper(UserInputHandler userInputHandler, Renderer renderer) {
        this.state = GameState.PLAYING;
        this.userInputHandler = userInputHandler;
        this.renderer = renderer;
    }

    public void start() {
        int boardWidth = -1;
        while (boardWidth < Board.MIN_WIDTH || boardWidth > Board.MAX_WIDTH) {
            System.out.printf("Width of the field [%d, %d]? ", Board.MIN_WIDTH, Board.MAX_WIDTH);
            boardWidth = userInputHandler.readInt();

            if (boardWidth < Board.MIN_WIDTH || boardWidth > Board.MAX_WIDTH) {
                System.out.println("Error: wrong width!");
            }
        }

        int boardHeight = -1;
        while (boardHeight < Board.MIN_HEIGHT || boardHeight > Board.MAX_HEIGHT) {
            System.out.printf("Height of the field [%d, %d]? ", Board.MIN_HEIGHT, Board.MAX_HEIGHT);
            boardHeight = userInputHandler.readInt();

            if (boardHeight < Board.MIN_HEIGHT || boardHeight > Board.MAX_HEIGHT) {
                System.out.println("Error: wrong height!");
            }
        }

        int mines = -1;
        int minMines = 1;
        int maxMines = (boardWidth - 1) * (boardHeight - 1);
        while (mines < minMines || mines > maxMines) {
            System.out.printf("How many mines do you want on the field [%d, %d]? ", minMines, maxMines);
            mines = userInputHandler.readInt();

            if (mines < minMines || mines > maxMines) {
                System.out.println("Error: wrong number of mines!");
            }
        }

        this.board = new Board(boardWidth, boardHeight, mines);

        this.board.generateBoard();

        while (this.state == GameState.PLAYING) {
            this.renderer.render(this.board);
            this.playTurn();
            this.checkGameState();
        }

        this.endGame();
    }

    private void playTurn() {
        PlayerAction nextAction = null;
        int x;
        int y;
        ActionType actionType;

        while (nextAction == null) {
            nextAction = this.userInputHandler.getNextAction();
            x = nextAction.x();
            y = nextAction.y();
            actionType = nextAction.actionType();

            if (x < 0 || x >= this.board.getWidth() || y < 0 || y >= this.board.getHeight()) {
                System.out.println("Error: wrong coordinates!");
                continue;
            }

            switch (actionType) {
                case FLAG -> this.board.flagCell(x, y);
                case FREE -> this.board.revealCell(x, y);
                case null, default -> System.out.println("Error: wrong command!");
            }
        }
    }

    private void checkGameState() {
        this.state = this.board.checkGameState();
    }

    private void endGame() {
        if (this.state == GameState.WON) {
            this.renderer.render(this.board);
            System.out.println("Congratulations, you won the game!");
        } else {
            this.board.revealAllCells();
            this.renderer.render(this.board);
            System.out.println("You lost!");
        }
    }
}
