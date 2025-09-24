package WordleGame.service;

import javafx.scene.paint.Color;

public class GuessResult {

    public enum ColorCode {
        GREEN, ORANGE, GREY
    }

    private final char letter;
    private final ColorCode colorCode;

    public GuessResult(char letter, ColorCode colorCode) {
        this.letter = letter;
        this.colorCode = colorCode;
    }

    public char getLetter() {
        return letter;
    }

    public ColorCode getColorCode() {
        return colorCode;
    }
}
