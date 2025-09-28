package WordleGame.service;

import WordleGame.database.UserDAO;
import WordleGame.database.GameDAO;
import WordleGame.model.User;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private static final int MAX_DAILY_GAMES = 3;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
        this.gameDAO = new GameDAO();
    }

    /**
     * Validates a username based on project requirements.
     * @param username The username to validate.
     * @return true if the username is valid, false otherwise.
     */
    public boolean isValidUsername(String username) {
        // Username must be at least 5 letters (upper and lower case)
        return username != null && username.length() >= 5 && Pattern.matches("[a-zA-Z]+", username);
    }

    /**
     * Validates a password based on project requirements.
     * @param password The password to validate.
     * @return true if the password is valid, false otherwise.
     */
    public boolean isValidPassword(String password) {
        // Password must be at least 5 characters and have alpha, numeric, and one of special characters $, %, * and @
        if (password == null || password.length() < 5) {
            return false;
        }
        boolean hasAlpha = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasNumeric = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[$%*@]").matcher(password).find();

        return hasAlpha && hasNumeric && hasSpecial;
    }

    /**
     * Registers a new user.
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param userType The type of user ('PLAYER' or 'ADMIN').
     * @return true if registration is successful, false otherwise.
     */
    public boolean registerUser(String username, String password, String userType) {
        System.out.println("reg fun op sus");
        if (!isValidUsername(username)) {
            return false;
        }
        if (!isValidPassword(password)) {
            return false;
        }
        if (userDAO.doesUserExist(username)) {
            return false;
        }


        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User newUser = new User(username, hashedPassword, userType);
        System.out.println("nu op sus");
        return userDAO.createUser(newUser);
    }

    /**
     * Authenticates a user.
     * @param username The username to authenticate.
     * @param password The plain text password.
     * @return The User object if authentication is successful, otherwise null.
     */
    public User loginUser(String username, String password) {
        User user = userDAO.findUserByUsername(username);
        if (user != null) {
            if (BCrypt.checkpw(password, user.getPasswordHash())) {
                // Check daily game limit for players
                if ("PLAYER".equals(user.getUserType())) {
                    int gamesPlayed = gameDAO.getGamesPlayedToday(user.getId());
                    if (gamesPlayed >= MAX_DAILY_GAMES) {
                        return null; // Return null to indicate limit reached
                    }
                }
                return user; // Login successful
            }
        }
        return null; // Login failed
    }
}
