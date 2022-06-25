package it.polimi.ingsw.utilities.parsers;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.board.Cloud;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.server.model.board.effects.HerbalistEffect;
import it.polimi.ingsw.server.model.board.effects.JesterEffect;
import it.polimi.ingsw.server.model.board.effects.MonkEffect;
import it.polimi.ingsw.server.model.board.effects.PrincessEffect;
import it.polimi.ingsw.server.model.player.Assistant;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.SchoolBoard;
import it.polimi.ingsw.utilities.HouseColor;

import java.util.List;
import java.util.Map;

/**
 * Converts some game objects to json format.
 *
 * @author Riccardo Motta
 */
public enum ObjectsToJson {

    /**
     * Possible action when generating a JsonArray from players: getting a list of players' names.
     */
    GET_NAMES,

    /**
     * Possible action when generating a JsonArray from players: getting a list of players.
     */
    GET_PLAYERS;

    /**
     * Decides whether to create a list of names or a list of players with connected school board.
     *
     * @param players Players to parse.
     * @param action  Action to do.
     * @return The generated JsonArray.
     */
    public static JsonArray toJsonArray(List<Player> players, ObjectsToJson action) {
        return switch (action) {
            case GET_NAMES -> toNamesArray(players);
            case GET_PLAYERS -> toPlayersArray(players);
        };
    }

    /**
     * Generates a list of names from the players.
     *
     * @param players Players to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray toNamesArray(List<Player> players) {
        JsonArray list = new JsonArray();
        for (Player player : players)
            list.add(player.getName());
        return list;
    }

    /**
     * Generates a list of players with connected school board.
     *
     * @param players Players to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray toPlayersArray(List<Player> players) {
        JsonArray list = new JsonArray();
        for (Player player : players)
            list.add(parsePlayer(player));
        return list;
    }

    /**
     * Generates the object which contains the player.
     *
     * @param player Player to parse.
     * @return The generated JsonObject.
     */
    private static JsonObject parsePlayer(Player player) {
        JsonObject object = new JsonObject();
        object.addProperty("name", player.getName());
        object.addProperty("wizardType", player.getWizard().name());
        object.addProperty("coins", player.getCoins());
        object.add("assistants", parseAssistants(player.getAssistants()));
        object.add("schoolBoard", parseSchoolBoard(player.getSchoolBoard()));
        return object;
    }

    /**
     * Generates a list of assistants.
     *
     * @param assistants Assistants to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray parseAssistants(List<Assistant> assistants) {
        JsonArray list = new JsonArray();
        JsonObject assistantCard;
        for (Assistant assistant : assistants) {
            if (assistant != null) {
                assistantCard = new JsonObject();
                assistantCard.addProperty("id", assistant.getId());
                assistantCard.addProperty("bonus", assistant.hasBonus());
                list.add(assistantCard);
            }
        }
        return list;
    }

    /**
     * Generates the object which contains the school board.
     *
     * @param schoolBoard School board to parse.
     * @return The generated JsonObject.
     */
    private static JsonObject parseSchoolBoard(SchoolBoard schoolBoard) {
        JsonObject object = new JsonObject();
        object.addProperty("towerType", schoolBoard.getTowerType().name());
        object.addProperty("towersNumber", schoolBoard.getTowersNumber());
        object.add("entrance", parseStudents(schoolBoard.getEntrance()));
        object.add("diningRoom", parseStudents(schoolBoard.getDiningRoom()));
        return object;
    }

    /**
     * Generates the object which contains the whole realm.
     *
     * @param gameBoard The game board of the game.
     * @return The generated JsonObject.
     */
    public static JsonObject toJsonObject(GameBoard gameBoard) {
        JsonObject object = new JsonObject();
        object.add("bag", parseStudents(gameBoard.getBag().getStatus()));
        object.add("clouds", parseClouds(gameBoard.getClouds()));
        object.add("islands", parseIslands(gameBoard.getIslands()));
        object.add("playedAssistants", parsePlayedAssistants(gameBoard.getPlayedAssistants()));
        if (gameBoard.getCharacters() != null)
            object.add("characters", parseSpecialCharacters(gameBoard.getCharacters()));
        object.add("professors", parseProfessors(gameBoard.getProfessors()));
        object.addProperty("motherNatureIsland", gameBoard.getMotherNatureIsland().getId());
        if (gameBoard.getIgnoreColor() != null)
            object.addProperty("ignoreColor", gameBoard.getIgnoreColor().toString());
        else object.add("ignoreColor", JsonNull.INSTANCE);
        if (gameBoard.getInfluenceBonus() != null)
            object.addProperty("influenceBonus", gameBoard.getInfluenceBonus().getName());
        else object.add("influenceBonus", JsonNull.INSTANCE);
        return object;
    }

    /**
     * Generates a list of clouds.
     *
     * @param clouds Clouds to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray parseClouds(List<Cloud> clouds) {
        JsonArray list = new JsonArray();
        for (Cloud cloud : clouds)
            list.add(parseStudents(cloud.getStudents()));
        return list;
    }

    /**
     * Generates a list of Islands.
     *
     * @param islands Islands to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray parseIslands(List<Island> islands) {
        JsonArray list = new JsonArray();
        for (Island island : islands)
            list.add(parseIsland(island));
        return list;
    }

    /**
     * Generates the object which contains the island.
     *
     * @param island Island to parse.
     * @return The generated JsonObject.
     */
    private static JsonObject parseIsland(Island island) {
        JsonObject object = new JsonObject();
        object.addProperty("id", island.getId());
        object.addProperty("size", island.getSize());
        if (island.getTower() != null)
            object.addProperty("tower", island.getTower().name());
        else object.add("tower", JsonNull.INSTANCE);
        object.addProperty("ban", island.isBanned());
        object.add("students", parseStudents(island.getStudents()));
        return object;
    }

    /**
     * Generates a list of played assistants.
     *
     * @param playedAssistants Assistants to parse with respective player.
     * @return The generated JsonArray.
     */
    private static JsonArray parsePlayedAssistants(Map<Player, Assistant> playedAssistants) {
        JsonArray list = new JsonArray();
        for (Map.Entry<Player, Assistant> entry : playedAssistants.entrySet())
            list.add(parsePlayedAssistant(entry.getKey().getName(), entry.getValue().getId(), entry.getValue().hasBonus()));
        return list;
    }

    /**
     * Generates the object which contains the assistant.
     *
     * @param name      Name of the player who played it.
     * @param assistant Assistant connected to that player.
     * @return The generated JsonObject.
     */
    private static JsonObject parsePlayedAssistant(String name, int assistant, boolean bonus) {
        JsonObject object = new JsonObject();
        object.addProperty("player", name);
        object.addProperty("assistant", assistant);
        object.addProperty("bonus", bonus);
        return object;
    }

    /**
     * Generates a list of special characters.
     *
     * @param specialCharacters Assistants to parse.
     * @return The generated JsonArray.
     */
    private static JsonArray parseSpecialCharacters(List<SpecialCharacter> specialCharacters) {
        JsonArray list = new JsonArray();
        for (SpecialCharacter specialCharacter : specialCharacters)
            list.add(parseSpecialCharacter(specialCharacter));
        return list;
    }

    /**
     * Generates the object which contains the special character.
     *
     * @param specialCharacter Special character to parse.
     * @return The generated JsonObject.
     */
    private static JsonObject parseSpecialCharacter(SpecialCharacter specialCharacter) {
        JsonObject object = new JsonObject();
        String containedStudents = "containedStudents";
        object.addProperty("id", specialCharacter.getId());
        object.addProperty("effectCost", (specialCharacter.isAlreadyPaid()) ? specialCharacter.getEffectCost() - 1 : specialCharacter.getEffectCost());
        object.addProperty("alreadyPaid", specialCharacter.isAlreadyPaid());
        object.addProperty("paidInTurn", specialCharacter.isPaidInTurn());
        object.addProperty("active", specialCharacter.isActive());
        object.addProperty("usesNumber", specialCharacter.getUsesNumber());
        switch (specialCharacter.getEffect().getId()) {
            case 1 ->
                    object.add(containedStudents, parseStudents(((MonkEffect) specialCharacter.getEffect()).getStudents()));
            case 5 ->
                    object.addProperty("availableBans", ((HerbalistEffect) specialCharacter.getEffect()).getAvailableBans());
            case 7 ->
                    object.add(containedStudents, parseStudents(((JesterEffect) specialCharacter.getEffect()).getStudents()));
            case 11 ->
                    object.add(containedStudents, parseStudents(((PrincessEffect) specialCharacter.getEffect()).getStudents()));
        }

        return object;
    }

    /**
     * Generates the object which contains the link between the player and the professor.
     *
     * @param map Map with professor and respective player.
     * @return The generated JsonObject.
     */
    private static JsonObject parseProfessors(Map<HouseColor, Player> map) {
        JsonObject object = new JsonObject();
        for (HouseColor color : HouseColor.values())
            if (map.get(color) == null)
                object.add(color.name(), JsonNull.INSTANCE);
            else
                object.addProperty(color.name(), map.get(color).getName());
        return object;
    }

    /**
     * Generates the object which contains the amount of students for each color.
     *
     * @param map The students for each color.
     * @return The generated JsonObject.
     */
    private static JsonObject parseStudents(Map<HouseColor, Integer> map) {
        JsonObject object = new JsonObject();
        for (HouseColor color : HouseColor.values())
            object.addProperty(color.name(), map.get(color));
        return object;
    }
}
