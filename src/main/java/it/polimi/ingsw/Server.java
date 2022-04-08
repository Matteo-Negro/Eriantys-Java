package it.polimi.ingsw;

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
                : Paths.get(new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getFile())
                .getParent(), "database").toString();
        activeGames = new HashMap<>();
        interrupetdGames = new HashMap<>();
        File directory = new File(this.savePath);
        if (!directory.exists())
            directory.mkdir();
        loadGames(directory);
    }

    public static void main(String[] args) {
        Server server = new Server(args.length == 1 ? args[0] : null);
    }

    private void loadGames(File directory) {
        // TODO: load previous games into interruptedGames
    }
}
