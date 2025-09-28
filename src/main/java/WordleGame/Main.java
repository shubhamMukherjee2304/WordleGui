package WordleGame;

import WordleGame.database.DatabaseManager;
import WordleGame.database.WordDAO;
import WordleGame.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    private static Stage primaryStage;
    private static User loggedInUser;

    // A temporary list of words to populate the database
    private static final List<String> INITIAL_WORDS = Arrays.asList(
            "APPLE", "BRAIN", "CRANE", "DRIVE", "EAGLE",
            "FIGHT", "GRAPE", "HOUSE", "IDEAL", "JOINT",
            "KITE", "LIGHT", "MOUNT", "NIGHT", "ORION",
            "PIZZA", "QUIET", "RIVER", "SMILE", "TRAIN"
    );

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        DatabaseManager.initializeDatabase();

        // Populate the words table on first run
        WordDAO wordDAO = new WordDAO();
        wordDAO.insertInitialWords(INITIAL_WORDS);

        switchScene("login_registration.fxml");
        stage.setTitle("Word Guess Game");
        stage.setResizable(false);
        stage.show();
    }

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/WordleGame/gui/" + fxmlFile));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + fxmlFile);
            e.printStackTrace();
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
        if (loggedInUser != null) {
            if ("ADMIN".equals(loggedInUser.getUserType())) {
                switchScene("AdminReportUI.fxml");
            } else {
                switchScene("GameSceneUI.fxml");
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
