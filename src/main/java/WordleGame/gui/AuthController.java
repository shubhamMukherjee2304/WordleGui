package WordleGame.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class AuthController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    @FXML
    protected void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // TODO: Implement input validation

        // TODO: Call AuthService for login logic

        // TODO: Redirect based on user type (Player or Admin)

        statusLabel.setText("Login attempt...");
    }

    @FXML
    protected void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // TODO: Implement input validation

        // TODO: Call AuthService for registration logic

        statusLabel.setText("Registration attempt...");
    }

    // Helper method to show messages to the user
    private void showStatus(String message) {
        statusLabel.setText(message);
    }

    // Helper method to navigate to different scenes
    private void navigateTo(String fxmlFile) throws IOException {
        // TODO: Implement view switching logic here
        // This will load a new FXML file and set it as the scene
    }
}
