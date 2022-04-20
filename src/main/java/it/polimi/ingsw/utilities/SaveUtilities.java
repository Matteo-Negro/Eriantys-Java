package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.board.Cloud;
import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.SpecialCharacter;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.SchoolBoard;

import java.util.List;
import java.util.Map;

/**
 * Converts some game objects to json format.
 *
 * @author Riccardo Motta
 */
public enum SaveUtilities {

    GET_NAMES, GET_PLAYERS;

    public static JsonArray toJsonArray(List<Player> players, SaveUtilities action) {
        return switch (action) {
            case GET_NAMES -> toNamesArray(players);
            case GET_PLAYERS -> toPlayersArray(players);
        };
    }

    private static JsonArray toNamesArray(List<Player> players) {
        JsonArray list = new JsonArray();
        for (Player player : players)
            list.add(player.getName());
        return list;
    }

    private static JsonArray toPlayersArray(List<Player> players) {
        JsonArray list = new JsonArray();
        for (Player player : players)
            list.add(parsePlayer(player));
        return list;
    }

    private static JsonObject parsePlayer(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("name", player.getName());
        object.addProperty("wizardType", player.getWizard().name());
        object.addProperty("coins", player.getCoins());
        object.add("assistants", parseAssistants(player.getAssistants()));
        object.add("schoolBoard", parseSchoolBoard(player.getSchoolBoard()));
        return object;
    }

    private static JsonArray parseAssistants(List<Assistant> assistants) {
        JsonArray list = new JsonArray();
        for (Assistant assistant : assistants)
            list.add(assistant.getId());
        return list;
    }

    private static JsonObject parseSchoolBoard(SchoolBoard schoolBoard) {
        JsonObject object = new JsonObject();
        object.addProperty("towerType", schoolBoard.getTowerType().name());
        object.addProperty("towersNumber", schoolBoard.getTowersNumber());
        object.add("entrance", parseHouseColorIntMap(schoolBoard.getEntrance()));
        object.add("diningRoom", parseHouseColorIntMap(schoolBoard.getDiningRoom()));
        return object;
    }

    public static JsonObject toJsonObject(GameBoard gameBoard) {
        JsonObject object = new JsonObject();
        object.add("bag", parseHouseColorIntMap(gameBoard.getBag().getStatus()));
        object.add("clouds", parseClouds(gameBoard.getClouds()));
        object.add("islands", parseIslands(gameBoard.getIslands()));
        object.add("playedAssistants", parsePlayedAssistants(gameBoard.getPlayedAssistants()));
        object.add("characters", parseSpecialCharacters(gameBoard.getCharacters()));
        object.add("professors", parseProfessors(gameBoard.getProfessors()));
        object.addProperty("motherNatureIsland", gameBoard.getMotherNatureIsland().getId());
        return object;
    }

    private static JsonArray parseClouds(List<Cloud> clouds) {
        JsonArray list = new JsonArray();
        for (Cloud cloud : clouds)
            list.add(parseHouseColorIntMap(cloud.getStudents()));
        return list;
    }

    private static JsonArray parseIslands(List<Island> islands) {
        JsonArray list = new JsonArray();
        for (Island island : islands)
            list.add(parseIsland(island));
        return list;
    }

    private static JsonObject parseIsland(Island island) {
        JsonObject object = new JsonObject();
        object.addProperty("size", island.getSize());
        object.addProperty("tower", island.getTower().name());
        object.addProperty("ban", island.isBanned());
        object.add("students", parseHouseColorIntMap(island.getStudents()));
        return object;
    }

    private static JsonArray parsePlayedAssistants(Map<Player, Assistant> playedAssistants) {
        JsonArray list = new JsonArray();
        for (Player player : playedAssistants.keySet())
            list.add(parsePlayedAssistant(player.getName(), playedAssistants.get(player).getId()));
        return list;
    }

    private static JsonObject parsePlayedAssistant(String name, int assistant) {
        JsonObject object = new JsonObject();
        object.addProperty("player", name);
        object.addProperty("assistant", assistant);
        return object;
    }

    private static JsonArray parseSpecialCharacters(List<SpecialCharacter> specialCharacters) {
        JsonArray list = new JsonArray();
        for (SpecialCharacter specialCharacter : specialCharacters)
            list.add(parseSpecialCharacter(specialCharacter));
        return list;
    }

    private static JsonObject parseSpecialCharacter(SpecialCharacter specialCharacter) {
        JsonObject object = new JsonObject();
        object.addProperty("id", specialCharacter.getId());
        object.addProperty("effectCost", specialCharacter.getEffectCost());
        object.addProperty("alreadyPaid", specialCharacter.isAlreadyPaid());
        object.addProperty("paidInRound", specialCharacter.isPaidInRound());
        object.addProperty("active", specialCharacter.isActive());
        return object;
    }

    private static JsonObject parseProfessors(Map<HouseColor, Player> map) {
        JsonObject object = new JsonObject();
        for (HouseColor color : HouseColor.values())
            if (map.get(color) == null)
                object.add(color.name(), JsonNull.INSTANCE);
            else
                object.addProperty(color.name(), map.get(color).getName());
        return object;
    }

    private static JsonObject parseHouseColorIntMap(Map<HouseColor, Integer> map) {
        JsonObject object = new JsonObject();
        for (HouseColor color : HouseColor.values())
            object.addProperty(color.name(), map.get(color));
        return object;
    }
}
