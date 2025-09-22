package WordleGame.database;

import WordleGame.model.Game;
import WordleGame.model.Guess;
import WordleGame.model.UserReport; // We will create this POJO later
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {

    /**
     * Creates a new game session in the 'games' table.
     * @param game The Game object to save.
     * @return The ID of the newly created game session, or -1 if an error occurs.
     */
    public int createGame(Game game) {
        String sql = "INSERT INTO games(user_id, target_word, game_date, is_win) VALUES(?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, game.getUserId());
            pstmt.setString(2, game.getTargetWord());
            pstmt.setString(3, game.getGameDate());
            pstmt.setInt(4, game.isWin() ? 1 : 0);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating game session: " + e.getMessage());
        }
        return generatedId;
    }

    /**
     * Saves a single guess made by the user to the 'guesses' table.
     * @param guess The Guess object to save.
     */
    public void saveGuess(Guess guess) {
        String sql = "INSERT INTO guesses(game_id, guess_word, guess_number) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, guess.getGameId());
            pstmt.setString(2, guess.getGuessWord());
            pstmt.setInt(3, guess.getGuessNumber());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving guess: " + e.getMessage());
        }
    }

    /**
     * Counts the number of games played by a user on the current day.
     * This is used to enforce the daily limit of 3 games.
     * @param userId The ID of the user.
     * @return The number of games played today.
     */
    public int getGamesPlayedToday(int userId) {
        String sql = "SELECT COUNT(*) FROM games WHERE user_id = ? AND game_date = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, LocalDate.now().toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting games played today: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Retrieves the daily report for the admin.
     * @param date The date for which to retrieve the report (format 'YYYY-MM-DD').
     * @return A list containing the number of unique users and the number of correct guesses.
     */
    public List<Integer> getDailyReport(String date) {
        String sql = "SELECT COUNT(DISTINCT user_id), SUM(is_win) FROM games WHERE game_date = ?";
        List<Integer> reportData = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, date);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    reportData.add(rs.getInt(1)); // Number of unique users
                    reportData.add(rs.getInt(2)); // Number of correct guesses
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting daily report: " + e.getMessage());
        }
        return reportData;
    }

    /**
     * Retrieves a report of all games played by a specific user.
     * This requires a JOIN operation to count the number of guesses per game.
     * @param userId The ID of the user.
     * @return A list of UserReport objects.
     */
    public List<UserReport> getUserReport(int userId) {
        String sql = "SELECT T1.target_word, T1.game_date, T1.is_win, COUNT(T2.id) AS num_guesses "
                + "FROM games AS T1 INNER JOIN guesses AS T2 ON T1.id = T2.game_id "
                + "WHERE T1.user_id = ? GROUP BY T1.id ORDER BY T1.game_date DESC";

        List<UserReport> reports = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String targetWord = rs.getString("target_word");
                    String gameDate = rs.getString("game_date");
                    boolean isWin = rs.getInt("is_win") == 1;
                    int numGuesses = rs.getInt("num_guesses");
                    reports.add(new UserReport(targetWord, gameDate, isWin, numGuesses));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user report: " + e.getMessage());
        }
        return reports;
    }
}