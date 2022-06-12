package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;

/**
 * This class manages all the client requests for the setup of the game (out of game).
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 */
class Matchmaking {

    private Matchmaking() {
    }

    /**
     * Calls the gameServer's "addGame(int expectedPlayers, boolean expertMode)" method, in order to create the game requested by the user.
     *
     * @return The alphanumerical code associated with the created game.
     */
    static String gameCreation(JsonObject command, Server server) {
        return server.addGame(command.get("playersNumber").getAsInt(), command.get("expert").getAsBoolean());
    }

    /**
     * Searches for the requested game. This has to be called before login, to eventually get GameController for that game.
     *
     * @param code   The code of the game to enter.
     * @param server The server reference for adding the user.
     * @return If the game is found, returns the GameController instance.
     * @throws FullGameException     Thrown if the selected game is already full.
     * @throws GameNotFoundException Thrown if the requested game cannot be found.
     */
    static GameController enterGame(String code, Server server) throws FullGameException, GameNotFoundException {

        // Search the game
        GameController desiredGame = server.findGame(code);

        if (desiredGame == null)
            throw new GameNotFoundException();

        // Throw an exception if the searched game is already full and active.
        if (desiredGame.isFull())
            throw new FullGameException();

        return desiredGame;
    }

    /**
     * Manages the process regarding the login of the user to a specific game.
     *
     * @param gameController The instance of the game in which the user wants to log in.
     * @param name           The name of the player to interpretate.
     * @param user           The user to add to the game.
     */
    static boolean login(GameController gameController, String name, User user) {
        if (gameController == null) {
            return false;
        }
        try {
            gameController.addUser(name, user);
            return true;
        } catch (FullGameException | AlreadyExistingPlayerException e) {
            return false;
        }
    }
}
