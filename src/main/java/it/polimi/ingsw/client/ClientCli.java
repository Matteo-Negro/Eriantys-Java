package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.GameServer;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import org.fusesource.jansi.Ansi;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli extends Thread {
    private final Terminal terminal;
    private final Object lock;
    private String userName;
    private boolean communicationToken;
    private String gameCode;
    private GameServer gameServer;
    private GameModel gameModel;
    private ClientStates state;
    private boolean modelUpdated;
    private History history;

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.gameServer = null;
        this.gameModel = null;
        this.modelUpdated = false;
        this.userName = null;
        this.communicationToken = false;
        this.gameCode = null;
        this.lock = new Object();
        this.terminal = TerminalBuilder.terminal();
        this.history = new DefaultHistory();
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
        return this.communicationToken;
    }

    public void setCommunicationToken(boolean token) {
        this.communicationToken = token;
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
                Log.debug(this.getClientState().toString());
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
    private void manageWaitingRoom() throws Exception{
        try{
            this.modelUpdated = false;
            List<String> onlinePlayers = new ArrayList<>();
            for (String name : this.getGameModel().getWaitingRoom().keySet())
                if (Boolean.TRUE.equals(this.getGameModel().getWaitingRoom().get(name)))
                    onlinePlayers.add(name);

            WaitingRoom.print(terminal, onlinePlayers, this.getGameCode(), this.getGameModel().getPlayersNumber(), waitingIteration++);
            synchronized (this.lock) {
                try {
                    this.lock.wait(1000);
                } catch (InterruptedException e) {
                    this.resetGame();
                }
            }
            clearScreen(terminal, false);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    /**
     * Manages the game-screen's I/O.
     */
    private void manageGameRunning() {

        Game.print(terminal, this.gameModel, this.getGameCode(), this.getGameModel().getPlayerByName(userName).isActive());

        if (this.hasCommunicationToken()) {
            String command = readLine(getPrettyUserName(), terminal, Autocompletion.get(this), true, history).toLowerCase(Locale.ROOT);
            if (command.equals("exit") || command.equals("logout")) {
                this.getGameServer().sendCommand(MessageCreator.logout());
                this.resetGame();
            }
            //call static method for command parsing.
            //Checking message.
        }
        else{
            synchronized (this.lock) {
                try {
                    this.lock.wait(2000);
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

    /*private boolean checkCommand(JsonObject command){
        if(this.getGameModel().getPhase().equals(Phase.PLANNING)){
            if(!Phase.valueOf(command.get("subtype").getAsString()).equals("playAssistant") )
        }
        else{

        }
    }*/

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
        this.gameModel = newGameModel;
        this.modelUpdated = true;
        synchronized (this.lock) {
            this.lock.notify();
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
        this.state = newState;
    }

    /**
     * Flushes the current game model and sets the current state to MAIN_MENU.
     */
    private void resetGame() {
        this.setClientState(ClientStates.MAIN_MENU);
        this.gameModel = null;
    }

    /**
     * Prints an error message.
     *
     * @param message The message to print.
     */
    public void errorOccurred(String message) {
        clearScreen(terminal, false);
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
