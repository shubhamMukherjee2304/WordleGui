package WordleGame.gui;



import WordleGame.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import WordleGame.model.User;

public class GameController {

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User user = Main.getLoggedInUser();
        if (user != null) {
            welcomeLabel.setText("Welcome, " + user.getUsername() + "!");
        } else {
            welcomeLabel.setText("Welcome!");
        }
    }

    // TODO: Add game logic here

    @FXML
    protected void handleLogout() {
        // TODO: Implement logout logic
    }
}

