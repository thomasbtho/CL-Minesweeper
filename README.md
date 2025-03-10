# 🏴‍☠️ Command-Line Minesweeper

A classic **Minesweeper** game playable directly in the **command line**, built in **Java 21**.

## 🎯 Features

✔ Classic Minesweeper mechanics (flagging, revealing cells, checking for mines)  
✔ Randomly generated minefield with adjustable grid size and mine count  
✔ Fully interactive CLI with simple text input  
✔ Clear and aligned board display for better readability  
✔ Win and loss detection

## 🛠 Installation & Setup

### 1️⃣ Prerequisites

- **Java 21** (or later) installed
- A terminal supporting standard input/output

### 2️⃣ Clone the Repository

```sh
git clone https://github.com/thomasbtho/CL-Minesweeper.git
cd CL-Minesweeper
```

### 3️⃣ Run the Game

```sh
# On MacOS and Linux
./gradlew run --console=plain

# On Windows
gradlew run --console=plain
```

## 🎮 How to Play

1️⃣ Enter **three values**: `x y action`

- `x` and `y` are the **coordinates** (starting from 1).
- `action` can be:
    - `free` → Uncover the cell
    - `flag` → Mark a possible mine

2️⃣ Example input:

```sh
3 5 free
```

3️⃣ **Win condition**: Reveal all non-mine cells.  
4️⃣ **Lose condition**: Reveal a mine.

---
**Author:** [thomasbtho](https://github.com/thomasbtho)  
🎮 Happy mining! 🏴‍☠️
