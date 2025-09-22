package WordleGame.model;

public class Guess {
    private int id;
    private int gameId;
    private String guessWord;
    private int guessNumber;

    // Constructor for creating a new Guess object
    public Guess(int gameId, String guessWord, int guessNumber) {
        this.gameId = gameId;
        this.guessWord = guessWord;
        this.guessNumber = guessNumber;
    }

    // Constructor for retrieving an existing Guess from the database
    public Guess(int id, int gameId, String guessWord, int guessNumber) {
        this.id = id;
        this.gameId = gameId;
        this.guessWord = guessWord;
        this.guessNumber = guessNumber;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuessWord() {
        return guessWord;
    }

    public void setGuessWord(String guessWord) {
        this.guessWord = guessWord;
    }

    public int getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(int guessNumber) {
        this.guessNumber = guessNumber;
    }

    @Override
    public String toString() {
        return "Guess{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", guessWord='" + guessWord + '\'' +
                ", guessNumber=" + guessNumber +
                '}';
    }
}
