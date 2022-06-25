package it.polimi.ingsw.utilities.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.server.model.player.Assistant;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.SchoolBoard;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;

import java.util.*;

/**
 * Parser for the jsons which represent the saved games.
 *
 * @author Riccardo Motta
 */
public class JsonToObjects {

    private JsonToObjects() {
    }

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
     * @param json    Json object which represents the list of players.
     * @param players The players in the game.
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
        Map<Integer, Boolean> ids = new HashMap<>();
        List<Assistant> assistants = new ArrayList<>();

        for (JsonElement element : json)
            ids.put(element.getAsJsonObject().get("id").getAsInt(), element.getAsJsonObject().get("bonus").getAsBoolean());
        for (int index = 1; index <= 10; index++)
            assistants.add(ids.containsKey(index) ? new Assistant(index, ids.get(index)) : null);

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
                parseStudents(json.getAsJsonObject("diningRoom")),
                parseStudents(json.getAsJsonObject("entrance"))
        );
    }

    /**
     * Parses a json to get the corresponding GameBoard.
     *
     * @param json    Json object which represents the GameBoard.
     * @param expert  Determines whether the game is in expert mode or not.
     * @param players The players in the game.
     * @return The GameBoard that has been built.
     */
    public static GameBoard parseGameBoard(JsonObject json, boolean expert, Map<String, Player> players) {
        Map<HouseColor, Player> professors = new EnumMap<>(HouseColor.class);
        JsonElement tmp;

        for (HouseColor color : HouseColor.values()) {
            tmp = json.getAsJsonObject("professors").get(color.name());
            professors.put(color, !tmp.isJsonNull() ? players.get(tmp.getAsString()) : null);
        }

        return new GameBoard(
                parseStudents(json.getAsJsonObject("bag")),
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
                    parseStudents(island.getAsJsonObject("students")),
                    island.get("id").getAsInt(),
                    island.get("size").getAsInt(),
                    island.get("ban").getAsBoolean(),
                    island.get("tower").isJsonNull() ? null : TowerType.valueOf(island.get("tower").getAsString())
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
                    parseStudents(json.get(index).getAsJsonObject())
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
        if (json == null)
            return specialCharactersList;
        JsonObject specialCharacter;
        for (JsonElement item : json) {
            specialCharacter = item.getAsJsonObject();
            specialCharactersList.add(new SpecialCharacter(
                    specialCharacter.get("id").getAsInt(),
                    specialCharacter.get("effectCost").getAsInt(),
                    specialCharacter.get("alreadyPaid").getAsBoolean(),
                    specialCharacter.get("paidInTurn").getAsBoolean(),
                    specialCharacter.get("active").getAsBoolean(),
                    specialCharacter.has("containedStudents") ? parseStudents(specialCharacter.get("containedStudents").getAsJsonObject()) : null,
                    specialCharacter.has("availableBans") ? specialCharacter.get("availableBans").getAsInt() : 0,
                    specialCharacter.get("usesNumber").getAsInt()
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
    public static Map<HouseColor, Integer> parseStudents(JsonObject json) {
        Map<HouseColor, Integer> map = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values())
            map.put(color, json.get(color.name()).getAsInt());
        return map;
    }
}
