# CL-Minesweeper

CL-Minesweeper is a command-line implementation of the classic Minesweeper game.

## Features

- Fixed **9x9** game board
- Customizable number of mines at the start
- Two possible actions: mark a cell (mine) or explore a cell (free)
- Automatic exploration of adjacent empty cells using a **flood fill** algorithm
- Updated board display after each action

## Game Rules

1. The game first asks how many mines should be placed on the board.
2. The player can then enter commands in the following format:
   ```
   x y cmd
   ```
   Where:
    - `x` is the column coordinate
    - `y` is the row coordinate
    - `cmd` is either `mine` or `free`

### Available Commands

- `mine`: Marks or unmarks a cell suspected of containing a mine.
- `free`: Explores a cell. Three possible outcomes:
    - If the cell contains a mine, all mines are revealed and the game is over.
    - If the cell contains a number, it is revealed.
    - If the cell is empty, all adjacent empty cells are explored recursively.

## Installation and Execution

### Prerequisites

- Java 8 or higher

### Compilation and Execution

Clone the repository and run the following command:

```sh
# On MacOS and Linux
./gradlew run --console=plain

# On Windows
gradlew run --console=plain
```

---

🚀 Have fun playing **CL-Minesweeper**!

