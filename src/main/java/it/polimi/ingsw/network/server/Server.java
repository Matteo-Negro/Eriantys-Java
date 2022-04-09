package it.polimi.ingsw.network.server;

import it.polimi.ingsw.ServerLauncher;
import it.polimi.ingsw.controller.GameController;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Map<String, GameController> activeGames;
    private final Map<String, GameController> interrupetdGames;
    private final String savePath;

    public Server(String savePath) {
        this.savePath = savePath != null
                ? savePath
                : Paths.get(new File(ServerLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile())
                .getParent(), "database").toString();
        activeGames = new HashMap<>();
        interrupetdGames = new HashMap<>();
        File directory = new File(this.savePath);
        if (!directory.exists())
            directory.mkdir();
        loadGames(directory);
    }

    private void loadGames(File directory) {
        // TODO: load previous games into interruptedGames
    }

}
