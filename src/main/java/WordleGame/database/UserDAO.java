package WordleGame.database;

import WordleGame.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Inserts a new user into the database.
     * @param user The User object to create. The id will be auto-generated.
     * @return true if the user was created successfully, false otherwise.
     */
    public boolean createUser(User user) {
        String sql = "INSERT INTO users(username, password_hash, user_type) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getUserType());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return A User object if found, otherwise null.
     */
    public User findUserByUsername(String username) {
        String sql = "SELECT id, username, password_hash, user_type FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String userType = rs.getString("user_type");
                    String passwordHash = rs.getString("password_hash");
                    return new User(id, username, passwordHash, userType);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a user with the given username already exists.
     * @param username The username to check.
     * @return true if a user exists, false otherwise.
     */
    public boolean doesUserExist(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error checking if user exists: " + e.getMessage());
            return false;
        }
    }
}
