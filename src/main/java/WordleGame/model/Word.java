package WordleGame.model;

public class Word {
    private int id;
    private String word;

    // Constructor for creating a new Word object
    public Word(String word) {
        this.word = word;
    }

    // Constructor for retrieving an existing Word from the database
    public Word(int id, String word) {
        this.id = id;
        this.word = word;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                '}';
    }
}