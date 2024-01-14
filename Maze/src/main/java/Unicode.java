import java.util.Random;

public enum Unicode {
    BAR('\u2502'),
    CORNER('\u2514'),
    T_SHAPE('\u252C'),
    PLUS('\u253C');

    char unicode;

    private Unicode(char unicode) {
        this.unicode = unicode;
    }

    public char getChar() {
        return this.unicode;
    }

    // TODO abstract this function
    public static Unicode getRandom(Random r) {
        Unicode[] unicodeChars = values();
        return unicodeChars[r.nextInt(unicodeChars.length)];
    }
}