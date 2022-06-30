package it.polimi.ingsw.client.controller;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
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

import static it.polimi.ingsw.utilities.GameControllerState.CHOOSE_CLOUD;
import static it.polimi.ingsw.utilities.GameControllerState.MOVE_MOTHER_NATURE;

/**
 * This is the client controller, it runs the main client's finite state machine.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class ClientController {
    private final View view;
    private final Object lock;
    private String userName;
    private String gameCode;
    private GameServer gameServer;
    private GameModel gameModel;
    private EndType endState;
    private ClientState state;
    private boolean replyArrived;

    /**
     * Default class constructor.
     *
     * @param view The connected view.
     */
    public ClientController(View view) {
        this.state = ClientState.START_SCREEN;
        this.gameServer = null;
        this.gameModel = null;
        this.userName = null;
        this.gameCode = null;
        this.endState = null;
        this.lock = new Object();
        this.view = view;
        this.replyArrived = false;
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
     * Sets the game server.
     *
     * @param server The GameServer instance to set.
     */
    public void setGameServer(GameServer server) {
        this.gameServer = server;
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
     * Sets the replyArrived attribute to true when the server gives a response.
     */
    public void setReplyArrived() {
        this.replyArrived = true;
    }

    /**
     * Returns the username used in the current game, null if the client is currently not playing a game.
     *
     * @return The userName attribute.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns true if this client has got the communication-enable token.
     *
     * @return The active status associated with the username of this client, saved into the game model.
     */
    public boolean hasCommunicationToken() {
        if (this.getGameModel() == null) return false;
        return this.getGameModel().getPlayerByName(getUserName()).isActive();
    }

    /**
     * Returns the states which indicates the final result of a game (WON, LOST or DRAW).
     *
     * @return The endState attribute.
     */
    public EndType getEndState() {
        return this.endState;
    }

    /**
     * Sets the endState attribute to the specified state.
     *
     * @param end The state to set.
     */
    public void setEndState(EndType end) {
        this.endState = end;
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
     * Sets the gameCode to the given second.
     *
     * @param gameCode The code associated to the game which the user is attempting join.
     */
    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * Manages the start-screen logic.
     *
     * @param hostSocket The socket on which communicate.
     */
    public void manageStartScreen(Socket hostSocket) {
        try {
            hostSocket.setSoTimeout(10000);
            setGameServer(new GameServer(hostSocket, this));
            new Thread(this.gameServer).start();
            setClientState(ClientState.MAIN_MENU);
            updateScreen();
        } catch (IOException | NumberFormatException e) {
            this.errorOccurred("Wrong data provided or server unreachable.");
        }
        updateScreen();
    }

    /**
     * Manages the main-menu logic.
     *
     * @param option The selected option.
     */
    public void manageMainMenu(String option) {
        switch (option) {
            case "1" -> {
                if (!getClientState().equals(ClientState.CONNECTION_LOST))
                    this.setClientState(ClientState.GAME_CREATION);
                updateScreen();
            }
            case "2" -> {
                if (!getClientState().equals(ClientState.CONNECTION_LOST)) this.setClientState(ClientState.JOIN_GAME);
                updateScreen();
            }
            case "exit" -> this.setClientState(ClientState.CONNECTION_LOST);
            default -> this.errorOccurred("Wrong command.");
        }
    }

    /**
     * Manages the game-creation logic.
     *
     * @param command The command to send to the user.
     */
    public void manageGameCreation(JsonObject command) {
        this.gameServer.sendCommand(command);

        this.tryConnection();

        if (this.getClientState().equals(ClientState.GAME_CREATION)) {
            Log.debug("manage game creation");
            this.setClientState(ClientState.CONNECTION_LOST);
        }
        updateScreen();
    }

    /**
     * Manages the join-game logic.
     *
     * @param gameCode The code of the game.
     */
    public void manageJoinGame(String gameCode) {
        if ("EXIT".equals(gameCode)) {
            if (!getClientState().equals(ClientState.CONNECTION_LOST)) this.setClientState(ClientState.MAIN_MENU);
            this.resetGame();
            updateScreen();
            return;
        }
        this.gameServer.sendCommand(MessageCreator.enterGame(gameCode));
        this.setGameCode(gameCode);

        this.tryConnection();

        if (this.getClientState().equals(ClientState.JOIN_GAME))
            this.errorOccurred("The desired game doesn't exist or is full.");
        else {
            updateScreen();
        }
    }

    /**
     * Manages the login logic.
     *
     * @param username The username of the player.
     */
    public void manageGameLogin(String username) {
        if ("exit".equals(username)) {
            if (!getClientState().equals(ClientState.CONNECTION_LOST)) this.setClientState(ClientState.MAIN_MENU);
            this.resetGame();
            updateScreen();
            return;
        }

        if (this.getGameModel().getWaitingRoom().containsKey(username) && this.getGameModel().getWaitingRoom().get(username).equals(true) || this.getGameModel().getWaitingRoom().keySet().size() == this.getGameModel().getPlayersNumber() && !this.getGameModel().getWaitingRoom().containsKey(username) || username.equals("")) {
            updateScreen();
            this.errorOccurred("Invalid username.");
            return;
        }

        this.userName = username;
        this.gameServer.sendCommand(MessageCreator.login(username));
        this.tryConnection();
        updateScreen();
    }

    /**
     * Manages the game logic.
     *
     * @param command The command received from the user.
     */
    public void manageGameRunning(String command) {

        if (getClientState().equals(ClientState.CONNECTION_LOST)) {
            updateScreen();
            return;
        }

        if (!CommandParser.checker(command) || command.equals("")) {
            Log.warning("Wrong command.");
            updateScreen();
            this.errorOccurred("Wrong command.");
            return;
        }

        // Logout command.
        if (command.equals("exit") || command.equals("logout")) {
            this.getGameServer().sendCommand(MessageCreator.logout());
            this.resetGame();
            updateScreen();
            return;
        }

        // Another player disconnected.
        if (getGameModel() == null) return;

        if (command.contains("info")) {
            Pair<String, String> message = CommandParser.infoGenerator(command);
            updateScreen();
            view.showInfo(message);
            return;
        }

        //Command parsing and check.
        List<JsonObject> messages;
        messages = new ArrayList<>(CommandParser.commandManager(command, this.gameModel.getPlayers()));
        if (messages.isEmpty()) return;

        //Sending messages to the server.
        try {
            sendCommandsToServer(messages);
        } catch (IllegalActionException iae) {
            Log.warning(iae);
            errorOccurred("Connection lost.");
            setClientState(ClientState.CONNECTION_LOST);
        } catch (IllegalMoveException ime) {
            updateScreen();
            errorOccurred("Command not allowed.");
            return;
        } catch (InterruptedException ie) {
            updateScreen();
            Thread.currentThread().interrupt();
            errorOccurred("Command not allowed.");
            return;
        }
        updateScreen();
    }

    /**
     * Checks the messages and sends them to the server.
     *
     * @param messages The messages to send.
     * @throws IllegalActionException Thrown if a message doesn't pass the check.
     * @throws InterruptedException   Thrown if a thread interruption occurs.
     */
    private void sendCommandsToServer(List<JsonObject> messages) throws IllegalActionException, InterruptedException, IllegalMoveException {
        for (JsonObject message : messages) {
            if (checkMessage(message)) {
                try {
                    if (this.gameModel.isExpert()) checkOccurrences(message);
                } catch (IllegalMoveException e) {
                    Log.warning(e);
                    updateScreen();
                    this.errorOccurred("This is an illegal move.");
                    return;
                }

                synchronized (this.lock) {
                    this.replyArrived = false;
                    getGameServer().sendCommand(message);
                    while (!this.replyArrived)
                        this.getLock().wait();
                }
                Log.debug("Command sent to game server.");
            } else throw new IllegalMoveException();
        }
    }

    /**
     * This method is a helper for 'manageGameRunning', check occurrences in specialCharacter.
     *
     * @param message The message has to be checked.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkOccurrences(JsonObject message) throws IllegalMoveException {
        if (this.gameModel.isExpert() && ((message.get("subtype").getAsString().equals("return")) ||
                (message.get("subtype").getAsString().equals("ban")) ||
                (message.get("special") != null && message.get("special").getAsBoolean()) ||
                (message.get("move") != null && !message.get("move").getAsBoolean()))) {
            int idSpecialCharacter = 0;
            for (SpecialCharacter sc : gameModel.getGameBoard().getSpecialCharacters()) {
                if (sc.isActive()) {
                    idSpecialCharacter = sc.getId();
                    break;
                }
            }
            if (idSpecialCharacter == 0) throw new IllegalMoveException();
            else {
                if (gameModel.getGameBoard().getSpecialCharacterById(idSpecialCharacter).getUsesNumber() == 0)
                    throw new IllegalMoveException();
                gameModel.getGameBoard().getSpecialCharacterById(idSpecialCharacter).decreaseUsesNumber();
            }
        }
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
                            default -> throw new IllegalMoveException();
                        }
                    }
                    case "pay" -> checkCharacterPayment(message);
                    case "refill" -> checkEntranceRefill(message);
                    case "ban" -> checkBan(message);
                    case "ignore" -> checkIgnoreColor();
                    case "return" -> checkReturn();
                    default -> throw new IllegalMoveException();
                }
            } catch (IllegalMoveException ime) {
                return false;
            }
        }
        return true;
    }

    /**
     * Manages end-game logic.
     *
     * @param command The command inserted by the user.
     */
    public void manageEndGame(String command) {
        if (command.equals("") || command.equals("exit")) {
            if (!getClientState().equals(ClientState.CONNECTION_LOST)) this.setClientState(ClientState.MAIN_MENU);
            updateScreen();
            this.resetGame();
        } else {
            this.errorOccurred("Wrong command.");
        }
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
                if (assistantId < 1 || assistantId > 10 || p.getCurrentPlayedAssistant() != null && assistantId == p.getCurrentPlayedAssistant().getId())
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
            switch (getGameModel().getSubPhase()) {
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3 -> {
                }
                case MOVE_STUDENT_4 -> {
                    if (getGameModel().getPlayersNumber() != 3) throw new IllegalActionException();
                }
                default -> {
                    if (getGameModel().getSubPhase().equals(CHOOSE_CLOUD) && !gameModel.isExpert() || gameModel.isExpert() && !message.get("special").getAsBoolean()) {
                        throw new IllegalMoveException();
                    }
                }
            }

            String from = message.get("from").getAsString();
            String to = message.get("to").getAsString();

            if (from.equals(to)) throw new IllegalMoveException();
            if (!(message.get("fromId") instanceof JsonNull) && !(message.get("toId") instanceof JsonNull) && message.get("fromId").getAsString().equals(message.get("toId").getAsString()))
                throw new IllegalMoveException();

            checkStudentMoveFrom(message);
            checkStudentMoveTo(message);
        }
    }

    /**
     * This method is a helper for 'checkStudentMove', check 'from' parameter.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the requested move is illegal.
     */
    public void checkStudentMoveFrom(JsonObject message) throws IllegalMoveException {
        String from = message.get("from").getAsString();
        String to = message.get("to").getAsString();

        switch (from) {
            case "entrance" -> {
                if (this.gameModel.getPlayerByName(this.userName).getSchoolBoard().getEntrance().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                    throw new IllegalMoveException();
            }
            case "dining-room" -> {
                if (!to.equals("entrance") || !getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(10) == null || !getGameModel().getGameBoard().getSpecialCharacterById(10).isActive())
                    throw new IllegalMoveException();
                if (this.gameModel.getPlayerByName(this.getUserName()).getSchoolBoard().getDiningRoom().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                    throw new IllegalMoveException();
            }
            case "card" -> {
                if (!to.equals("entrance") && !to.equals("dining-room") && !to.equals("island"))
                    throw new IllegalMoveException();
                SpecialCharacter involvedCharacter = this.gameModel.getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt());
                if (involvedCharacter == null || !involvedCharacter.isActive() || involvedCharacter.getStudents() == null || involvedCharacter.getStudents().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                    throw new IllegalMoveException();
            }
            default -> throw new IllegalMoveException();
        }
    }

    /**
     * This method is a helper for 'checkStudentMove', check 'to' parameter.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the requested move is illegal.
     */
    public void checkStudentMoveTo(JsonObject message) throws IllegalMoveException {
        String to = message.get("to").getAsString();

        switch (to) {
            case "entrance" -> {
                SpecialCharacter chr10 = this.gameModel.getGameBoard().getSpecialCharacterById(10);
                SpecialCharacter chr7 = this.gameModel.getGameBoard().getSpecialCharacterById(7);
                if ((chr10 != null && !chr10.isActive()) && (chr7 != null && !chr7.isActive()))
                    throw new IllegalMoveException();
            }
            case "card" -> {
                if (message.get("toId").getAsInt() != 7 || !this.gameModel.isExpert() || this.gameModel.getGameBoard().getSpecialCharacterById(7) == null || !this.gameModel.getGameBoard().getSpecialCharacterById(7).isActive())
                    throw new IllegalMoveException();
            }
            case "island" -> {
                int destinationIndex = message.get("toId").getAsInt();
                if (destinationIndex < 0 || destinationIndex > 11) throw new IllegalMoveException();
                while (getGameModel().getGameBoard().getIslandById(destinationIndex).hasPrev()) {
                    destinationIndex = (destinationIndex - 1) % 12;
                    if (destinationIndex < 0) destinationIndex = destinationIndex + 12;
                }
                message.remove("toId");
                message.addProperty("toId", destinationIndex);
            }
            case "dining-room" -> {
            }
            default -> throw new IllegalMoveException();
        }
    }

    /**
     * Checks if the mother nature move is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkMotherNatureMove(JsonObject message) throws IllegalMoveException {
        if (!getGameModel().getSubPhase().equals(MOVE_MOTHER_NATURE) && (message.get("move").getAsBoolean()))
            throw new IllegalMoveException();

        int finalIsland = message.get("island").getAsInt();
        if (finalIsland < 0 || finalIsland > 11) throw new IllegalMoveException();

        int motherNatureIsland = getGameModel().getGameBoard().getMotherNatureIsland();

        while (getGameModel().getGameBoard().getIslandById(finalIsland).hasPrev()) {
            finalIsland = (finalIsland - 1) % 12;
            if (finalIsland < 0) finalIsland = finalIsland + 12;
        }
        message.remove("island");
        message.addProperty("island", finalIsland);

        if (message.get("move").getAsBoolean()) {
            int maxDistance = getGameModel().getPlayerByName(this.getUserName()).getCurrentPlayedAssistant().getMaxDistance();

            int distanceWanted = (getGameModel().getGameBoard().getIslandById(motherNatureIsland).hasNext()) ? -1 : 0;
            for (int i = ((motherNatureIsland + 1) % 12); i != finalIsland; i = ((i + 1) % 12)) {
                if (!getGameModel().getGameBoard().getIslandById(i).hasNext()) distanceWanted++;
            }
            distanceWanted++;

            if (distanceWanted > maxDistance || distanceWanted == 0) throw new IllegalMoveException();
        }
    }

    /**
     * Checks if the chosen special character payment is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkCharacterPayment(JsonObject message) throws IllegalMoveException {
        if (!getGameModel().isExpert()) throw new IllegalMoveException();

        int characterId = message.get("character").getAsInt();
        if (!getGameModel().isExpert()) throw new IllegalMoveException();
        if (getGameModel().getGameBoard().getSpecialCharacterById(characterId) == null || getGameModel().getGameBoard().getSpecialCharacterById(characterId).isActive() || getGameModel().getGameBoard().characterPaidInTurn())
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
        if (!getGameModel().getSubPhase().equals(CHOOSE_CLOUD)) throw new IllegalMoveException();

        if (getGameModel().getGameBoard().getClouds().get(message.get("cloud").getAsInt()).getStudents(true) == null)
            throw new IllegalMoveException();
    }

    /**
     * Checks whether the ban can be calls.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkBan(JsonObject message) throws IllegalMoveException {
        if (!this.gameModel.isExpert()) throw new IllegalMoveException();
        if (this.gameModel.getGameBoard().getIslandById(message.get("island").getAsInt()).isBanned())
            throw new IllegalMoveException();
        for (SpecialCharacter sc : this.gameModel.getGameBoard().getSpecialCharacters()) {
            if (sc.getId() == 5 && !sc.isActive()) throw new IllegalMoveException();
            else if (sc.getId() == 5) return;
        }
        throw new IllegalMoveException();
    }

    /**
     * Checks whether the ignored color command can be calls.
     *
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkIgnoreColor() throws IllegalMoveException {
        if (!this.gameModel.isExpert()) throw new IllegalMoveException();
        for (SpecialCharacter sc : this.gameModel.getGameBoard().getSpecialCharacters()) {
            if (sc.getId() == 9 && !sc.isActive()) throw new IllegalMoveException();
            else if (sc.getId() == 9) return;
        }
        throw new IllegalMoveException();
    }

    /**
     * Checks whether the return command can be calls.
     *
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkReturn() throws IllegalMoveException {
        if (!this.gameModel.isExpert()) throw new IllegalMoveException();
        for (SpecialCharacter sc : this.gameModel.getGameBoard().getSpecialCharacters()) {
            if (sc.getId() == 12 && !sc.isActive()) throw new IllegalMoveException();
            else if (sc.getId() == 12) return;
        }
        throw new IllegalMoveException();
    }

    /**
     * Manages the CONNECTION_LOST state of the client, terminating all the server-associated threads.
     */
    public void manageConnectionLost() {
        this.resetGame();
        this.getGameServer().disconnected();
        this.gameServer = null;
        Log.debug("manageConnectionLost");
        this.setClientState(ClientState.START_SCREEN);
        updateScreen();
        this.errorOccurred("Connection lost.");
    }

    /**
     * Sets the gameModel attribute to a new GameModel instance.
     *
     * @param newGameModel The new GameModel instance.
     */
    public void initializeGameModel(GameModel newGameModel) {
        synchronized (this.lock) {
            this.gameModel = newGameModel;
            this.replyArrived = true;
            if (newGameModel != null) {
                this.lock.notifyAll();
            }
            Log.debug("Gui notified");
        }
    }

    /**
     * Returns the current client's state.
     *
     * @return The state attribute.
     */
    public ClientState getClientState() {
        return this.state;
    }

    /**
     * Sets the current client's state to a new second.
     *
     * @param newState The next state to set.
     */
    public void setClientState(ClientState newState) {
        synchronized (this.lock) {
            this.state = newState;
            this.lock.notifyAll();
        }
    }

    /**
     * Flushes the current game model and sets the current state to MAIN_MENU.
     */
    public void resetGame() {
        this.setClientState(ClientState.MAIN_MENU);
        this.gameModel = null;
        this.userName = null;
        this.gameCode = null;
        this.setEndState(null);
    }

    /**
     * Prints an error message.
     *
     * @param message The message to print.
     */
    public void errorOccurred(String message) {
        view.showError(message);
        Log.warning(message);
    }

    /**
     * Waits for a response from the game server; if the timeout expires, the CONNECTION_LOST state is set.
     */
    private void tryConnection() {
        synchronized (this.lock) {
            try {
                this.replyArrived = false;
                while (!this.replyArrived)
                    this.lock.wait(10000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.debug("tryConnection.");
                this.setClientState(ClientState.CONNECTION_LOST);
            }
        }
    }

    /**
     * Updates the view screen.
     */
    private void updateScreen() {
        view.updateScreen(false);
    }
}
