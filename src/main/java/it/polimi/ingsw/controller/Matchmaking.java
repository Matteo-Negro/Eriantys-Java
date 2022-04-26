package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;

/**
 * @author Riccardo Milici
 * @author Riccardo Motta
 */
class Matchmaking {

    static String gameCreation(JsonObject command, Server server) {
        return server.addGame(command.get("playersNumber").getAsInt(), command.get("expert").getAsBoolean());
    }

    static GameController enterGame(String code, Server server) throws FullGameException, GameNotFoundException {
        return searchGame(code, server);
    }

    private static GameController searchGame(String gameCode, Server server) throws GameNotFoundException, FullGameException {

        // Search the game
        GameController desiredGame = server.findGame(gameCode);

        if (desiredGame == null)
            throw new GameNotFoundException();

        // Throw an exception if the searched game is already full and active.
        if (desiredGame.isFull())
            throw new FullGameException();

        return desiredGame;
    }

    static boolean login(GameController gameController, String name, User user) {

        if (gameController == null)
            return false;

        try {
            gameController.addUser(name, user);
        } catch (FullGameException | AlreadyExistingPlayerException e) {
            return false;
        }

        return false;
    }
}
