import com.google.gson.JsonArray;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class JsonToGamesWithBadPlayers2 extends JsonToGamesWithBadPlayers {
    public JsonToGamesWithBadPlayers2(InputStream inputStream, OutputStream outputStream)
            throws UnsupportedEncodingException {

        super(inputStream, outputStream);
    }

    /**
     * Convert the JSON array representation of a referee player into a player.
     * Will handle PS, BadPS, and BadPS2 JSON arrays as specified on the milestone page.
     * @param ps    single player representation
     * @return      player
     */
    @Override
    protected IPlayer convertToPlayer(JsonArray ps) {
        String name = ps.get(0).getAsString();
        String strategy = ps.get(1).getAsString();
        if (isBadPSFormat(ps)) {
            return getNewPlayerFailsBadFM(ps);
        }
        else if (isBadPS2Format(ps)) {
            return getNewPlayerLoops(ps);
        }
        return new Player(name, convertToStrategy(strategy));
    }

    private IPlayer getNewPlayerLoops(JsonArray ps) {
        String name = ps.get(0).getAsString();
        IStrategy strategy = convertToStrategy(ps.get(1).getAsString());
        String badFM = ps.get(2).getAsString();
        int count = ps.get(3).getAsInt();
        switch(badFM) {
            case "setUp":
                return new PlayerLoopsSetUp(name, strategy, count);
            case "takeTurn":
                return new PlayerLoopsTakeTurn(name, strategy, count);
            case "win":
                return new PlayerLoopsWon(name, strategy, count);
            default:
                throw new IllegalArgumentException("Should never reach this case because PlayerLoop can only be above cases");
        }
    }
}
