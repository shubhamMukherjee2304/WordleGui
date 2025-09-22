package WordleGame.model;

public class Game {
    private int id;
    private int userId;
    private String targetWord;
    private String gameDate;
    private boolean isWin;

    // Constructor for creating a new Game session
    public Game(int userId, String targetWord, String gameDate) {
        this.userId = userId;
        this.targetWord = targetWord;
        this.gameDate = gameDate;
        this.isWin = false; // A new game starts with isWin as false
    }

    // Constructor for retrieving an existing Game from the database
    public Game(int id, int userId, String targetWord, String gameDate, boolean isWin) {
        this.id = id;
        this.userId = userId;
        this.targetWord = targetWord;
        this.gameDate = gameDate;
        this.isWin = isWin;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", userId=" + userId +
                ", targetWord='" + targetWord + '\'' +
                ", gameDate='" + gameDate + '\'' +
                ", isWin=" + isWin +
                '}';
    }
}
