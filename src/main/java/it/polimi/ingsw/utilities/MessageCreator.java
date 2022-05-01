package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.GameController;

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

    public static JsonObject turnEnable(boolean enabled) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "turnEnable");
        reply.addProperty("enable", enabled);
        return reply;
    }

    /*public static JsonObject status(){

    }*/

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
