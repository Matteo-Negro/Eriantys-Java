package it.polimi.ingsw.client;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.controller.GameServer;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.Island;
import it.polimi.ingsw.client.model.SpecialCharacter;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.parsers.CommandParser;
import org.fusesource.jansi.Ansi;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import it.polimi.ingsw.utilities.exceptions.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static it.polimi.ingsw.utilities.GameControllerStates.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli extends Thread {
    private final Terminal terminal;
    private final Object lock;
    private String userName;
    private String gameCode;
    private GameServer gameServer;
    private GameModel gameModel;
    private ClientStates state;
    private boolean modelUpdated;
    private final History history;

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.gameServer = null;
        this.gameModel = null;
        this.modelUpdated = false;
        this.userName = null;
        this.gameCode = null;
        this.lock = new Object();
        this.terminal = TerminalBuilder.terminal();
        this.history = new DefaultHistory();
        Autocompletion.initialize(this);
        clearScreen(terminal, false);
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

    private boolean hasCommunicationToken() {
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
     * The main client method, chooses the correct method to invoke basing on the current state.
     */
    @Override
    public void run() {
        boolean process = true;
        try {
            while (process) {
                //Log.debug(this.getClientState().toString());
                switch (getClientState()) {
                    case START_SCREEN -> manageStartScreen();
                    case MAIN_MENU -> manageMainMenu();
                    case GAME_CREATION -> manageGameCreation();
                    case JOIN_GAME -> manageJoinGame();
                    case GAME_LOGIN -> manageGameLogin();
                    case GAME_WAITING_ROOM -> manageWaitingRoom();
                    case GAME_RUNNING -> manageGameRunning();
                    case END_GAME -> {
                        //visualize end game screen
                        //transition to main menu
                    }
                    case CONNECTION_LOST -> manageConnectionLost();
                    case EXIT -> process = false;
                }
            }
        } catch (Exception e) {
            Log.error(e);
        } finally {
            clearScreen(terminal, true);
            if (this.gameServer != null)
                gameServer.disconnected();
        }
    }

    /**
     * Manages the start-screen's I/O.
     */
    private void manageStartScreen() {
        SplashScreen.print(terminal);
        String hostIp = readLine(" ", terminal, List.of(node("localhost"), node("127.0.0.1")), false, null);
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-18, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        try {
            int hostTcpPort = Integer.parseInt(readLine(" ", terminal, List.of(node("36803")), false, null));
            Socket hostSocket = new Socket(hostIp, hostTcpPort);
            hostSocket.setSoTimeout(10000);
            this.gameServer = new GameServer(hostSocket, this);
            new Thread(this.gameServer).start();
            setClientState(ClientStates.MAIN_MENU);
            clearScreen(terminal, false);
        } catch (IOException | NumberFormatException e) {
            this.errorOccurred("Wrong data provided or server unreachable.");
        }
    }

    /**
     * Manages the main-menu-screen's I/O.
     */
    private void manageMainMenu() {
        String option;

        MainMenu.print(terminal);
        option = readLine(" ", terminal, List.of(node("1"), node("2")), false, null);

        switch (option) {
            case "1" -> {
                this.setClientState(ClientStates.GAME_CREATION);
                clearScreen(terminal, false);
            }
            case "2" -> {
                this.setClientState(ClientStates.JOIN_GAME);
                clearScreen(terminal, false);
            }
            default -> this.errorOccurred("Wrong command.");
        }
    }

    /**
     * Manages the game-creation-screen's I/O.
     */
    private void manageGameCreation() {
        int expectedPlayers;
        boolean expert;

        GameCreation.print(terminal);
        String playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);
        switch (playersNumber) {
            case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
            case "exit" -> {
                this.setClientState(ClientStates.MAIN_MENU);
                clearScreen(terminal, false);
                this.resetGame();
                return;
            }
            default -> {
                this.errorOccurred("Wrong command.");
                return;
            }
        }

        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-1, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        String difficulty = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("exit")), false, null);
        switch (difficulty) {
            case "normal" -> expert = false;
            case "expert" -> expert = true;
            case "exit" -> {
                this.setClientState(ClientStates.MAIN_MENU);
                clearScreen(terminal, false);
                this.resetGame();
                return;
            }
            default -> {
                this.errorOccurred("Wrong command.");
                return;
            }
        }

        this.gameServer.sendCommand(MessageCreator.gameCreation(expectedPlayers, expert));

        this.tryConnection();

        if (this.getClientState().equals(ClientStates.GAME_CREATION))
            this.setClientState(ClientStates.CONNECTION_LOST);

        clearScreen(terminal, false);
    }

    /**
     * Manages the join-game-screen's I/O.
     */
    private void manageJoinGame() {
        JoinGame.print(terminal);

        String gameCode = readLine(" ", terminal, List.of(node("exit")), false, null).toUpperCase(Locale.ROOT);
        if ("EXIT".equals(gameCode)) {
            this.setClientState(ClientStates.MAIN_MENU);
            clearScreen(terminal, false);
            this.resetGame();
            return;
        }
        this.gameServer.sendCommand(MessageCreator.enterGame(gameCode));
        this.setGameCode(gameCode);

        this.tryConnection();

        if (this.getClientState().equals(ClientStates.JOIN_GAME)) {
            this.errorOccurred("The desired game doesn't exist or is full.");
            return;
        }

        clearScreen(terminal, false);
    }

    /**
     * Manages the login-screen's I/O.
     */
    private void manageGameLogin() {

        Login.print(terminal, this.getGameModel().getWaitingRoom(), this.getGameModel().getPlayersNumber());

        String username;

        username = readLine(" ", terminal, List.of(node("exit")), false, null);
        if ("exit".equals(username)) {
            this.setClientState(ClientStates.MAIN_MENU);
            clearScreen(terminal, false);
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

        clearScreen(terminal, false);
    }

    /**
     * Manages the waiting-room-screen's I/O.
     */
    static int waitingIteration = 0;

    private void manageWaitingRoom(){
        if(this.getGameModel()!=null){

            List<String> onlinePlayers = new ArrayList<>();
            for (String name : this.getGameModel().getWaitingRoom().keySet())
                if (Boolean.TRUE.equals(this.getGameModel().getWaitingRoom().get(name)))
                    onlinePlayers.add(name);

            WaitingRoom.print(terminal, onlinePlayers, this.getGameCode(), this.getGameModel().getPlayersNumber(), waitingIteration++);

        }
        synchronized (this.lock){
            try{
                this.lock.wait();
            }catch(InterruptedException ie){
                resetGame();
            }
        }
        clearScreen(terminal, false);
    }

    /**
     * Manages the game-screen's I/O.
     */
    private void manageGameRunning() {
        JsonObject message;

        Game.print(terminal, this.gameModel, this.getGameCode(), this.getGameModel().getPlayerByName(userName).isActive());

        if (this.hasCommunicationToken()) {
            String command = readLine(getPrettyUserName(), terminal, Autocompletion.get(), true, history).toLowerCase(Locale.ROOT);

            // Logout command.
            if (command.equals("exit") || command.equals("logout")) {
                this.getGameServer().sendCommand(MessageCreator.logout());
                this.resetGame();
                clearScreen(terminal, false);
                return;
            }

            // Another player disconnected.
            if(getGameModel() == null) return;

            if (command.contains("info")) {
                // TODO: managed info printing (for now the searches for info but it's a void method)
                CommandParser.infoGenerator(command);
            } else {
                //Command parsing and check.
                message = CommandParser.commandManager(command, userName);
                try{
                    if(checkMessage(message)) getGameServer().sendCommand(message);
                    else errorOccurred("Command not allowed");
                }catch(IllegalActionException iae){
                    errorOccurred("Connection lost.");
                    setClientState(ClientStates.CONNECTION_LOST);
                    clearScreen(terminal, false);
                    return;
                }

            }

        }
        else{
            synchronized (this.lock) {
                try {
                    this.lock.wait(1500);
                } catch (InterruptedException e) {
                    this.resetGame();
                }
            }
        }
        clearScreen(terminal, false);
    }


    private String getPrettyUserName() {
        Ansi ansi = new Ansi();
        ansi.a(" ");
        ansi.a(foreground(switch (gameModel.getPlayerByName(userName).getWizard()) {
            case FUCHSIA -> WizardFuchsia.getInstance();
            case GREEN -> WizardGreen.getInstance();
            case WHITE -> WizardWhite.getInstance();
            case YELLOW -> WizardYellow.getInstance();
        }));
        ansi.a(bold(true));
        ansi.a(userName);
        ansi.a(bold(false));
        ansi.a(foreground(Grey.getInstance()));
        ansi.a(" > ");
        ansi.a(foreground(White.getInstance()));
        return ansi.toString();
    }

    /**
     * Manages the end-game-screen's I/O.
     */
    private void manageEndGame() {
        //TODO Print end game screen on cli.
        String command;
        do {
            command = readLine(" ", terminal, List.of(node("exit")), false, null);
            if (command.equals("exit")) {
                this.setClientState(ClientStates.MAIN_MENU);
                clearScreen(terminal, false);
                this.resetGame();
                return;
            } else {
                this.errorOccurred("Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.END_GAME));
        clearScreen(terminal, false);
    }

    /**
     * Checks the message validity.
     *
     * @param message The message to check.
     * @return True if the message is correct, false otherwise.
     */
    private boolean checkMessage(JsonObject message) throws IllegalActionException{
        if(this.getGameModel().getPhase().equals(Phase.PLANNING)){
            if(!message.get("subtype").getAsString().equals("playAssistant")) return false;
            else{
                int assistantId = message.get("assistant").getAsInt();
                return !message.get("subtype").getAsString().equals("playAssistant") || getGameModel().getPlayerByName(this.getUserName()).getAssistantById(assistantId) != null;
            }

        }
        else{
            try{
                switch(message.get("subtype").getAsString()){
                    case "moveStudent" -> checkStudentMove(message);
                    case "motherNature" -> checkMotherNatureMove(message);
                    case "pay" -> checkCharacterPayment(message);
                    case "refill" -> checkEntranceRefill(message);
                    default -> {
                        return false;
                    }
                }
            }catch (IllegalMoveException ime){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the student move request is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the requested move is illegal.
     * @throws IllegalActionException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkStudentMove(JsonObject message) throws IllegalMoveException, IllegalActionException{
        synchronized (this.getGameModel()){
            switch(getGameModel().getSubphase()){
                case MOVE_STUDENT_1, MOVE_STUDENT_2, MOVE_STUDENT_3 -> {}
                case MOVE_STUDENT_4 -> {
                    if(getGameModel().getPlayersNumber()!=3) throw new IllegalActionException();
                }
            }

            String from = message.get("from").getAsString();
            String to = message.get("to").getAsString();

            if(from.equals(to)) throw new IllegalMoveException();
            if(!(message.get("fromId") instanceof JsonNull) && !(message.get("toId") instanceof JsonNull) && message.get("fromId").getAsString().equals(message.get("toId").getAsString())) throw new IllegalMoveException();

            switch(from){
                case "entrance" -> {
                    if(getGameModel().getPlayerByName(this.getUserName()).getSchoolBoard().getEntrance().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                        throw new IllegalMoveException();
                }
                case "diningRoom" -> {
                    if(!to.equals("entrance") || !getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(10)!=null || !getGameModel().getGameBoard().getSpecialCharacterById(10).isActive())
                        throw new IllegalMoveException();
                    if(getGameModel().getPlayerByName(this.getUserName()).getSchoolBoard().getDiningRoom().get(HouseColor.valueOf(message.get("color").getAsString())) == 0)
                        throw new IllegalMoveException();
                }
                case "character" -> {
                    if(!to.equals("entrance") && !to.equals("diningRoom") && !to.equals("island"))
                        throw new IllegalMoveException();

                    SpecialCharacter involvedCharacter = getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt());
                    if(involvedCharacter == null || !involvedCharacter.isActive() || involvedCharacter.getStudents() == null || involvedCharacter.getStudents().get(HouseColor.valueOf(message.get("color").getAsString()))==0)
                        throw new IllegalMoveException();
                }
                default -> throw new IllegalMoveException();
            }

            switch(to){
                case "entrance" -> {
                    if(!from.equals("character") && (!getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(10) != null || !getGameModel().getGameBoard().getSpecialCharacterById(10).isActive())) throw new IllegalMoveException();
                    if(message.get("fromId") instanceof JsonNull || !getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt()).isActive() || getGameModel().getGameBoard().getSpecialCharacterById(message.get("fromId").getAsInt()) == null)
                        throw new IllegalMoveException();
                }
                case "character" ->{
                    if(message.get("toId").getAsInt()!=7 || !getGameModel().isExpert() || getGameModel().getGameBoard().getSpecialCharacterById(7)==null || !getGameModel().getGameBoard().getSpecialCharacterById(7).isActive())
                        throw new IllegalMoveException();
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
    private void checkMotherNatureMove(JsonObject message) throws IllegalMoveException{
        if(!getGameModel().getSubphase().equals(MOVE_MOTHER_NATURE)) throw new IllegalMoveException();

        int finalIsland = message.get("island").getAsInt();
        int maxDistance = getGameModel().getPlayerByName(this.getUserName()).getCurrentPlayedAssistant().getMaxDistance();
        int motherNatureIsland = 0;
        for(int i=0; i<getGameModel().getGameBoard().getIslands().size(); i++){
            Island isl = getGameModel().getGameBoard().getIslands().get(i);
            if(isl.hasMotherNature()) motherNatureIsland = i;
        }

        int distanceWanted;
        if(finalIsland<motherNatureIsland) distanceWanted = finalIsland + 12 - motherNatureIsland;
        else distanceWanted = finalIsland - motherNatureIsland;

        if(distanceWanted > maxDistance) throw new IllegalMoveException();
    }

    /**
     * Checks if the chosen special character payment is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkCharacterPayment(JsonObject message) throws IllegalMoveException{
        if(getGameModel().getSubphase().equals(MOVE_MOTHER_NATURE))
            throw new IllegalMoveException();

        int characterId = message.get("character").getAsInt();
        if(!getGameModel().isExpert()) throw new IllegalMoveException();

        if(getGameModel().getGameBoard().getSpecialCharacterById(characterId) == null || getGameModel().getGameBoard().getSpecialCharacterById(characterId).isActive())
            throw new IllegalMoveException();

        if(getGameModel().getPlayerByName(this.getUserName()).getCoins() < getGameModel().getGameBoard().getSpecialCharacterById(characterId).getCost())
            throw new IllegalMoveException();
    }

    /**
     * Checks if the refill from the chosen cloud is legal.
     *
     * @param message The message to check.
     * @throws IllegalMoveException Thrown if the client model is not aligned with that of the game server.
     */
    private void checkEntranceRefill(JsonObject message) throws IllegalMoveException{
        if(!getGameModel().getSubphase().equals(END_TURN)) throw new IllegalMoveException();

        int cloudId = message.get("cloud").getAsInt();
        if(getGameModel().getGameBoard().getClouds().get(cloudId).getStudents(true)==null)
            throw new IllegalMoveException();
    }

    /**
     * Manages the CONNECTION_LOST state of the client, terminating all the server-associated threads.
     */
    private void manageConnectionLost() {
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
            if(newGameModel!=null){
                this.modelUpdated = true;
                this.lock.notify();
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
        synchronized (this.lock){
            this.state = newState;
            this.lock.notify();
        }

    }

    /**
     * Flushes the current game model and sets the current state to MAIN_MENU.
     */
    private void resetGame() {
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
        printError(terminal, message);
        Log.warning(message);
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
                clearScreen(terminal, false);
            }
        }
    }
}
