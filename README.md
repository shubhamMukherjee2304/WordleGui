# 🎮 WobblyWordle

## 1. Project Overview

**WobblyWordle** is a complete, desktop-based Word Guess Game application developed using **JavaFX**.
It follows a multi-tier architecture, separating the **database**, **business logic**, and **user interface**.

The application features two distinct user roles:

* **Player** → for gameplay
* **Admin** → for reporting and statistics

The core game challenges players to guess a randomly selected five-letter word in **five attempts**, providing **color-coded feedback** after each guess: 🟩 🟧 ⬜

---

## 2. Core Features

### 👤 Player Features

*  **Secure Authentication**:

  * Username: minimum **5 letters**
  * Password: minimum **5 characters**, must include **alphabetic, numeric, and one special character** (`$, %, *, @`)
*  **Daily Limit**: Players can play a maximum of **3 games per day**.
*  **Game Loop**: Guess a random **5-letter word** stored in the database.
*  **Color-Coded Feedback**:

  * 🟩 Green → Correct letter in the correct position
  * 🟧 Orange → Correct letter but in the wrong position
  * ⬜ Grey → Letter not in the target word
* 💾 **Game Persistence**: All games, guesses, and outcomes (win/loss) are saved to the database.

###  Admin Features

*  **Role-Based Access** → Admins have their own dashboard.
* **Daily Report**:

  * Total number of unique users who played
  * Total number of correct guesses (wins) for any given day
*  **User Report**:

  * Detailed history for a selected user, including:

    * Game date
    * Target word
    * Number of guesses made
    * Game outcome (win/loss)

---

## 3. Technology Stack

| **Component** | **Technology** | **Description**                                     |
| ------------- | -------------- | --------------------------------------------------- |
| GUI           | JavaFX         | Modern Java framework for cross-platform UI         |
| Backend/Logic | Java 17+       | Core programming language for business & game logic |
| Database      | SQLite         | Lightweight, file-based relational database         |
| Build Tool    | Apache Maven   | Dependency management & build system                |
| Security      | JBCrypt        | Secure password hashing & verification              |

---

## 4. Setup and Installation

###  Prerequisites

*  **JDK**: Version **17 or higher**
*  **Maven**: Installed and added to PATH
*  **IDE**: IntelliJ IDEA or VS Code with Java/JavaFX extensions

###  Project Configuration

1. **Clone the Repository**

   ```bash
   git clone https://github.com/shubhamMukherjee2304/WordleGui
   cd WordleGui
   ```

2. **Verify Dependencies** in `pom.xml` (important ones):

   * `org.openjfx:javafx-* (controls, fxml)`
   * `org.xerial:sqlite-jdbc`
   * `org.mindrot:jbcrypt`

3. **Build the Project**

   ```bash
   mvn clean install
   
   ```

---

## 5. Running the Application

*  **From Terminal**: mvn javafx:run

---

## 6. Usage Guide

###  Initial Setup (word_game.db)

* On first run, `DatabaseManager` automatically creates `word_game.db`.
* `WordDAO` inserts **20 initial five-letter words** into the `words` table.

###  Login & Registration

* First screen → **Login / Register**
* Registration:

  * Choose role: `"YES"` = Admin, `"NO"` = Player
  * Must meet username/password validation rules
* Login:

  * Admin → Reporting Dashboard
  * Player → Game Screen

###  Player Gameplay Flow

1. **Welcome Screen** → Inputs disabled
2. **Start Game** → Click *Start New Game*

   * System checks **daily limit (3 games max)**
   * If allowed → Random word selected & inputs enabled
3. **Guessing** → Enter a **5-letter word** → Click *Submit Guess*
4. **Game End**:

   *  **Win** → Word guessed → Congratulations 🎉
   *  **Loss** → 5 attempts over → Word revealed
5. **Logout** → Return to Login Screen

---
