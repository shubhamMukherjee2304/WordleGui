package WordleGame;

import WordleGame.database.DatabaseManager;
import WordleGame.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;
    private static User loggedInUser;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        DatabaseManager.initializeDatabase();

        // This is where we would also populate the initial words
        // WordDAO wordDAO = new WordDAO();
        // wordDAO.insertInitialWords(initialWordsList);

        // Load the initial login/registration scene
        switchScene("login_registration.fxml");
        primaryStage.setTitle("Word Guess Game");
        primaryStage.show();
    }

    // Static method to switch scenes from any controller
    public static void switchScene(String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/WordleGame/gui/" + fxmlFileName));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + fxmlFileName);
            e.printStackTrace();
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
