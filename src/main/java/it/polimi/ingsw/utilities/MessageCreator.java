package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.parsers.ObjectsToJson;

import java.util.List;

/**
 * This class generates JsonObjects for the various communication cases.
 *
 * @author Riccardo Motta
 */
public class MessageCreator {

    private MessageCreator() {
    }

    /**
     * Creates the reply for "gameCreation" message.
     *
     * @param code The code of the newly created game.
     * @return JsonObject which represents the message.
     */
    public static JsonObject gameCreation(String code) {
        JsonObject replay = new JsonObject();
        replay.addProperty("type", "gameCreation");
        replay.addProperty("code", code);
        return replay;
    }

    /**
     * Creates the reply for "enterGame" message.
     *
     * @param gameController The GameController from which getting all the required data.
     * @return JsonObject which represents the message.
     */
    public static JsonObject enterGame(GameController gameController) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "enterGame");
        reply.addProperty("found", gameController != null);
        if (gameController == null)
            return reply;
        reply.addProperty("expectedPlayers", gameController.getExpectedPlayers());
        reply.add("players", getOnlinePlayers(gameController));
        return reply;
    }

    /**
     * Sends a waiting room update message.
     *
     * @param gameController The backend controller of the game.
     * @return JsonObject which represents the message.
     */
    public static JsonObject waitingRoomUpdate(GameController gameController) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "waitingRoomUpdate");
        reply.addProperty("expectedPlayers", gameController.getExpectedPlayers());
        reply.add("players", getOnlinePlayers(gameController));
        return reply;
    }

    /**
     * Creates a list of players for "enterGame" message.
     *
     * @param gameController The GameController from which getting all the required data.
     * @return JsonArray which represents the list of players and connected users.
     */
    private static JsonArray getOnlinePlayers(GameController gameController) {
        JsonArray list = new JsonArray();
        for (String username : gameController.getUsernames().stream().sorted().toList()) {
            JsonObject player = new JsonObject();
            player.addProperty("name", username);
            player.addProperty("online", gameController.getUser(username) != null);
            list.add(player);
        }
        return list;
    }

    /**
     * Creates the reply for "login" message.
     *
     * @param logged A boolean second which indicates if the user has correctly logged to a game or not.
     * @return JsonObject which represents the message.
     */
    public static JsonObject login(boolean logged) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "login");
        reply.addProperty("success", logged);
        return reply;
    }

    /**
     * Creates the message used to give the communication token to a specified user.
     *
     * @param player The name of the player who is going to receive the communication token.
     * @param enable true if it's this player's turn, false otherwise.
     * @return JsonObject which represents the message.
     */
    public static JsonObject turnEnable(String player, boolean enable) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "turnEnable");
        reply.addProperty("player", player);
        reply.addProperty("enable", enable);
        return reply;
    }

    /**
     * Creates the message used to send the current game status to a user who just joined the game.
     *
     * @param game The gameController instance involved.
     * @return JsonObject which represents the message.
     */
    public static JsonObject status(GameController game) {

        JsonObject reply = new JsonObject();
        reply.addProperty("type", "status");
        reply.addProperty("round", game.getRound());
        reply.addProperty("expert", game.getGameModel().isExpert());
        if (game.getActiveUser() != null)
            reply.addProperty("activeUser", game.getActiveUser());
        else reply.add("activeUser", JsonNull.INSTANCE);
        reply.addProperty("phase", game.getPhase().toString());
        reply.addProperty("subPhase", game.getSubPhase().toString());
        reply.add("players", ObjectsToJson.toJsonArray(game.getGameModel().getPlayers(), ObjectsToJson.GET_PLAYERS));
        reply.add("gameBoard", ObjectsToJson.toJsonObject(game.getGameModel().getGameBoard()));
        return reply;
    }

    /**
     * Creates the message used to tell the clients that the game has started running.
     *
     * @return JsonObject which represents the message.
     */
    public static JsonObject gameStart() {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "gameStart");
        return reply;
    }

    /**
     * Creates the "ping" message.
     *
     * @return JsonObject which represents the message.
     */
    public static JsonObject ping() {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "ping");
        return reply;
    }

    /**
     * Creates the "pong" message.
     *
     * @return JsonObject which represents the message.
     */
    public static JsonObject pong() {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "pong");
        return reply;
    }

    /**
     * Creates the "win" message.
     *
     * @param playerList A List with the winner of the game, or the players who drawn.
     * @return JsonObject which represents the message.
     */
    public static JsonObject win(List<Player> playerList) {
        JsonObject reply = new JsonObject();
        JsonArray winners = new JsonArray();
        for (Player player : playerList) winners.add(player.getName());
        reply.addProperty("type", "win");
        reply.add("winners", (!playerList.isEmpty()) ? winners : JsonNull.INSTANCE);

        return reply;
    }

    /**
     * Creates the "error" message.
     *
     * @param message The message to send to the client.
     * @return JsonObject which represents the message.
     */
    public static JsonObject error(String message) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "error");
        reply.addProperty("message", message);
        return reply;
    }

    /**
     * Creates the "gameCreation" request message.
     *
     * @param playersNumber The players number of the game.
     * @param expert        A boolean parameter which indicates the difficulty of the desired game.
     * @return JsonObject which represents the message.
     */
    public static JsonObject gameCreation(int playersNumber, boolean expert) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "gameCreation");
        command.addProperty("playersNumber", playersNumber);
        command.addProperty("expert", expert);

        return command;
    }

    /**
     * Creates the "enterGame" request message.
     *
     * @param code The game code of the game the player wants to join.
     * @return JsonObject which represents the message.
     */
    public static JsonObject enterGame(String code) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "enterGame");
        command.addProperty("code", code);

        return command;
    }

    /**
     * Creates the "login" request message.
     *
     * @param username The name of the player who wants to join the game.
     * @return JsonObject which represents the message.
     */
    public static JsonObject login(String username) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "login");
        command.addProperty("name", username);

        return command;
    }

    /**
     * Creates the "logout" request message.
     *
     * @return JsonObject which represents the message.
     */
    public static JsonObject logout() {
        JsonObject command = new JsonObject();
        command.addProperty("type", "logout");

        return command;
    }

    /**
     * Creates the "playAssistant" command.
     *
     * @param player      The player who's performing the move.
     * @param assistantId The id of the assistant card played.
     * @return JsonObject which represents the message.
     */
    public static JsonObject playAssistant(String player, int assistantId) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "playAssistant");
        command.addProperty("player", player);
        command.addProperty("assistant", assistantId);

        return command;
    }

    /**
     * Creates the "moveStudent" command.
     *
     * @param player           The player who's performing the move.
     * @param color            The color of the student moved.
     * @param from             The initial position.
     * @param to               The final position.
     * @param fromId           The initial island's id (null if the move doesn't involve islands or characters).
     * @param toId             The final island's id (null if the move doesn't involve islands or characters).
     * @param specialCharacter true if it comes from a special character.
     * @return JsonObject which represents the message.
     */
    public static JsonObject moveStudent(String player, HouseColor color, String from, String to, Integer fromId, Integer toId, boolean specialCharacter) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "move");
        command.addProperty("pawn", "student");
        command.addProperty("player", player);
        command.addProperty("color", color.toString());
        command.addProperty("from", from);
        command.addProperty("to", to);
        if (fromId == null) command.add("fromId", JsonNull.INSTANCE);
        else command.addProperty("fromId", fromId);
        if (toId == null) command.add("toId", JsonNull.INSTANCE);
        else command.addProperty("toId", toId);
        command.addProperty("special", specialCharacter);

        return command;
    }

    /**
     * Creates the "moveMotherNature" command.
     *
     * @param islandId The destination island.
     * @param move     true if mother nature has to be moved, false only if resolving an island as the effect of a special character.
     * @return JsonObject which represents the message.
     */
    public static JsonObject moveMotherNature(int islandId, boolean move) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "move");
        command.addProperty("pawn", "motherNature");
        command.addProperty("island", islandId);
        command.addProperty("move", move);

        return command;
    }

    /**
     * Creates the "payCharacter" command.
     *
     * @param player      The player who's performing the move.
     * @param characterId The paid special character's id.
     * @return JsonObject which represents the message.
     */
    public static JsonObject payCharacter(String player, int characterId) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "pay");
        command.addProperty("player", player);
        command.addProperty("character", characterId);

        return command;
    }

    /**
     * Creates the "refillEntrance" command.
     *
     * @param player  The player who's performing the move.
     * @param cloudId The cloud's id.
     * @return JsonObject which represents the message.
     */
    public static JsonObject refillEntrance(String player, int cloudId) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "refill");
        command.addProperty("player", player);
        command.addProperty("cloud", cloudId);

        return command;
    }

    /**
     * Creates the "ban" command.
     *
     * @param islandId The banned island.
     * @return JsonObject which represents the message.
     */
    public static JsonObject ban(int islandId) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "ban");
        command.addProperty("island", islandId);

        return command;
    }

    /**
     * Creates the "ignore" command.
     *
     * @param color The ignored color.
     * @return JsonObject which represents the message.
     */
    public static JsonObject ignoreColor(String color) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "ignore");
        command.addProperty("color", color);

        return command;
    }

    /**
     * Creates the "return" command.
     *
     * @param color The color to be return.
     * @return JsonObject which represents the message.
     */
    public static JsonObject returnColor(String color) {
        JsonObject command = new JsonObject();
        command.addProperty("type", "command");
        command.addProperty("subtype", "return");
        command.addProperty("color", color);

        return command;
    }
}
