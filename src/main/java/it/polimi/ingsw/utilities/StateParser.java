package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.SpecialCharacter;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;

import java.util.*;

/**
 * Parser for the jsons which represent the saved games.
 *
 * @author Riccardo Motta
 */
public class StateParser {

    /**
     * Parses a json to get the corresponding Player.
     *
     * @param json Json object which represents the Player.
     * @return The Player that has been built.
     */
    public static Player parsePlayer(JsonObject json) {
        return new Player(
                json.get("name").getAsString(),
                WizardType.valueOf(json.get("wizardType").getAsString()),
                parseAssistants(json.getAsJsonArray("assistants")),
                json.get("coins").getAsInt(),
                parseSchoolBoard(json.getAsJsonObject("schoolBoard"))
        );
    }

    /**
     * Parses a json to get the corresponding list of players.
     *
     * @param json Json object which represents the list of players.
     * @return The List that has been built.
     */
    public static List<Player> parsePlayersList(JsonArray json, Map<String, Player> players) {
        List<Player> playersList = new ArrayList<>();
        for (JsonElement item : json)
            playersList.add(players.get(item.getAsString()));
        return playersList;
    }

    /**
     * Parses a json to get the corresponding list of assistants.
     *
     * @param json Json object which represents the list of assistants.
     * @return The List that has been built.
     */
    private static List<Assistant> parseAssistants(JsonArray json) {
        Set<Integer> ids = new HashSet<>();
        List<Assistant> assistants = new ArrayList<>();

        for (JsonElement id : json)
            ids.add(id.getAsInt());
        for (int index = 1; index <= 10; index++)
            assistants.add(ids.contains(index) ? new Assistant(index) : null);

        return assistants;
    }

    /**
     * Parses a json to get the corresponding SchoolBoard.
     *
     * @param json Json object which represents the SchoolBoard.
     * @return The SchoolBoard that has been built.
     */
    private static SchoolBoard parseSchoolBoard(JsonObject json) {
        return new SchoolBoard(
                json.get("towersNumber").getAsInt(),
                TowerType.valueOf(json.get("towerType").getAsString()),
                parseHouseColorIntMap(json.getAsJsonObject("diningRoom")),
                parseHouseColorIntMap(json.getAsJsonObject("entrance"))
        );
    }

    /**
     * Parses a json to get the corresponding GameBoard.
     *
     * @param json Json object which represents the GameBoard.
     * @return The GameBoard that has been built.
     */
    public static GameBoard parseGameBoard(JsonObject json, boolean expert, Map<String, Player> players) {
        Map<HouseColor, Player> professors = new HashMap<>();
        JsonElement tmp;

        for (HouseColor color : HouseColor.values()) {
            tmp = json.getAsJsonObject("professors").get(color.name());
            professors.put(color, !tmp.isJsonNull() ? players.get(tmp.getAsString()) : null);
        }

        return new GameBoard(
                parseHouseColorIntMap(json.getAsJsonObject("bag")),
                parsePlayedAssistants(json.getAsJsonArray("playedAssistants"), players),
                parseIslands(json.getAsJsonArray("islands")),
                parseClouds(json.getAsJsonArray("clouds")),
                parseSpecialCharacters(json.getAsJsonArray("characters")),
                professors,
                expert,
                json.get("motherNatureIsland").getAsInt()
        );
    }

    /**
     * Parses a json to get the corresponding played assistants.
     *
     * @param json Json object which represents the played assistants.
     * @return The Map that has been built.
     */
    private static Map<Player, Assistant> parsePlayedAssistants(JsonArray json, Map<String, Player> players) {
        Map<Player, Assistant> playedAssistants = new HashMap<>();
        for (JsonElement item : json)
            playedAssistants.put(players.get(item.getAsJsonObject().get("player").getAsString()), new Assistant(item.getAsJsonObject().get("assistant").getAsInt()));
        return playedAssistants;
    }

    /**
     * Parses a json to get the corresponding list of islands.
     *
     * @param json Json object which represents the list of islands.
     * @return The List that has been built.
     */
    private static List<Island> parseIslands(JsonArray json) {
        List<Island> islandsList = new ArrayList<>();
        JsonObject island;
        for (int index = 0; index < json.size(); index++) {
            island = json.get(index).getAsJsonObject();
            islandsList.add(new Island(
                    parseHouseColorIntMap(island.getAsJsonObject("students")),
                    index,
                    island.get("size").getAsInt(),
                    island.get("ban").getAsBoolean(),
                    TowerType.valueOf(island.get("tower").getAsString())
            ));
        }
        return islandsList;
    }

    /**
     * Parses a json to get the corresponding list of clouds.
     *
     * @param json Json object which represents the list of clouds.
     * @return The List that has been built.
     */
    private static List<Cloud> parseClouds(JsonArray json) {
        List<Cloud> cloudsList = new ArrayList<>();
        for (int index = 0; index < json.size(); index++)
            cloudsList.add(new Cloud(
                    index,
                    parseHouseColorIntMap(json.get(index).getAsJsonObject())
            ));
        return cloudsList;
    }

    /**
     * Parses a json to get the corresponding list of special characters.
     *
     * @param json Json object which represents the list of special characters.
     * @return The List that has been built.
     */
    private static List<SpecialCharacter> parseSpecialCharacters(JsonArray json) {
        List<SpecialCharacter> specialCharactersList = new ArrayList<>();
        JsonObject specialCharacter;
        for (JsonElement item : json) {
            specialCharacter = item.getAsJsonObject();
            specialCharactersList.add(new SpecialCharacter(
                    specialCharacter.get("id").getAsInt(),
                    specialCharacter.get("effectCost").getAsInt(),
                    specialCharacter.get("alreadyPlayed").getAsBoolean(),
                    specialCharacter.get("playedInRound").getAsBoolean(),
                    specialCharacter.get("active").getAsBoolean()
            ));
        }
        return specialCharactersList;
    }

    /**
     * Parses a json to get the corresponding map of students.
     *
     * @param json Json object which represents the map of students.
     * @return The Map that has been built.
     */
    private static Map<HouseColor, Integer> parseHouseColorIntMap(JsonObject json) {
        Map<HouseColor, Integer> map = new HashMap<>();
        for (HouseColor color : HouseColor.values())
            map.put(color, json.get(color.name()).getAsInt());
        return map;
    }
}
