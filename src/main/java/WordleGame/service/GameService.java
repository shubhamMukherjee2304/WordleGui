package WordleGame.service;

import WordleGame.database.GameDAO;
import WordleGame.database.WordDAO;
import WordleGame.model.Game;
import WordleGame.model.Guess;
import WordleGame.service.GuessResult.ColorCode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameService {

    private final WordDAO wordDAO;
    private final GameDAO gameDAO;

    public GameService() {
        this.wordDAO = new WordDAO();
        this.gameDAO = new GameDAO();
    }

    /**
     * Starts a new game session.
     * @param userId The ID of the current player.
     * @return A Game object representing the new game session.
     */
    public Game startNewGame(int userId) {
        String randomWord = wordDAO.getRandomWord();
        if (randomWord == null) {
            System.err.println("No words available in the database to start a new game.");
            return null;
        }
        Game newGame = new Game(userId, randomWord, LocalDate.now().toString());
        int gameId = gameDAO.createGame(newGame);
        if (gameId != -1) {
            newGame.setId(gameId);
            return newGame;
        }
        return null;
    }

    /**
     * Processes a user's guess and returns the color-coded feedback.
     * @param guessWord The word guessed by the user.
     * @param targetWord The word the user is trying to guess.
     * @param gameId The ID of the current game session.
     * @param guessNumber The current guess attempt number.
     * @return A list of GuessResult objects for each letter.
     */
    public List<GuessResult> processGuess(String guessWord, String targetWord, int gameId, int guessNumber) {
        // Save the guess to the database
        Guess guess = new Guess(gameId, guessWord, guessNumber);
        gameDAO.saveGuess(guess);

        List<GuessResult> feedback = new ArrayList<>();
        char[] targetChars = targetWord.toCharArray();
        char[] guessChars = guessWord.toCharArray();

        // Use a mutable list to track used letters in the target word
        List<Character> unusedTargetLetters = targetWord.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        // First pass for GREEN (correct letter and position)
        for (int i = 0; i < guessChars.length; i++) {
            if (guessChars[i] == targetChars[i]) {
                feedback.add(new GuessResult(guessChars[i], ColorCode.GREEN));
                // Remove the used letter from the unused list
                unusedTargetLetters.remove(Character.valueOf(guessChars[i]));
            } else {
                feedback.add(null); // Placeholder for later
            }
        }

        // Second pass for ORANGE and GREY
        for (int i = 0; i < guessChars.length; i++) {
            if (feedback.get(i) == null) { // Only check for letters that weren't green
                char currentGuessChar = guessChars[i];
                if (unusedTargetLetters.contains(currentGuessChar)) {
                    feedback.set(i, new GuessResult(currentGuessChar, ColorCode.ORANGE));
                    // Remove the used letter to prevent double-counting
                    unusedTargetLetters.remove(Character.valueOf(currentGuessChar));
                } else {
                    feedback.set(i, new GuessResult(currentGuessChar, ColorCode.GREY));
                }
            }
        }
        return feedback;
    }

    /**
     * Checks if the user's guess is a winning guess.
     * @param guessWord The word guessed.
     * @param targetWord The correct word.
     * @return true if the guess is a win, false otherwise.
     */
    public boolean checkWin(String guessWord, String targetWord) {
        return guessWord.equals(targetWord);
    }

    /**
     * Ends the current game session, updating the outcome in the database.
     * @param gameId The ID of the game to end.
     * @param isWin true if the player won, false otherwise.
     */
    public void endGame(int gameId, boolean isWin) {
        gameDAO.updateGameStatus(gameId, isWin);
    }
}
