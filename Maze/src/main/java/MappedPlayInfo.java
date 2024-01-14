import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Optional;

/**
 * Contains methods for converting a PlayInfo or Optional PlayInfo into a JsonElement object.
 */
public class MappedPlayInfo {
    /**
     * Converts the given Optional PlayInfo into a JsonElement representing either "PASS" if the optional
     * is empty, or a JsonElement representation of a Choice, which is a JSON array of Index, Direction, Degree,
     * and Coordinate.
     * @param optionalPlayInfo  the Optional PlayInfo to convert.
     * @return                  the JsonElement representation of the given Optional PlayInfo.
     */
    public static JsonElement convertPlayInfoToJsonElement(Optional<PlayInfo> optionalPlayInfo) {
        if (optionalPlayInfo.isEmpty()) {
            return new JsonPrimitive("PASS");
        }
        return convertPlayInfoToJsonElement(optionalPlayInfo.get());
    }

    /**
     * Converts the given PlayInfo into a JsonElement representing a Choice, which is
     * a JSON array of Index, Direction, Degree, and Coordinate.
     * @param playInfo          the PlayInfo to convert.
     * @return                  the JsonElement representation of the given PlayInfo.
     */
    public static JsonElement convertPlayInfoToJsonElement(PlayInfo playInfo) {
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(playInfo.getIndex());
        jsonArray.add(playInfo.getDirection().toString());
        jsonArray.add(playInfo.getDegreeToRotate());
        jsonArray.add(MappedCoordinate.convertPositionToJsonElement(playInfo.getNewPosition()));
        return jsonArray;
    }

    /**
     * Converts the given JsonElement to an Optional PlayInfo. This method should only be called
     * with a JsonElement that is a valid Choice.
     * @param element   the element to convert.
     * @return          an Optional PlayInfo representation of the given JSON element.
     */
    public static Optional<PlayInfo> convertJsonElementToPlayInfo(JsonElement element) {
        if (element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();
            int index = jsonArray.get(0).getAsInt();
            String directionStr = jsonArray.get(1).getAsString();
            Direction direction = Direction.getDirectionFromString(directionStr);
            int degree = jsonArray.get(2).getAsInt();
            Position coordinate = GsonSingleton.getInstance().fromJson(jsonArray.get(3), MappedCoordinate.class).getPosition();
            return Optional.of(new PlayInfo(index, direction, degree, coordinate));
        }
        return Optional.empty();
    }
}
