package WordleGame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:word_game.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL)) {
            if (conn != null) {
                System.out.println("Database connection established.");
                createTables(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) {
        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT NOT NULL UNIQUE,"
                + "password_hash TEXT NOT NULL,"
                + "user_type TEXT NOT NULL"
                + ");";

        String createWordsTableSQL = "CREATE TABLE IF NOT EXISTS words ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "word TEXT NOT NULL UNIQUE"
                + ");";

        String createGamesTableSQL = "CREATE TABLE IF NOT EXISTS games ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER NOT NULL,"
                + "target_word TEXT NOT NULL,"
                + "game_date TEXT NOT NULL,"
                + "is_win INTEGER NOT NULL,"
                + "FOREIGN KEY (user_id) REFERENCES users(id)"
                + ");";

        String createGuessesTableSQL = "CREATE TABLE IF NOT EXISTS guesses ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "game_id INTEGER NOT NULL,"
                + "guess_word TEXT NOT NULL,"
                + "guess_number INTEGER NOT NULL,"
                + "FOREIGN KEY (game_id) REFERENCES games(id)"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createUsersTableSQL);
            stmt.execute(createWordsTableSQL);
            stmt.execute(createGamesTableSQL);
            stmt.execute(createGuessesTableSQL);
            System.out.println("All tables created or already exist.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}
