package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.board.Island;
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
     * @param logged A boolean value which indicates if the user has correctly logged to a game or not.
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
     * @param enabled A boolean parameter which indicates the communication permission token.
     * @return JsonObject which represents the message.
     */
    public static JsonObject turnEnable(boolean enabled) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "turnEnable");
        reply.addProperty("enable", enabled);
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
        reply.addProperty("round", game.getRound());
        reply.addProperty("expert", game.getGameModel().isExpert());
        reply.addProperty("activeUser", game.getActiveUser());
        reply.addProperty("phase", game.getPhase());
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
     * @return JsonObject which represents the message.
     */
    public static JsonObject win(List<Player> playerList) {
        JsonObject reply = new JsonObject();
        JsonArray winners = new JsonArray();
        for (Player player : playerList) winners.add(player.getName());
        reply.addProperty("type", "win");
        reply.add("winners", winners);
        return reply;
    }

    /**
     * Creates the "moveTower" message.
     *
     * @param towerColor The color of the moving tower.
     * @param island     The island's id on which the tower is being moved.
     * @return JsonObject which represents the message.
     */
    public static JsonObject moveTower(TowerType towerColor, Island island) {
        JsonObject reply = new JsonObject();
        reply.addProperty("towerColor", towerColor.toString());
        reply.addProperty("island", island.getId());
        return reply;
    }

    /**
     * Creates the "moveProfessor" message.
     *
     * @param professor The color of the professor that is going to be reassigned.
     * @param player    The name of the player to whom the professor is going to be reassigned.
     * @return JsonObject which represents the message.
     */
    public static JsonObject moveProfessor(String professor, String player) {
        JsonObject reply = new JsonObject();
        reply.addProperty("professor", professor);
        reply.addProperty("player", player);
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
     * @param code The gamecode of the game the player wants to join.
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
}
