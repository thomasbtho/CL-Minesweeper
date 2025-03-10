package minesweeper.input;

import minesweeper.model.PlayerAction;

public interface UserInputHandler {
    int readInt();

    PlayerAction getNextAction();
}
