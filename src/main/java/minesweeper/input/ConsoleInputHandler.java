package minesweeper.input;

import minesweeper.model.ActionType;
import minesweeper.model.PlayerAction;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInputHandler implements UserInputHandler {
    private final Scanner scanner;

    public ConsoleInputHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public int readInt() {
        int n;

        try {
            n = this.scanner.nextInt();
            this.scanner.nextLine();
        } catch (InputMismatchException e) {
            n = -1;
        }

        return n;
    }

    @Override
    public PlayerAction getNextAction() {
        String[] action;
        int x;
        int y;
        String cmd;

        System.out.print("Set/unset mines marks or claim a cell as free: ");
        String input = this.scanner.nextLine();

        action = input.split(" ");

        if (action.length != 3) {
            System.out.println("Error: wrong input!");
            return null;
        }

        try {
            x = Integer.parseInt(action[0]) - 1;
            y = Integer.parseInt(action[1]) - 1;
            cmd = action[2];
        } catch (NumberFormatException e) {
            System.out.println("Error: not a number!");
            return null;
        }

        return new PlayerAction(x, y, ActionType.valueOf(cmd.toUpperCase()));
    }
}
