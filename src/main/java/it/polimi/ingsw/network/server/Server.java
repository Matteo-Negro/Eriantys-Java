package it.polimi.ingsw.network.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.ServerLauncher;
import it.polimi.ingsw.controller.GameController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Server instance.
 */

public class Server {

    private final Map<String, GameController> activeGames;
    private final Map<String, GameController> interrupetdGames;
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
        activeGames = new HashMap<>();
        interrupetdGames = new HashMap<>();
        File directory = new File(this.savePath);
        if (!directory.exists())
            directory.mkdir();
        loadGames();
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
                        return Files.newBufferedReader(file).lines().reduce((s1, s2) -> s1 + s2);
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
     * @param game Json object which contains all the required information to restore the game.
     */
    private void loadGame(JsonObject game) {

    }
}
