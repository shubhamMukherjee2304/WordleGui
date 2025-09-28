package WordleGame.gui;



import WordleGame.Main;
import WordleGame.database.GameDAO;
import WordleGame.model.UserReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class AdminController {

    @FXML
    private DatePicker datePicker;
    @FXML
    private Label dailyReportLabel;
    @FXML
    private TextField userIdInput;
    @FXML
    private TableView<UserReport> userReportTable;
    @FXML
    private TableColumn<UserReport, String> targetWordColumn;
    @FXML
    private TableColumn<UserReport, String> gameDateColumn;
    @FXML
    private TableColumn<UserReport, Boolean> isWinColumn;
    @FXML
    private TableColumn<UserReport, Integer> numGuessesColumn;

    private final GameDAO gameDAO = new GameDAO();

    @FXML
    public void initialize() {
        // Set up the columns in the TableView to map to the UserReport POJO properties
        targetWordColumn.setCellValueFactory(new PropertyValueFactory<>("targetWord"));
        gameDateColumn.setCellValueFactory(new PropertyValueFactory<>("gameDate"));
        isWinColumn.setCellValueFactory(new PropertyValueFactory<>("isWin"));
        numGuessesColumn.setCellValueFactory(new PropertyValueFactory<>("numGuesses"));

        // Pre-fill the DatePicker with the current date for convenience
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    protected void handleDailyReport() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            dailyReportLabel.setText("Please select a date.");
            return;
        }

        List<Integer> reportData = gameDAO.getDailyReport(selectedDate.toString());
        int uniqueUsers = reportData.isEmpty() ? 0 : reportData.get(0);
        int correctGuesses = reportData.isEmpty() ? 0 : reportData.get(1);

        dailyReportLabel.setText(String.format("On %s, there were %d unique players and %d correct guesses.", selectedDate.toString(), uniqueUsers, correctGuesses));
    }

    @FXML
    protected void handleUserReport() {
        try {
            int userId = Integer.parseInt(userIdInput.getText());
            List<UserReport> userReports = gameDAO.getUserReport(userId);

            ObservableList<UserReport> data = FXCollections.observableArrayList(userReports);
            userReportTable.setItems(data);

            if (userReports.isEmpty()) {
                System.out.println("No games found for this user.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid User ID. Please enter a number.");
            userReportTable.getItems().clear();
        }
    }

    @FXML
    protected void handleLogout() {
        Main.setLoggedInUser(null);
        Main.switchScene("login_registration.fxml");
    }
}

