package it.polimi.ingsw.utilities;

/**
 * Contains all the possible state in which the client can be.
 */
public enum ClientState {

    /**
     * The connection with the server was lost.
     */
    CONNECTION_LOST,

    /**
     * The game was ended.
     */
    END_GAME,

    /**
     * The user wants to exit.
     */
    EXIT,

    /**
     * Creation of a new game.
     */
    GAME_CREATION,

    /**
     * Login with username into a preselected game.
     */
    GAME_LOGIN,

    /**
     * The game is running and everything is ok.
     */
    GAME_RUNNING,

    /**
     * Waiting room befor entering a game (waiting for others to connect).
     */
    GAME_WAITING_ROOM,

    /**
     * Insertion on the game code for entering an already existing game.
     */
    JOIN_GAME,

    /**
     * Main menu of the game, from which the user can choose whether to create a new game or entering an already existing one.
     */
    MAIN_MENU,

    /**
     * Start (or splash) screen which allows to connect to a game server.
     */
    START_SCREEN
}
