import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.function.Function;

/**
 * Contains all methods to validate that a given JsonElement is of a specified
 */
public class JsonValidator {
    /**
     * A valid index is a Json integer representing a natural number.
     * @return  true if the given JsonElement is a valid index, false otherwise.
     */
    private static boolean isValidIndex(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isNumber()
                && element.getAsInt() >= 0;
    }

    /**
     * A valid direction is a Json string representing one of "LEFT", "RIGHT", "UP", or "DOWN".
     * @return  true if the given JsonElement is a valid direction, false otherwise.
     */
    private static boolean isValidDirection(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString()
                && (element.getAsString().equals("LEFT")
                || element.getAsString().equals("RIGHT")
                || element.getAsString().equals("UP")
                || element.getAsString().equals("DOWN"));
    }

    /**
     * A valid degree is a Json integer representing one of 0, 90, 180, or 270.
     * @return  true if the given JsonElement is a valid degree, false otherwise.
     */
    private static boolean isValidDegree(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isNumber()
                && (element.getAsInt() == 0
                || element.getAsInt() == 90
                || element.getAsInt() == 180
                || element.getAsInt() == 270);
    }

    /**
     * A valid coordinate is a Json Object with two fields:
     * - "row#" : Index
     * - "column#" : Index
     * @return  true if the given JsonElement is a valid coordinate, false otherwise.
     */
    private static boolean isValidCoordinate(JsonElement element) {
        return isValidJsonObjectWithSize(element, 2)
                && element.getAsJsonObject().has("row#")
                && isValidIndex(element.getAsJsonObject().get("row#"))
                && element.getAsJsonObject().has("column#")
                && isValidIndex(element.getAsJsonObject().get("column#"));
    }

    /**
     * A valid Choice is one of:
     * - "PASS"
     * - an array of four JSON values: [Index, Direction, Degree, Coordinate]
     * @return  true if the given JsonElement is a valid Choice, false otherwise.
     */
    public static boolean isValidChoice(JsonElement element) {
        return isValidPass(element)
                || (isValidJsonArrayWithSize(element, 4)
                && isValidIndex(element.getAsJsonArray().get(0))
                && isValidDirection(element.getAsJsonArray().get(1))
                && isValidDegree(element.getAsJsonArray().get(2))
                && isValidCoordinate(element.getAsJsonArray().get(3)));
    }

    /**
     * A valid pass is a JSON string "PASS".
     * @return  true if the given JsonElement is a valid pass, false otherwise.
     */
    private static boolean isValidPass(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString()
                && element.getAsString().equals("PASS");
    }

    /**
     * A valid method call is a JSON array in the format of:
     * - [ MName, [Argument, ...] ]
     * @return  true if the given JsonElement is a valid method call, false otherwise.
     */
    public static boolean isValidMethodCall(JsonElement element) {
        return isValidJsonArrayWithSize(element, 2)
                && isValidMethodHelper(element);
    }

    /**
     * A valid MName is a JSON string that is one of:
     * - "setup"
     * - "take-turn"
     * - "win"
     * @return  true if the given JsonElement is a valid MName, false otherwise.
     */
    private static boolean isValidMethodHelper(JsonElement element) {
        JsonElement firstElement = element.getAsJsonArray().get(0);
        return firstElement.isJsonPrimitive()
                && firstElement.getAsJsonPrimitive().isString()
                && isValidArgument(firstElement.getAsString(), element.getAsJsonArray().get(1));
    }

    /**
     * A valid argument is a JSON array that is one of three things depending on the given MName:
     * - If the MName is "setup", the argument is a JsonArray containing a F/State and a Coordinate.
     * - If the MName is "take-turn", the argument is a JSON State.
     * - If the MName is "win", the argument is a JSON boolean.
     * @param mname     the given valid MName of the method that this argument is for.
     * @param arguments the given JsonElement to check.
     * @return          true if the given JsonElement is a valid argument, false otherwise.
     */
    private static boolean isValidArgument(String mname, JsonElement arguments) {
        switch (mname) {
            case "setup":
                return isValidSetupArguments(arguments);
            case "take-turn":
                return isValidTakeTurnArguments(arguments);
            case "win":
                return isValidWinArguments(arguments);
            default:
                return false;
        }
    }

    /**
     * Determines whether the given arguments in JSON are valid for the setup function.
     * @return whether the given arguments in JSON are valid for the setup function
     */
    private static boolean isValidSetupArguments(JsonElement arguments) {
        return isValidJsonArrayWithSize(arguments, 2)
                && checkIfFalseOrState(arguments.getAsJsonArray().get(0))
                && isValidCoordinate(arguments.getAsJsonArray().get(1));
    }

    /**
     * Determines whether the given arguments in JSON are valid for the take-turn function.
     * @return whether the given arguments in JSON are valid for the take-turn function
     */
    private static boolean isValidTakeTurnArguments(JsonElement arguments) {
        return isValidJsonArrayWithSize(arguments,  1)
                && isState(arguments.getAsJsonArray().get(0));
    }

    /**
     * Determines whether the given arguments in JSON are valid for the win function.
     * @return whether the given arguments in JSON are valid for the win function
     */
    private static boolean isValidWinArguments(JsonElement arguments) {
        return isValidJsonArrayWithSize(arguments,  1)
                && arguments.getAsJsonArray().get(0).isJsonPrimitive()
                && arguments.getAsJsonArray().get(0).getAsJsonPrimitive().isBoolean();
    }

    /**
     * Determine whether the given element is False or a State.
     * @return whether the given element is False or a State.
     */
    private static boolean checkIfFalseOrState(JsonElement element) {
        boolean isFalse = element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isBoolean()
                && !element.getAsBoolean();
        boolean isState = isState(element);
        return isFalse || isState;
    }

    /**
     * Determines whether the given element is a valid State argument.
     * @param element   the element to check
     * @return          whether the given element is a valid State argument.
     */
    private static boolean isState(JsonElement element) {
        return isValidJsonObjectWithSize(element, 4)
                && hasValidFieldJson("board", element.getAsJsonObject(), JsonValidator::isValidBoardJson)
                && hasValidFieldJson("spare", element.getAsJsonObject(), JsonValidator::isValidTileJson)
                && hasValidFieldJson("plmt", element.getAsJsonObject(), JsonValidator::isValidPlayerArrayJson)
                && hasValidFieldJson("last", element.getAsJsonObject(), JsonValidator::isValidLastJson);
    }

    /**
     * Does the given JsonObject have the specified field and is the field value valid?
     * @param fieldName         the field name to check
     * @param jsonObject        the JsonObject to check if the field exists
     * @param fieldValidator    the function that checks the validity of the object in the given field
     * @return                  whether the given JsonObject has the field and a valid value
     */
    private static boolean hasValidFieldJson(String fieldName, JsonObject jsonObject, Function<JsonElement,
            Boolean> fieldValidator) {
        return jsonObject.has(fieldName)
                && fieldValidator.apply(jsonObject.get(fieldName));
    }

    /**
     * Determines whether the given element is a valid Board argument.
     * @param element   the element to check
     * @return          whether the given element is a valid Board argument.
     */
    private static boolean isValidBoardJson(JsonElement element) {
        return isValidJsonObjectWithSize(element, 2)
                && hasValidFieldJson("connectors", element.getAsJsonObject(), JsonValidator::isValidConnectorMatrix)
                && hasValidFieldJson("treasures", element.getAsJsonObject(), JsonValidator::isValidTreasuresMatrix);
    }

    /**
     * Determines if a given JsonElement is a valid matrix, which is a JsonArray of JsonArrays containing JsonElements
     * of the format specified by the given elementValidator.
     * @param element           the given element to check.
     * @param elementValidator  a function that takes a JsonElement and determines if it is in a valid format.
     * @return                  true if the JsonElement is a valid Matrix, false otherwise.
     */
    private static boolean isValidMatrix(JsonElement element, Function<JsonElement, Boolean> elementValidator) {
        if (!element.isJsonArray()) {
            return false;
        }
        if (!areRowsAndCellsValid(element, elementValidator)) return false;
        return true;
    }

    private static boolean areRowsAndCellsValid(JsonElement element, Function<JsonElement, Boolean> elementValidator) {
        for (JsonElement row : element.getAsJsonArray()) {
            if (!row.isJsonArray()) {
                return false;
            }
            if (!areElementsValidInThisJsonArray(elementValidator, row.getAsJsonArray())) return false;
        }
        return true;
    }

    private static boolean areElementsValidInThisJsonArray(Function<JsonElement, Boolean> elementValidator, JsonArray array) {
        for (JsonElement cell : array) {
            if (!elementValidator.apply(cell)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the given element is a valid Connector matrix.
     * @param element   the element to check
     * @return          whether the given element is a valid Connector matrix.
     */
    private static boolean isValidConnectorMatrix(JsonElement element) {
        return isValidMatrix(element, JsonValidator::isValidConnector);
    }

    /**
     * Determines whether the given element is a valid Connector, which is one of:
     * - "│", "─", "┐", "└", "┌", "┘", "┬", "├", "┴", "┤", "┼".
     * @param element   the given element to check.
     * @return          true if the element is a valid Connector, false otherwise.
     */
    private static boolean isValidConnector(JsonElement element) {
        boolean isString = element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString();
        boolean isConnectorSymbol = Connector.getCharacterMap().containsValue(element.getAsString().charAt(0));
        return isString && isConnectorSymbol;
    }

    private static boolean isValidTreasure(JsonElement element) {
        return isValidJsonArrayWithSize(element, 2)
                && isValidGem(element.getAsJsonArray().get(0))
                && isValidGem(element.getAsJsonArray().get(1));
    }

    private static boolean isValidGem(JsonElement element) {
        boolean isString = element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString();
        boolean isGem = Gem.isGem(element.getAsString());
        return isString && isGem;
    }

    /**
     * Determines whether the given element is a valid Treasures matrix.
     * @param element   the element to check
     * @return          whether the given element is a valid Treasures matrix.
     */
    private static boolean isValidTreasuresMatrix(JsonElement element) {
        return isValidMatrix(element, JsonValidator::isValidTreasure);
    }

    /**
     * Determines whether the given element is a valid Tile argument.
     * @param element   the element to check
     * @return          whether the given element is a valid Tile argument.
     */
    private static boolean isValidTileJson(JsonElement element) {
        return isValidJsonObjectWithSize(element, 3)
                && hasValidFieldJson("tilekey", element.getAsJsonObject(), JsonValidator::isValidConnector)
                && hasValidFieldJson("1-image", element.getAsJsonObject(), JsonValidator::isValidGem)
                && hasValidFieldJson("2-image", element.getAsJsonObject(), JsonValidator::isValidGem);
    }

    /**
     * Determines if the given JsonElement is an object with the specified number of fields.
     * @param element   the JsonElement to check.
     * @param size      the number of fields this JsonObject should have.
     * @return          true if the JsonElement is a JsonObject with the exact number of fields specified, false otherwise.
     */
    private static boolean isValidJsonObjectWithSize(JsonElement element, int size) {
        return element.isJsonObject() && element.getAsJsonObject().size() == size;
    }

    /**
     * Determines if the given JsonElement is an array with the specified size.
     * @param element   the JsonElement to check.
     * @param size      the size of this JsonArray.
     * @return          true if the JsonElement is a JsonArray of the specified size, false otherwise.
     */
    private static boolean isValidJsonArrayWithSize(JsonElement element, int size) {
        return element.isJsonArray() && element.getAsJsonArray().size() == size;
    }

    /**
     * Determines whether the given element is a valid Player array argument.
     * @param element   the element to check
     * @return          whether the given element is a valid Player array argument.
     */
    private static boolean isValidPlayerArrayJson(JsonElement element) {
        return element.isJsonArray()
                && areElementsValidInThisJsonArray(JsonValidator::isValidPlayerJson, element.getAsJsonArray());
    }

    private static boolean isValidPlayerJson(JsonElement element) {
        return isValidJsonObjectWithSize(element, 3)
                && hasValidFieldJson("current", element.getAsJsonObject(), JsonValidator::isValidCoordinate)
                && hasValidFieldJson("home", element.getAsJsonObject(), JsonValidator::isValidCoordinate)
                && hasValidFieldJson("color", element.getAsJsonObject(), JsonValidator::isValidColorJson);
    }

    private static boolean isValidColorJson(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString()
                && isValidColorString(element.getAsString());
    }

    private static boolean isValidColorString(String colorString) {
        switch(colorString) {
            case "purple":
            case "orange":
            case "pink":
            case "red":
            case "blue":
            case "green":
            case "yellow":
            case "white":
            case "black":
                return true;
            default:
                return colorString.matches("^[A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d][A-F|\\d]$");
        }
    }

    /**
     * Determines whether the given element is a valid Last argument.
     * @param element   the element to check
     * @return          whether the given element is a valid Last argument.
     */
    private static boolean isValidLastJson(JsonElement element) {
        return element.isJsonNull() || isValidSlidingActionJson(element);
    }

    /**
     * Determines whether the given element is a valid SlidingAction argument.
     * @param element   the element to check
     * @return          whether the given element is a valid SlidingAction argument.
     */
    private static boolean isValidSlidingActionJson(JsonElement element) {
        return isValidJsonArrayWithSize(element, 2)
                && isValidIndex(element.getAsJsonArray().get(0))
                && isValidDirection(element.getAsJsonArray().get(1));
    }

    /**
     * Determines if the given JsonElement is a valid Name as specified on the
     * <a href="https://course.ccs.neu.edu/cs4500f22/6.html#%28tech._name%29">milestone page</a>.
     * A valid name is a string of at least one and at most 20 alphanumeric characters, AKA
     * matches the regular expression "^[a-zA-Z0-9]+$".
     * @param element   the given JsonElement.
     * @return          true if the element is a valid name, false otherwise.
     */
    public static boolean isValidJsonName(JsonElement element) {
        return element.isJsonPrimitive()
                && element.getAsJsonPrimitive().isString()
                && isValidStringName(element.getAsString());
    }

    /**
     * Determines if a given string name matches the specification for a valid Name, where
     * the name is at least one and at most 20 alphanumeric characters that matches the
     * regex "^[a-zA-Z0-9]+$".
     * @param name      the given string to check.
     * @return          true if the string is in a valid Name format, false otherwise.
     */
    private static boolean isValidStringName(String name) {
        return name.length() >= 1 && name.length() <= 20
                && name.matches("^[a-zA-Z0-9]+$");
    }
}