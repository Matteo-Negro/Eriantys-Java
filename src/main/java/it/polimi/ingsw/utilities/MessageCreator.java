package it.polimi.ingsw.utilities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.GameController;

public class MessageCreator {

    public static JsonObject gameCreation(String code) {
        JsonObject replay = new JsonObject();
        replay.addProperty("type", "gameCreation");
        replay.addProperty("code", code);
        return replay;
    }

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

    public static JsonObject login(boolean logged) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "login");
        reply.addProperty("success", logged);
        return reply;
    }

    public static JsonObject error(String message) {
        JsonObject reply = new JsonObject();
        reply.addProperty("type", "error");
        reply.addProperty("message", message);
        return reply;
    }
}
