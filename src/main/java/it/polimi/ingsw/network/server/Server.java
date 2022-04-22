package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.ServerLauncher;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GamePlatform;
import it.polimi.ingsw.model.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static it.polimi.ingsw.utilities.StateParser.*;

/**
 * Server instance.
 */

public class Server {

    private final Map<String, GameController> games;
    private final String savePath;

    /**
     * Class constructor.
     *
     * @param savePath Path where to find and store the games.
     * @throws IOException Thrown if there is an error while processing files.
     */
    public Server(String savePath) throws IOException {
        this.savePath = savePath != null
                ? savePath
                : Paths.get(new File(ServerLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile())
                .getParent(), "database").toString();
        games = new HashMap<>();
        File directory = new File(this.savePath);
        if (!directory.exists())
            directory.mkdir();

        System.out.println("Loading games...");
        loadGames();
        System.out.println("Load completed.\n");
    }

    /**
     * Loads the previous games from the disk.
     *
     * @throws IOException Thrown if there is an error while processing files.
     */
    private void loadGames() throws IOException {
        Files.list(Paths.get(savePath))
                .parallel()
                .filter(file -> !Files.isDirectory(file) && file.toString().endsWith(".json"))
                .map(file -> {
                    try {
                        BufferedReader stream = Files.newBufferedReader(file);
                        Optional<String> json = stream.lines().reduce((s1, s2) -> s1 + s2);
                        stream.close();
                        return json;
                    } catch (IOException e) {
                        return Optional.<String>empty();
                    }
                })
                .filter(Optional::isPresent)
                .forEach(json -> loadGame(new Gson().fromJson(json.get(), JsonObject.class)));
    }

    /**
     * Creates all the required data structures for a specific game.
     *
     * @param json Json object which contains all the required information to restore the game.
     */
    private void loadGame(JsonObject json) {

        System.out.println("Loading game \"" + json.get("id").getAsString() + "\"");

        Map<String, Player> players = new HashMap<>();
        GameController gameController;

        for (JsonElement player : json.getAsJsonArray("players"))
            players.put(player.getAsJsonObject().get("name").getAsString(), parsePlayer(player.getAsJsonObject()));

        gameController = new GameController(new GamePlatform(
                json.get("expert").getAsBoolean(),
                parseGameBoard(json.getAsJsonObject("board"), json.get("expert").getAsBoolean(), players),
                parsePlayersList(json.getAsJsonArray("clockwiseOrder"), players),
                parsePlayersList(json.getAsJsonArray("turnOrder"), players),
                json.get("currentPlayer").getAsString()
        ), json.get("expectedPlayers").getAsInt(), savePath);

        synchronized (games) {
            games.put(json.get("id").getAsString(), gameController);
        }
    }

    /**
     * Adds a new game to the list of games.
     *
     * @param expectedPlayers Number of players expected to play the game.
     * @param expertMode      Tells if the desired game has to be played in expert mode.
     * @return The string of the newly created game.
     */
    public String addGame(int expectedPlayers, boolean expertMode) {
        String id;
        GameController gameController = new GameController(new GamePlatform(expectedPlayers, expertMode), expectedPlayers, savePath);
        synchronized (games) {
            do id = getNewId();
            while (games.containsKey(id));
            games.put(id, gameController);
        }
        return id;
    }

    /**
     * Generates a random identifier for a new game.
     *
     * @return The generated identifier.
     */
    private String getNewId() {
        StringBuilder id = new StringBuilder();
        int letter = 'A';
        int number = '0';
        for (int index = 0; index < 5; index++)
            if (ThreadLocalRandom.current().nextBoolean())
                id.append((char) (letter + ThreadLocalRandom.current().nextInt(0, 26)));
            else
                id.append((char) (number + ThreadLocalRandom.current().nextInt(0, 10)));
        return id.toString();
    }

    /**
     * Gets the GameController associated with a specific game identifier.
     *
     * @param id The game identifier.
     * @return GameController associated with a specific game identifier.
     */
    public GameController findGame(String id) {
        return games.get(id);
    }
}
