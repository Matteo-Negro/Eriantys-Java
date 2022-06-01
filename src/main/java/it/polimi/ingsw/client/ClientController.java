package it.polimi.ingsw.client;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.controller.GameServer;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;
import it.polimi.ingsw.utilities.parsers.CommandParser;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utilities.GameControllerStates.CHOOSE_CLOUD;
import static it.polimi.ingsw.utilities.GameControllerStates.MOVE_MOTHER_NATURE;

public class ClientController {
    private final View view;
    private final Object lock;
    private String userName;
    private String gameCode;
    private GameServer gameServer;
    private GameModel gameModel;
    private ClientStates state;
    private boolean modelUpdated;


    /**
     * Default constructor.
     */
    public ClientController(View view){
        this.state = ClientStates.START_SCREEN;
        this.gameServer = null;
        this.gameModel = null;
        this.modelUpdated = false;
        this.userName = null;
        this.gameCode = null;
        this.lock = new Object();
        this.view = view;
    }

    /**
     * Returns the GameServer instance which represents the network link to the server.
     *
     * @return The gameServer attribute.
     */
    public GameServer getGameServer() {
        return this.gameServer;
    }

    /**
     * Returns the GameModel instance associated to the current game status.
     *
     * @return The gameModel attribute.
     */
    public GameModel getGameModel() {
        return this.gameModel;
    }

    /**
     * Returns the lock used to synchronize multithreading operations.
     *
     * @return The lock attribute.
     */
    public Object getLock() {
        return lock;
    }

    /**
     * Returns the username used in the current game, null if the client is currently not playing a game.
     *
     * @return The userName attribute.
     */
    public String getUserName() {
        return this.userName;
    }

    public boolean hasCommunicationToken() {
        return this.getGameModel().getPlayerByName(getUserName()).isActive();
    }

    /**
     * Returns the game code associated to the joined game, null if the user hasn't already joined one.
     *
     * @return The gameCode attribute.
     */
    public String getGameCode() {
        return this.gameCode;
    }

    /**
     * Sets the gameCode to the given value.
     *
     * @param gameCode The code associated to the game which the user is attempting join.
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * Sets the game server.
     *
     * @param server The GameServer instance to set.
     */
    public void setGameServer(GameServer server) {
        this.gameServer = server;
    }

    /**
     * Manages the start-screen logic.
     */
    public void manageStartScreen(Socket hostSocket) {
        try {
            hostSocket.setSoTimeout(10000);
            setGameServer(new GameServer(hostSocket, this));
            new Thread(this.gameServer).start();
            setClientState(ClientStates.MAIN_MENU);
            view.updateScreen(false);
        } catch (IOException | NumberFormatException e) {
            this.errorOccurred("Wrong data provided or server unreachable.");
        }
    }

    /**
     * Manages the main-menu logic.
     */
    public void manageMainMenu(String option) {
        switch (option) {
            case "1" -> {
                this.setClientState(ClientStates.GAME_CREATION);
                view.updateScreen(false);
            }
            case "2" -> {
                this.setClientState(ClientStates.JOIN_GAME);
                view.updateScreen(false);
            }
            default -> this.errorOccurred("Wrong command.");
        }
    }

    /**
     * Manages the game-creation logic.
     */
    public void manageGameCreation(JsonObject command) {
        this.gameServer.sendCommand(command);

        this.tryConnection();

        if (this.getClientState().equals(ClientStates.GAME_CREATION))
            this.setClientState(ClientStates.CONNECTION_LOST);
    }

    /**
     * Manages the join-game logic.
     */
    public void manageJoinGame(String gameCode) {
        if ("EXIT".equals(gameCode)) {
            this.setClientState(ClientStates.MAIN_MENU);
            view.updateScreen(false);
            this.resetGame();
            return;
        }
        this.gameServer.sendCommand(MessageCreator.enterGame(gameCode));
        this.setGameCode(gameCode);

        this.tryConnection();

        if (this.getClientState().equals(ClientStates.JOIN_GAME)) {
            this.errorOccurred("The desired game doesn't exist or is full.");
        }
    }

    /**
     * Manages the login logic.
     */
    public void manageGameLogin(String username) {
        if ("exit".equals(username)) {
            this.setClientState(ClientStates.MAIN_MENU);
            view.updateScreen(false);
            this.resetGame();
            return;
        }
        if (this.getGameModel().getWaitingRoom().containsKey(username) && this.getGameModel().getWaitingRoom().get(username).equals(true) || username.equals("")) {
            this.errorOccurred("Invalid username.");
            return;
        }

        this.userName = username;

        this.gameServer.sendCommand(MessageCreator.login(username));

        this.tryConnection();
    }

    /**
     * Manages the game logic.
     */
    public void manageGameRunning(String command) {
        List<JsonObject> messages = new ArrayList<>();

        if (this.hasCommunicationToken()) {
            // Logout command.
            if (command.equals("exit") || command.equals("logout")) {
                this.getGameServer().sendCommand(MessageCreator.logout());
                this.resetGame();
                view.updateScreen(false);
                return;
            }
            view.updateScreen(false);

            // Another player disconnected.
            if (getGameModel() == null)
                return;

            if (command.contains("info")) {
                Pair<String, String> message = CommandParser.infoGenerator(command);
                view.updateScreen(false);
                view.printInfo(message);
                return;
            }

            //Command parsing and check.
            try {
                messages.addAll(CommandParser.commandManager(command, gameModel.getPlayers()));
            } catch (Exception e) {
                Log.warning(e);
                view.updateScreen(false);
                this.errorOccurred("Wrong command.");
                return;
            }
            if (messages.isEmpty())
                return;
            try {
                for (JsonObject message : messages) {
                    if (checkMessage(message)) {
                        getGameServer().sendCommand(message);
                        synchronized (this.lock) {
                            try {
                                this.getLock().wait();
                            } catch (InterruptedException ie) {
                                view.updateScreen(false);
                                errorOccurred("Command not allowed.");
                                return;
                            }
                        }
                        Log.debug("Command sent to game server.");
                    } else {
                        view.updateScreen(false);
                        errorOccurred("Command not allowed.");
                        return;
                    }
                }
                view.updateScreen(false);
            } catch (IllegalActionException iae) {
                view.updateScreen(false);
                errorOccurred("Connection lost.");
                setClientState(ClientStates.CONNECTION_LOST);
            }
        }
    }

    /**
     * Manages end-game logic.
     */
    public void manageEndGame(String command) {
        if (command.equals("exit")) {
            this.setClientState(ClientStates.MAIN_MENU);
            view.updateScreen(false);
            this.resetGame();
            return;
        } else {
            this.errorOccurred("Wrong command.");
        }
        view.updateScreen(false);
    }

    /**
     * Checks the message validity.
     *
     * @param message The message to check.
     * @return True if the message is correct, false otherwise.
     */
    private boolean checkMessage(JsonObject message) throws IllegalActionException {

        if (this.getGameModel().getPhase().equals(Phase.PLANNING)) {
            try {
                checkAssistant(message);
            } catch (IllegalMoveException ime) {
                return false;
            }
        } else {
            try {
                switch (message.get("subtype").getAsString()) {
                    case "move" -> {
                        switch (message.get("pawn").getAsString()) {
                            case "student" -> checkStudentMove(message);
                            case "motherNature" -> checkMotherNatureMove(message);
                        }
                    }
                    case "pay" -> checkCharacterPayment(message);
                    case "refill" -> checkEntranceRefill(message);
                    default -> {
                        return false;
                    }
                }
            } catch (IllegalMoveException ime) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the assistant play request is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkAssistant(JsonObject message) throws IllegalMoveException {
        if (!message.get("subtype").getAsString().equals("playAssistant")) throw new IllegalMoveException();
        else {
            int assistantId = message.get("assistant").getAsInt();
            if (!message.get("subtype").getAsString().equals("playAssistant") || getGameModel().getPlayerByName(this.getUserName()).getAssistantById(assistantId) == null)
                throw new IllegalMoveException();
            for (Player p : getGameModel().getPlayers()) {
                if (p.getCurrentPlayedAssistant() != null && assistantId == p.getCurrentPlayedAssistant().getId())
                    throw new IllegalMoveException();
            }
        }
    }

    /**
     * Checks if the student move request is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException   Thrown if the requested move is illegal.
     * @throws IllegalActionException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkStudentMove(JsonObject message) throws IllegalMoveException, IllegalActionException {
        synchronized (this.getGameModel()) {
            switch (getGameModel().getSubphase()) {
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3 -> {
                }
                case MOVE_STUDENT_4 -> {
                    if (getGameModel().getPlayersNumber() != 3) throw new IllegalActionException();
                }
                default -> {
                    if (getGameModel().getSubphase().equals(CHOOSE_CLOUD) || !getGameModel().isExpert() || getGameModel().isExpert() && !getGameModel().isMovementEffectActive())
                        throw new IllegalMoveException();
                }
            }

            String from = message.get("from").getAsString();
            String to = message.get("to").getAsString();

            if (from.equals(to)) throw new IllegalMoveException();
            if (!(message.get("fromId") instanceof JsonNull) && !(message.get("toId") instanceof JsonNull) && message.get("fromId").getAsString().equals(message.get("toId").getAsString()))
                throw new IllegalMoveException();

            switch (from) {
                case "entrance" -> {
                    if (getGameModel().getPlayerByName(this.getUserName()).getSchoolBoard().getEntrance().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                        throw new IllegalMoveException();
                }
                case "diningRoom" -> {
                    if (!to.equals("entrance") || !getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(10) != null || !getGameModel().getGameBoard().getSpecialCharacterById(10).isActive())
                        throw new IllegalMoveException();
                    if (getGameModel().getPlayerByName(this.getUserName()).getSchoolBoard().getDiningRoom().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                        throw new IllegalMoveException();
                }
                case "character" -> {
                    if (!to.equals("entrance") && !to.equals("diningRoom") && !to.equals("island"))
                        throw new IllegalMoveException();

                    SpecialCharacter involvedCharacter = getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt());
                    if (involvedCharacter == null || !involvedCharacter.isActive() || involvedCharacter.getStudents() == null || involvedCharacter.getStudents().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                        throw new IllegalMoveException();
                }
                default -> throw new IllegalMoveException();
            }

            switch (to) {
                case "entrance" -> {
                    if (!from.equals("character") && (!getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(10) != null || !getGameModel().getGameBoard().getSpecialCharacterById(10).isActive()))
                        throw new IllegalMoveException();
                    if (message.get("fromId") instanceof JsonNull || !getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt()).isActive() || getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt()) == null)
                        throw new IllegalMoveException();
                }
                case "character" -> {
                    if (message.get("toId").getAsInt() != 7 || !getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(7) == null || !getGameModel().getGameBoard().getSpecialCharacterById(7).isActive())
                        throw new IllegalMoveException();
                }
                case "island" -> {
                    int destinationIndex = message.get("toId").getAsInt();
                    while (getGameModel().getGameBoard().getIslands().get(destinationIndex).hasPrev()) {
                        destinationIndex = (destinationIndex - 1) % 12;
                    }
                    message.remove("toId");
                    message.addProperty("toId", destinationIndex);
                }
            }
        }
    }

    /**
     * Checks if the mother nature move is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkMotherNatureMove(JsonObject message) throws IllegalMoveException {
        if (!getGameModel().getSubphase().equals(MOVE_MOTHER_NATURE)) throw new IllegalMoveException();
        int finalIsland = message.get("island").getAsInt();
        int maxDistance = getGameModel().getPlayerByName(this.getUserName()).getCurrentPlayedAssistant().getMaxDistance();

        int motherNatureIsland = getGameModel().getGameBoard().getMotherNatureIsland();
        int motherNatureIslandSize = 1;

        int i = motherNatureIsland;
        while (getGameModel().getGameBoard().getIslands().get(i).hasNext()) {
            motherNatureIslandSize++;
            i = (i + 1) % 12;
        }

        int distanceWanted;
        if (finalIsland < motherNatureIsland)
            distanceWanted = finalIsland + 12 - motherNatureIsland - (motherNatureIslandSize - 1);
        else distanceWanted = finalIsland - motherNatureIsland - (motherNatureIslandSize - 1);
        if (distanceWanted > maxDistance) throw new IllegalMoveException();
    }

    /**
     * Checks if the chosen special character payment is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkCharacterPayment(JsonObject message) throws IllegalMoveException {
        if (getGameModel().getSubphase().equals(MOVE_MOTHER_NATURE) || !getGameModel().isExpert())
            throw new IllegalMoveException();

        int characterId = message.get("character").getAsInt();
        if (!getGameModel().isExpert()) throw new IllegalMoveException();

        if (getGameModel().getGameBoard().getSpecialCharacterById(characterId) == null || getGameModel().getGameBoard().getSpecialCharacterById(characterId).isActive() || getGameModel().getGameBoard().getSpecialCharacterById(characterId).isPaidInRound())
            throw new IllegalMoveException();

        if (getGameModel().getPlayerByName(this.getUserName()).getCoins() < getGameModel().getGameBoard().getSpecialCharacterById(characterId).getCost())
            throw new IllegalMoveException();
    }

    /**
     * Checks if the refill from the chosen cloud is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkEntranceRefill(JsonObject message) throws IllegalMoveException {
        if (!getGameModel().getSubphase().equals(CHOOSE_CLOUD))
            throw new IllegalMoveException();

        if (getGameModel().getGameBoard().getClouds().get(message.get("cloud").getAsInt()).getStudents(true) == null)
            throw new IllegalMoveException();
    }

    /**
     * Manages the CONNECTION_LOST state of the client, terminating all the server-associated threads.
     */
    public void manageConnectionLost() {
        this.resetGame();
        this.getGameServer().disconnected();
        this.gameServer = null;
        this.errorOccurred("Connection lost.");
        this.setClientState(ClientStates.START_SCREEN);
    }

    /**
     * Sets the gameModel attribute to a new GameModel instance.
     *
     * @param newGameModel The new GameModel instance.
     */
    public void initializeGameModel(GameModel newGameModel) {
        synchronized (this.lock) {
            this.gameModel = newGameModel;
            if (newGameModel != null) {
                this.modelUpdated = true;
                this.lock.notifyAll();
            }
        }
    }

    /**
     * Returns the current client's state.
     *
     * @return The state attribute.
     */
    public ClientStates getClientState() {
        return this.state;
    }

    /**
     * Sets the current client's state to a new value.
     *
     * @param newState The next state to set.
     */
    public void setClientState(ClientStates newState) {
        synchronized (this.lock) {
            this.state = newState;
            this.lock.notify();
        }

    }

    /**
     * Flushes the current game model and sets the current state to MAIN_MENU.
     */
    public void resetGame() {
        this.setClientState(ClientStates.MAIN_MENU);
        this.gameModel = null;
        this.userName = null;
        this.gameCode = null;
    }

    /**
     * Prints an error message.
     *
     * @param message The message to print.
     */
    public void errorOccurred(String message) {
        view.printError(message);
        Log.warning(message);
        synchronized (this.lock) {
            try {
                this.lock.wait(1500);
            } catch (InterruptedException e) {
                this.setClientState(ClientStates.CONNECTION_LOST);
                view.updateScreen(false);
            }
        }
    }

    /**
     * Waits for a response from the game server; if the timeout expires, the CONNECTION_LOST state is set.
     */
    private void tryConnection() {
        synchronized (this.lock) {
            try {
                this.lock.wait(10000);
            } catch (InterruptedException e) {
                this.setClientState(ClientStates.CONNECTION_LOST);
                view.updateScreen(false);
            }
        }
    }
}
