package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.utilities.parsers.ObjectsToJson;

/**
 * This class generates JsonObjects for the various communication cases.
 *
 * @author Riccardo Motta
 */
public class MessageCreator {

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
    public static JsonObject status(GameController game){
        JsonObject reply = new JsonObject();
        reply.addProperty("activeUser", game.getActiveUser());
        reply.addProperty("phase", game.getPhase());
        JsonArray subPhaseCompletion = new JsonArray();
        for(String sp : game.getSubPhaseCompletion().keySet()){
            JsonObject subPhase = new JsonObject();
            subPhase.addProperty(sp,game.getSubPhaseCompletion().get(sp));
            subPhaseCompletion.add(subPhase);
        }
        reply.add("subPhaseCompletion", subPhaseCompletion);
        reply.add("players", ObjectsToJson.toJsonArray(game.getGameModel().getPlayers(),ObjectsToJson.GET_PLAYERS));
        reply.add("gameBoard", ObjectsToJson.toJsonObject(game.getGameModel().getGameBoard()));
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
    public static JsonObject win(String player) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "win");
        reply.addProperty("winner", player);
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
}
