package WordleGame.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class WordDAO {

    /**
     * Inserts a list of 5-letter words into the database if the table is empty.
     * This method is intended for one-time initialization.
     * @param words A list of String words to insert.
     */
    public void insertInitialWords(List<String> words) {
        String countSql = "SELECT COUNT(*) FROM words";
        String insertSql = "INSERT INTO words(word) VALUES(?)";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Words table is already populated. Skipping insertion.");
                return;
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                conn.setAutoCommit(false); // Start transaction for efficiency
                for (String word : words) {
                    pstmt.setString(1, word);
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                conn.commit(); // Commit transaction
                System.out.println("Initial words inserted successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting initial words: " + e.getMessage());
        }
    }

    /**
     * Retrieves a single random 5-letter word from the database.
     * @return A random word as a String, or null if no words are found.
     */
    public String getRandomWord() {
        String sql = "SELECT word FROM words ORDER BY RANDOM() LIMIT 1";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getString("word");
            }
        } catch (SQLException e) {
            System.err.println("Error getting random word: " + e.getMessage());
        }
        return null;
    }
}
