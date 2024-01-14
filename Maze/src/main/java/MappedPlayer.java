import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * The mapped player representing the JSON object representing a non-referee player
 */
public class MappedPlayer extends AbstractMappedPlayer {

    public MappedPlayer(MappedCoordinate current, MappedCoordinate home, String color) {
        super(current, home, color);
    }

    public PlayerState getPlayerState() {
        return new PlayerState(this.getColor(), home.getPosition(),new Position(0, 0),
                current.getPosition(), 0, new Player("Test Map Player", new RiemannStrategy()));
    }

    public static MappedPlayer[] playerStatesToPlayers(Queue<LimitedPlayerState> ps) {
        MappedPlayer[] ans = new MappedPlayer[ps.size()];
        List<LimitedPlayerState> psList = new ArrayList<>(ps);
        for (int i = 0; i < ps.size(); i += 1) {
            LimitedPlayerState currentPS = psList.get(i);
            MappedCoordinate currentMS = currentPS.getCurrent().getMappedCoordinate();
            MappedCoordinate homeMS = currentPS.getHome().getMappedCoordinate();
            String color = getHexString(currentPS.getAvatar());
            ans[i] = new MappedPlayer(currentMS,homeMS, color);
        }

        return ans;
    }

    public static JsonArray convertMappedPlayersToJsonArray(MappedPlayer[] mappedPlayers) {
        JsonArray playersAsJsonArray = new JsonArray();
        for (MappedPlayer player : mappedPlayers) {
            playersAsJsonArray.add(convertMappedPlayerToJsonObject(player));
        }
        return playersAsJsonArray;
    }

    private static JsonElement convertMappedPlayerToJsonObject(MappedPlayer mappedPlayer) {
        return GsonSingleton.getInstance().toJsonTree(mappedPlayer, MappedPlayer.class);
    }
}
