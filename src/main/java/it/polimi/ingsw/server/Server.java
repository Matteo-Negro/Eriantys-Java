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
import java.util.HashMap;
import java.util.Locale;
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
    boolean processRunning;
    ServerSocket serverSocket;
    ServerIO debugIO;

    /**
     * Class constructor.
     *
     * @param savePath Path where to find and store the games.
     * @param port     The port on which the server has to listen for connections.
     * @throws IOException Thrown if there is an error while processing files.
     */
    public Server(String savePath, int port) throws IOException {
        this.processRunning = true;
        this.debugIO = new ServerIO(this);
        this.port = port;
        this.serverSocket = new ServerSocket(port);

        this.savePath = savePath != null
                ? savePath
                : Paths.get(
                new File(ServerLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParent(),
                "database"
        ).toString();
        games = new HashMap<>();
        gameExecutor = Executors.newCachedThreadPool();
        File directory = new File(this.savePath);

        boolean directoryCreated = true;
        if (!directory.exists()) {
            directoryCreated = directory.mkdir();
        }
        if (!directoryCreated) {
            throw new IOException("An error occurred while creating the database directory.");
        }
        Log.info("Loading games...");
        loadGames();
        Log.info("Load completed.");
    }

    /**
     * The main server method, it runs the Server instance as a parallel thread,
     * managing the new connection requests from the clients.
     */
    public void start() {
        new Thread(debugIO).start();
        ExecutorService userExecutor = Executors.newCachedThreadPool();
        try {
            Log.info("Server up and listening on port " + port);

            while (true) {
                synchronized (this.games) {
                    if (!isRunning()) break;
                }
                Socket socket = serverSocket.accept();
                Log.info("Received client connection from " + socket.getRemoteSocketAddress().toString().substring(1));
                userExecutor.submit(new User(socket, this));
            }

        } catch (NoSuchElementException | IOException e) {
            Log.error(e);
        } finally {
            userExecutor.shutdown();
            this.debugIO.close();
            this.debugIO = null;
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

        try {

            Log.info("Loading game \"" + json.get("id").getAsString().toUpperCase() + "\"...");

            Map<String, Player> players = new HashMap<>();

            for (JsonElement player : json.getAsJsonArray("players"))
                players.put(player.getAsJsonObject().get("name").getAsString(), parsePlayer(player.getAsJsonObject()));

            GameController gameController = new GameController(
                    this,
                    json.get("id").getAsString().toUpperCase(Locale.ROOT),
                    new GamePlatform(
                            json.get("expert").getAsBoolean(),
                            parseGameBoard(json.getAsJsonObject("board"), json.get("expert").getAsBoolean(), players),
                            parsePlayersList(json.getAsJsonArray("clockwiseOrder"), players),
                            parsePlayersList(json.getAsJsonArray("turnOrder"), players),
                            json.get("currentPlayer").getAsString()
                    ),
                    json.get("expectedPlayers").getAsInt(),
                    json.get("round").getAsInt(),
                    json.get("phase").getAsString(),
                    json.get("subPhase").getAsString(),
                    players.keySet(),
                    savePath
            );

            gameExecutor.submit(gameController);

            synchronized (games) {
                games.put(json.get("id").getAsString(), gameController);
            }

            Log.info("Loaded " + json.get("id").getAsString());

        } catch (Exception e) {
            Log.warning("Cannot to load " + json.get("id").getAsString().toUpperCase() + " because of the following error", e);
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
        synchronized (games) {
            do id = getNewId();
            while (games.containsKey(id));
            GameController gameController = new GameController(
                    this,
                    id,
                    new GamePlatform(expectedPlayers, expertMode),
                    expectedPlayers,
                    savePath);
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
                id.append((char) (letter + ThreadLocalRandom.current().nextInt(26)));
            else
                id.append((char) (number + ThreadLocalRandom.current().nextInt(10)));
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

    /**
     * Returns true if the server process is currently running.
     *
     * @return The processRunning attribute.
     */
    public boolean isRunning() {
        return this.processRunning;
    }

    /**
     * Closes the server process and saves the games.
     */
    public void shutdown() {
        synchronized (this.games) {
            this.debugIO.close();
            this.processRunning = false;
            for (GameController game : this.games.values()) {
                if (game.isFull()) game.saveGame();
            }
            Log.debug("Games saved.");

            try (Socket endSocket = new Socket()) {
                endSocket.connect(serverSocket.getLocalSocketAddress());
            } catch (IOException ioe) {
                Log.debug("An error occurred while closing server socket, the server didn't shut-down.");
            }
        }
    }
}
