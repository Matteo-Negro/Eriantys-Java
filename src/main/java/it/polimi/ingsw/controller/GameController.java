package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.GamePlatform;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.parsers.ObjectsToJson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameController extends Thread {
    private final GamePlatform gameModel;
    private String id;
    private int round;
    private String phase;
    private final int expectedPlayers;
    private int connectedPlayers;
    private final Map<String, User> users;
    private final String savePath;

    public GameController(GamePlatform gameModel, int expectedPlayers, String savePath) {
        this.gameModel = gameModel;
        this.expectedPlayers = expectedPlayers;
        this.connectedPlayers = 0;
        this.users = new HashMap<>();
        this.savePath = savePath;
    }

    public String getGameId() {
        return id;
    }

    public int getRound() {
        return round;
    }

    public String getPhase() {
        return phase;
    }

    public Set<User> getUsers() {
        synchronized (users) {
            return new HashSet<>(users.values());
        }
    }

    public User getUser(String name) {
        synchronized (users) {
            return users.get(name);
        }
    }

    public Set<String> getUsernames() {
        synchronized (users) {
            return users.keySet();
        }
    }

    public void addUser(String name, User user) throws FullGameException, AlreadyExistingPlayerException {

        if (isFull())
            throw new FullGameException();

        synchronized (users) {

            if (users.get(name) != null)
                throw new AlreadyExistingPlayerException();

            users.put(name, user);
            connectedPlayers++;
        }

        // TODO: create player into model
        // TODO: notify game start if full
    }

    public void removeUser(User user) {
        synchronized (users) {
            String username = getUsername(user);
            if (username == null)
                return;
            users.replace(username, null);
            connectedPlayers--;
            // TODO: pause game (notifyAll())
        }
    }

    public String getUsername(User user) {
        return users.entrySet().stream().filter(entry -> entry.getValue().equals(user)).findFirst().map(Map.Entry::getKey).orElse(null);
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    public boolean isFull() {
        synchronized (users) {
            return connectedPlayers == expectedPlayers;
        }
    }

    public GamePlatform getGameModel() {
        return gameModel;
    }

//    public void manageCommand(JsonObject command) {
//
//    }

    /**
     * Saves the current state of the game into a file.
     */
    private void saveGame() {

        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        json.addProperty("expert", gameModel.isExpert());
        json.addProperty("currentPlayer", gameModel.getCurrentPlayer().getName());
        json.add("clockwiseOrder", ObjectsToJson.toJsonArray(gameModel.getPlayers(), ObjectsToJson.GET_NAMES));
        json.add("turnOrder", ObjectsToJson.toJsonArray(gameModel.getTurnOrder(), ObjectsToJson.GET_NAMES));
        json.addProperty("expectedPlayers", expectedPlayers);
        json.add("players", ObjectsToJson.toJsonArray(gameModel.getPlayers(), ObjectsToJson.GET_PLAYERS));
        json.add("board", ObjectsToJson.toJsonObject(gameModel.getGameBoard()));

        new Thread(() -> {
            try {
                writeFile(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Writes the json containing the state into a file.
     *
     * @param json The json to write.
     * @throws IOException If there is an exception during the process.
     */
    private void writeFile(JsonObject json) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(savePath, id + ".json"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        writer.write(json.toString());
        writer.close();
    }

    private void setIsRunning() {

    }

    private void playAssistantCard(String player, int assistant) {

    }

    private void moveStudent(HouseColor color, String from, String to) {

    }

    private void moveMotherNature(int moves) {

    }

    private void chooseCloud(int cloud) {

    }

    private void paySpecialCharacter(int specialCharacter) {

    }

//    private boolean checkWinContitions() {
//
//    }

    private void endGame() {

    }
}
