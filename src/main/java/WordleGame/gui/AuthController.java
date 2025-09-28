package WordleGame.gui;

import WordleGame.Main;
import WordleGame.model.User;
import WordleGame.service.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AuthController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private final AuthService authService = new AuthService();

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter a username and password.");
            return;
        }

        User user = authService.loginUser(username, password);

        if (user != null) {
            Main.setLoggedInUser(user);
            System.out.println("Login successful for user: " + user.getUsername());

            // Switch to the appropriate view based on user type
            if ("PLAYER".equals(user.getUserType())) {
                Main.switchScene("GameSceneUI.fxml");
            } else if ("ADMIN".equals(user.getUserType())) {
                Main.switchScene("AdminReportUI.fxml");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password, or daily game limit reached.");
        }
    }

    @FXML
    protected void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. Validate username and password against rules
        if (!authService.isValidUsername(username)) {
            showAlert("Registration Failed", "Username must be at least 5 letters (a-z, A-Z).");
            return;
        }
        if (!authService.isValidPassword(password)) {
            showAlert("Registration Failed", "Password must be at least 5 characters and contain an alpha, a numeric, and one of $, %, *, @.");
            return;
        }

        // 2. Ask user to select their role (Admin or Player)
        Optional<ButtonType> result = showRoleSelectionAlert();

        if (result.isPresent()) {
            String userType = "PLAYER"; // Default to PLAYER
            if (result.get().getText().equals("YES")) {
                userType = "ADMIN";
            }

            // 3. Attempt to register the user with the selected role
            boolean newUser = authService.registerUser(username, password, userType);
            if (newUser) {
                showAlert("Registration Successful", "User '" + username + "' created successfully! You can now log in.");
            } else {
                showAlert("Registration Failed", "Username is already taken or an error occurred.");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Optional<ButtonType> showRoleSelectionAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Type Selection");
        alert.setHeaderText("Do you want to register as an ADMIN?");
        alert.setContentText("Choose 'Yes' for Admin or 'No' for Player.");

        ButtonType yesButton = new ButtonType("YES");
        ButtonType noButton = new ButtonType("NO");

        alert.getButtonTypes().setAll(yesButton, noButton);

        return alert.showAndWait();
    }
}
