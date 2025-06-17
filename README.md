# 🎲 Duel Dice - A Dice Game

A simple, fun, and strategic 2-player dice game built using **Kotlin**, **Jetpack Compose**, and **MVVM architecture**. In this game, a human player competes against a computer in a race to reach or exceed a target score. The game features smart computer AI, score strategies, dice selection, and UI animations.

---

## 🚀 Features

- 🎮 2-player turn-based dice game (Human vs Computer)
- 🎯 Target-based scoring system (default: 101)
- 🎲 Dice rolling with selective holding logic
- 🧠 Smart AI decision-making strategy for the computer
- ⏳ Roll animations and delays to simulate real-time rolling
- 🧮 Score tracking and win/tie breaker logic
- 🧑‍💻 Built using MVVM architecture with `ViewModel` and `State`
- ✨ Built entirely using Jetpack Compose (no XML layouts)
- 📱 Responsive, interactive, and animated UI
- ⚠️ Dialogs for win/loss/tie-breaker situations

---

## 🧩 Game Rules

1. Each player (human and computer) rolls **5 dice**.
2. Players can choose to **re-roll** unselected dice up to **3 times per round**.
3. At the end of a round, each player's dice values are summed and added to their total score.
4. The first player to reach or exceed the **target score** (default: 101) wins.
5. If both reach the score in the same round, a **tie-breaker** occurs.
6. The computer uses different strategies based on the game state:
   - Aggressive: when behind in score
   - Conservative: when close to winning
   - Final push: when last chance to reach the target

---


## 🏗️ Architecture

- This project follows the **MVVM (Model-View-ViewModel)** architecture:

   ```
   📁 com.example.dicegame
   │
   ├── 📁 viewmodel
   │ └── GameModel.kt // Core game logic & state handling
   │
   ├── 📁 ui
   │ ├── GameScreen.kt // Main game UI
   │ └── MainActivity.kt // App launcher with navigation & dialogs
   │
   ├── 📁 resources
   │ └── drawable/ // Dice images (dice_1.png to dice_6.png)
   │
   └── build.gradle.kts // Compose & Kotlin dependencies

---

## 🛠️ Tech Stack

| Tool/Library         | Purpose                          |
|----------------------|----------------------------------|
| 🧑‍💻 Kotlin             | Programming language              |
| 🧱 Jetpack Compose    | UI framework                      |
| 🧠 ViewModel (MVVM)   | State management                  |
| ⏱️ Kotlin Coroutines  | Handling delays & async logic     |
| 🧪 JUnit, Espresso     | (Optional) UI & logic testing     |
| 🎨 Material 3         | UI design & components            |

---
