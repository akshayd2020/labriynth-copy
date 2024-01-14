import java.util.HashMap;
import java.util.Random;
import java.util.Map;

/**
 * Orientation of a tile if rotated counterclockwise the specified degrees
 */
public enum Orientation {
    ZERO(0),
    NINETY(90),
    ONE_EIGHTY(180),
    TWO_SEVENTY(270);

    private final int degree;

    private Orientation(int degree) {
        this.degree = degree;
    }

    public int getDegree() {
        return this.degree;
    }

    public static Orientation getRandom(Random r) {
        Orientation[] orientations = values();
        return orientations[r.nextInt(orientations.length)];
    }

    public static Map<Integer, Orientation> getIntToOrientation() {
        Map<Integer, Orientation> intToOrientation = new HashMap<>();
        intToOrientation.put(0, ZERO);
        intToOrientation.put(90, NINETY);
        intToOrientation.put(180, ONE_EIGHTY);
        intToOrientation.put(270, TWO_SEVENTY);
        return intToOrientation;
    }

    /**
     * Returns an orientation based on a degree which a multiple of 90
     * @param degree
     * @return
     */
    public static Orientation getOrientation(int degree) {
        int ninety = 90;
        int threeSixty = 360;
        int finalDegree = degree;

        if (degree % ninety != 0) {
            throw new IllegalArgumentException("Attempted to rotate an invalid degree");
        }
        if (degree < -1 * threeSixty) {
            finalDegree = degree % threeSixty + threeSixty;
        }
        if (-1 * threeSixty <= degree && degree <= -1 * ninety) {
            finalDegree = degree + threeSixty;
        }
        if (degree >= threeSixty) {
            finalDegree = degree % threeSixty;
        }
        return getIntToOrientation().get(finalDegree);
    }
}
