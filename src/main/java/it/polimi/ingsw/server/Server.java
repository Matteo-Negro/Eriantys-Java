package it.polimi.ingsw.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.ServerLauncher;
import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.controller.User;
import it.polimi.ingsw.server.model.GamePlatform;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static it.polimi.ingsw.utilities.parsers.JsonToObjects.*;

/**
 * Server instance.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 */

public class Server {

    private final Map<String, GameController> games;
    private final String savePath;
    private final int port;

    private final ExecutorService gameExecutor;

    /**
     * Class constructor.
     *
     * @param savePath Path where to find and store the games.
     * @throws IOException Thrown if there is an error while processing files.
     */
    public Server(String savePath, int port) throws IOException {
        this.port = port;
        this.savePath = savePath != null
                ? savePath
                : Paths.get(
                        new File(ServerLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent(),
                "database"
        ).toString();
        games = new HashMap<>();
        gameExecutor = Executors.newCachedThreadPool();
        File directory = new File(this.savePath);
        if (!directory.exists())
            directory.mkdir();

        Log.info("Loading games...");
        loadGames();
        Log.info("Load completed.");
    }

    public void start() throws IOException {

        ExecutorService userExecutor = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Log.info("Server up and listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                Log.info("Received client connection from " + socket.getRemoteSocketAddress().toString().substring(1));
                userExecutor.submit(new User(socket, this));
            }
        } catch (NoSuchElementException e) {
            Log.error(e);
        } finally {
            userExecutor.shutdown();
            Log.info("Socket closed.");
        }
    }

    /**
     * Loads the previous games from the disk.
     *
     * @throws IOException Thrown if there is an error while processing files.
     */
    private void loadGames() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(savePath)).parallel()) {
            stream.filter(file -> !Files.isDirectory(file) && file.toString().endsWith(".json"))
                    .forEach(file -> {
                        try (BufferedReader bufferedReader = Files.newBufferedReader(file)) {
                            loadGame(JsonParser.parseReader(bufferedReader).getAsJsonObject());
                        } catch (IOException e) {
                            Log.error(e);
                        }
                    });
        }
    }

    /**
     * Creates all the required data structures for a specific game.
     *
     * @param json Json object which contains all the required information to restore the game.
     */
    private void loadGame(JsonObject json) {

        Log.info("Loading game \"" + json.get("id").getAsString() + "\"...");

        Map<String, Player> players = new HashMap<>();
        GameController gameController;

        for (JsonElement player : json.getAsJsonArray("players"))
            players.put(player.getAsJsonObject().get("name").getAsString(), parsePlayer(player.getAsJsonObject()));

        gameController = new GameController(json.get("id").getAsString(), new GamePlatform(
                json.get("expert").getAsBoolean(),
                parseGameBoard(json.getAsJsonObject("board"), json.get("expert").getAsBoolean(), players),
                parsePlayersList(json.getAsJsonArray("clockwiseOrder"), players),
                parsePlayersList(json.getAsJsonArray("turnOrder"), players),
                json.get("currentPlayer").getAsString()
        ), json.get("expectedPlayers").getAsInt(), savePath);

        gameExecutor.submit(gameController);

        synchronized (games) {
            games.put(json.get("id").getAsString(), gameController);
        }
        Log.info("Loaded " + json.get("id").getAsString());
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
        synchronized (games) {
            do id = getNewId();
            while (games.containsKey(id));
            GameController gameController = new GameController(id, new GamePlatform(expectedPlayers, expertMode), expectedPlayers, savePath);
            gameExecutor.submit(gameController);
            games.put(id, gameController);
        }
        Log.info("Created new game with id " + id);
        return id;
    }

    /**
     * Removes a game from the map. This method has to be called when the game ends.
     *
     * @param id The id of the game to remove.
     */
    public void removeGame(String id) {
        synchronized (games) {
            games.get(id).interrupt();
            games.remove(id);
        }

        Log.info("Deleted the game with id: " + id);
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
        synchronized (games) {
            return games.get(id);
        }
    }
}
