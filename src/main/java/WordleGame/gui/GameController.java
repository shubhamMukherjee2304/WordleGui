package WordleGame.gui;

import WordleGame.Main;
import WordleGame.model.Game;
import WordleGame.model.User;
import WordleGame.database.GameDAO;
import WordleGame.service.GameService;
import WordleGame.service.GuessResult;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;

public class GameController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private GridPane gameGrid;
    @FXML
    private TextField guessInput;
    @FXML
    private Label statusLabel;
    @FXML
    private Button submitButton;

    // NOTE: For this to work, you must add a Button to your FXML with:
    // fx:id="startGameButton" and onAction="#handleStartNewGame"
    // @FXML
    // private Button startGameButton;


    private final GameService gameService = new GameService();
    private final GameDAO gameDao = new GameDAO();
    private Game currentGame;
    private int currentGuessNumber = 1;
    private static final int MAX_GUESSES = 5;
    private static final int WORD_LENGTH = 5;

    @FXML
    public void initialize() {
        User user = Main.getLoggedInUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        } else {
            statusLabel.setText("User not logged in.");
        }
        setupGrid();

        // BUG FIX: Initially disable game interaction elements
        guessInput.setDisable(true);
        submitButton.setDisable(true);
        statusLabel.setText("Ready to play? Click 'Start New Game' to begin.");
    }

    private void setupGrid() {
        gameGrid.getChildren().clear();
        for (int row = 0; row < MAX_GUESSES; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                StackPane stack = new StackPane();

                Rectangle rect = new Rectangle(50, 50);
                rect.setFill(Color.web("#444444"));
                rect.setArcWidth(10);
                rect.setArcHeight(10);
                rect.setStroke(Color.web("#666666"));

                Text text = new Text("");
                text.setFont(Font.font("System", 24));
                text.setFill(Color.WHITE);

                stack.getChildren().addAll(rect, text);
                GridPane.setRowIndex(stack, row);
                GridPane.setColumnIndex(stack, col);
                gameGrid.getChildren().add(stack);
            }
        }
    }

    /**
     * Handles the action of starting a new game, linked to a button in the FXML.
     */
    @FXML
    protected void handleStartNewGame() {
        User user = Main.getLoggedInUser();
        if (user == null) {
            showAlert("Error", "User session expired. Please re-login.");
            handleLogout();
            return;
        }

        // Reset game state and UI
        currentGuessNumber = 1;
        setupGrid();
        guessInput.clear();

        // Attempt to start the game
        startGame(user);
    }


    private void startGame(User user) {
        if (gameDao.getGamesPlayedToday(user.getId()) >= 3) {
            statusLabel.setText("You have reached your daily limit of 3 games. Please try again tomorrow.");
            submitButton.setDisable(true);
            guessInput.setDisable(true);
            // startGameButton.setDisable(true); // If you add the button field
            showAlert("Game Limit Reached", "You have reached your daily limit of 3 games.");
            return;
        }

        currentGame = gameService.startNewGame(user.getId());
        if (currentGame == null) {
            statusLabel.setText("Error starting a new game.");
            submitButton.setDisable(true);
            guessInput.setDisable(true);
        } else {
            // Success: Enable interaction elements
            guessInput.setDisable(false);
            submitButton.setDisable(false);
            guessInput.requestFocus();
            statusLabel.setText("Game started! Guess the 5-letter word.");
        }
    }

    @FXML
    protected void handleSubmitGuess() {
        // Guard clause to ensure game is running
        if (currentGame == null || submitButton.isDisable()) {
            showAlert("Game Not Ready", "Please start a new game first.");
            return;
        }

        String guessWord = guessInput.getText().toUpperCase();

        if (guessWord.length() != WORD_LENGTH) {
            statusLabel.setText("Please enter a 5-letter word.");
            return;
        }

        List<GuessResult> results = gameService.processGuess(guessWord, currentGame.getTargetWord(), currentGame.getId(), currentGuessNumber);
        updateGrid(results, currentGuessNumber - 1);

        boolean isWin = gameService.checkWin(guessWord, currentGame.getTargetWord());
        if (isWin) {
            statusLabel.setText("Congratulations! You've guessed the word!");
            gameService.endGame(currentGame.getId(), true);
            endGame("You Win!", "Congratulations! You've guessed the word!");
        } else if (currentGuessNumber >= MAX_GUESSES) {
            statusLabel.setText("Better luck next time! The word was: " + currentGame.getTargetWord());
            gameService.endGame(currentGame.getId(), false);
            endGame("Game Over", "Better luck next time! The word was: " + currentGame.getTargetWord());
        } else {
            statusLabel.setText("Incorrect guess. Try again! (" + (MAX_GUESSES - currentGuessNumber) + " guesses left)");
            currentGuessNumber++;
            guessInput.clear();
        }
    }

    private void updateGrid(List<GuessResult> results, int row) {
        for (int col = 0; col < results.size(); col++) {
            GuessResult result = results.get(col);
            if (result != null) {
                StackPane stack = (StackPane) gameGrid.getChildren().get(row * WORD_LENGTH + col);
                Rectangle rect = (Rectangle) stack.getChildren().get(0);
                Text text = (Text) stack.getChildren().get(1);

                text.setText(String.valueOf(result.getLetter()));

                switch (result.getColorCode()) {
                    case GREEN:
                        rect.setFill(Color.web("#6aaa64"));
                        break;
                    case ORANGE:
                        rect.setFill(Color.web("#c9b458"));
                        break;
                    case GREY:
                        rect.setFill(Color.web("#888888"));
                        break;
                }
            }
        }
    }

    private void endGame(String title, String message) {
        submitButton.setDisable(true);
        guessInput.setDisable(true);
        // startGameButton.setDisable(false); // If you add the button field
        showAlert(title, message);
    }

    @FXML
    protected void handleLogout() {
        Main.setLoggedInUser(null);
        Main.switchScene("login_registration.fxml");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
