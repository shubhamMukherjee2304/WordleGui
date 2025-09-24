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
                Main.switchScene("game_scene.fxml");
            } else if ("ADMIN".equals(user.getUserType())) {
                Main.switchScene("admin_report.fxml");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password, or daily game limit reached.");
        }
    }

    @FXML
    protected void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!authService.isValidUsername(username)) {
            showAlert("Registration Failed", "Username must be at least 5 letters (a-z, A-Z).");
            return;
        }
        if (!authService.isValidPassword(password)) {
            showAlert("Registration Failed", "Password must be at least 5 characters and contain an alpha, a numeric, and one of $, %, *, @.");
            return;
        }

        // For simplicity, we'll ask the user if they are an admin or player
        Optional<ButtonType> result = showRoleSelectionAlert();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            if (authService.registerUser(username, password, "ADMIN")) {
                showAlert("Registration Successful", "Admin user created successfully! You can now log in.");
            } else {
                showAlert("Registration Failed", "Username is already taken or an error occurred.");
            }
        } else if (result.isPresent() && result.get() == ButtonType.NO) {
            if (authService.registerUser(username, password, "PLAYER")) {
                showAlert("Registration Successful", "Player user created successfully! You can now log in.");
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

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        return alert.showAndWait();
    }
}
