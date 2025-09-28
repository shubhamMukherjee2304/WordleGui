package WordleGame.model;


import java.time.LocalDate;

public class UserReport {
    private String targetWord;
    private String gameDate;
    private boolean isWin;
    private int numGuesses;

    public UserReport(String targetWord, String gameDate, boolean isWin, int numGuesses) {
        this.targetWord = targetWord;
        this.gameDate = gameDate;
        this.isWin = isWin;
        this.numGuesses = numGuesses;
    }

    // Getters
    public String getTargetWord() {
        return targetWord;
    }

    public String getGameDate() {
        return gameDate;
    }

    public boolean getIsWin() {
        return isWin;
    }

    public int getNumGuesses() {
        return numGuesses;
    }
}

