package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.GameServer;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.model.SchoolBoard;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.client.view.cli.pages.WaitingRoom;
import it.polimi.ingsw.client.view.cli.pages.subparts.Realm;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.io.Reader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
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

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.gameServer = null;
        this.gameModel = null;
        this.userName = null;
        this.gameCode = null;
        this.lock = new Object();
        this.terminal = TerminalBuilder.terminal();
        clearScreen(terminal, false);
    }

    public GameServer getGameServer() {
        return this.gameServer;
    }

    public GameModel getGameModel() {
        return this.gameModel;
    }

    public Object getLock() {
        return lock;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getGameCode(){
        return this.gameCode;
    }

    public void setGameCode(String gameCode){
        this.gameCode = gameCode;
    }

    @Override
    public void run() {
        boolean process = true;
        try {
            while (process) {
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
            Log.error(e.getMessage());
        } finally {
            clearScreen(terminal, true);
            if (this.gameServer != null)
                gameServer.disconnected();
        }
    }

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

    private void manageJoinGame() {
        JoinGame.print(terminal);

        String gameCode = readLine(" ", terminal, List.of(node("exit")), false, null);
        if ("exit".equals(gameCode)) {
            this.setClientState(ClientStates.MAIN_MENU);
            clearScreen(terminal, false);
            this.resetGame();
            return;
        }
        this.gameServer.sendCommand(MessageCreator.enterGame(gameCode));
        this.setGameCode(gameCode);

        this.tryConnection();

        if (this.getClientState().equals(ClientStates.JOIN_GAME))
            this.setClientState(ClientStates.CONNECTION_LOST);

        clearScreen(terminal, false);
    }

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

    static int waitingIteration = 0;
    private void manageWaitingRoom() {
        List<String> onlinePlayers = new ArrayList<>();
        for (String name : this.getGameModel().getWaitingRoom().keySet()) if(this.getGameModel().getWaitingRoom().get(name)) onlinePlayers.add(name);

        WaitingRoom.print(terminal, onlinePlayers, this.getGameCode(), this.getGameModel().getPlayersNumber(), waitingIteration++);
        synchronized (this.lock) {
            try {
                this.lock.wait(500);
            } catch (InterruptedException e) {
                this.resetGame();
            }
        }
        clearScreen(terminal, false);
    }

    private void manageGameRunning() {
        //TODO Print current status screen on cli.
        //work in progress.;
        Game.print(terminal, this.gameModel, this.getGameCode());
        synchronized (this.lock) {
            try {
                this.lock.wait(2000);
            } catch (InterruptedException e) {
                this.resetGame();
            }
        }
        clearScreen(terminal, false);
    }

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

    private void manageConnectionLost() {
        this.resetGame();
        this.getGameServer().disconnected();
        this.gameServer = null;
        this.errorOccurred("Connection lost.");
        this.setClientState(ClientStates.START_SCREEN);
    }

    public void initializeGameModel(GameModel newGameModel) {
        this.gameModel = newGameModel;
    }

    public ClientStates getClientState() {
        return this.state;
    }

    public void setClientState(ClientStates newState) {
        this.state = newState;
    }

    private void resetGame() {
        this.setClientState(ClientStates.MAIN_MENU);
        this.gameModel = null;
    }

    public void errorOccurred(String message) {
        clearScreen(terminal, false);
        printError(terminal, message);
        Log.warning(message);
    }

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
