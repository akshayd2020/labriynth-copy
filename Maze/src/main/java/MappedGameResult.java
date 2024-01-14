import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Contains methods for converting a GameResult or into a JsonElement object.
 */
public class MappedGameResult {

    /**
     * Converts the given GameResult into a JsonElement representation, which is
     * a Json Array containing a Json Array of winning player names and a Json Array
     * of cheating player names, where each array of player names is sorted alphabetically.
     * @param results   the game results to convert.
     * @return          the JsonElement representing the game results.
     */
    public static JsonElement convertGameResultToJsonElement(GameResult results) {
        List<String>[] winnersAndMisbehaved = getSortedWinnersAndMisbehaved(results);
        return convertNamesToJson2DArray(winnersAndMisbehaved);
    }

    /**
     * Given the names of the winners and misbehaved players, returns a JsonArray of
     * JsonArrays of the winner names and misbehaved names respectively.
     * @param winnersAndMisbehaved  the lists of winners and misbehaved.
     * @return                      JsonArray of winners and misbehaved.
     */
    private static JsonArray convertNamesToJson2DArray(List<String>[] winnersAndMisbehaved) {
        JsonArray winnerJsonArray = convertNamesToJsonArray(winnersAndMisbehaved[0]);
        JsonArray misbehavedJsonArray = convertNamesToJsonArray(winnersAndMisbehaved[1]);
        JsonArray winnersAndMisbehavedArray = new JsonArray();
        winnersAndMisbehavedArray.add(winnerJsonArray);
        winnersAndMisbehavedArray.add(misbehavedJsonArray);
        return winnersAndMisbehavedArray;
    }


    private static List<String>[] getSortedWinnersAndMisbehaved(GameResult winnersAndMisbehaved) {
        Set<IPlayer> winners = winnersAndMisbehaved.getWinners();
        List<String> sortedWinners = getSortedPlayers(winners);
        Set<IPlayer> misbehaved = winnersAndMisbehaved.getMisbehaved();
        List<String> sortedMisbehaved = getSortedPlayers(misbehaved);
        List<String>[] sortedWinnersAndMisbehaved = (ArrayList<String>[]) new ArrayList[2];
        sortedWinnersAndMisbehaved[0] = sortedWinners;
        sortedWinnersAndMisbehaved[1] = sortedMisbehaved;
        return sortedWinnersAndMisbehaved;
    }

    /**
     * Sort the set of players into a sorted list
     * @param players   set of players represented by colors
     * @return          sorted list of players
     */
    private static List<String> getSortedPlayers(Set<IPlayer> players) {
        List<String> playerNames = getPlayerNames(players);
        Collections.sort(playerNames);
        return playerNames;
    }

    private static JsonArray convertNamesToJsonArray(List<String> names) {
        JsonArray res = new JsonArray();
        for (String name : names) {
            res.add(name);
        }
        return res;
    }

    /**
     * Get the list of player names that won
     * @param players   players of this game represented by colors
     * @return          list of player names
     */
    private static List<String> getPlayerNames(Set<IPlayer> players) {
        List<String> ans = new ArrayList<>();
        for (IPlayer player : players) {
            ans.add(player.getName());
        }
        return ans;
    }
}
