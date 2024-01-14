import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MappedCoordinate {
    @SerializedName("row#")
    private int row;

    @SerializedName("column#")
    private int column;

    public MappedCoordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position getPosition() {
        return new Position(row, column);
    }

    public static JsonElement convertPositionToJsonElement(Position position) {
        return GsonSingleton.getInstance().toJsonTree(position.getMappedCoordinate(), MappedCoordinate.class);
    }

    public static Position convertsJsonToPosition(JsonElement element) {
        return GsonSingleton.getInstance().fromJson(element.getAsJsonObject(), MappedCoordinate.class).getPosition();
    }

    @Override
    public String toString() {
        return "row# " + Integer.toString(this.row) + " column# " + Integer.toString(this.column);
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof MappedCoordinate)) {
            return false;
        }

        MappedCoordinate toCompare = (MappedCoordinate) o;
        return this.column == toCompare.getColumn() && this.row == toCompare.getRow();
    }
}