package it.polimi.ingsw.client;

import it.polimi.ingsw.client.controller.GameServer;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.MessageCreator;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli extends Thread {
    private final Terminal terminal;
    private final Object serverReplyLock;
    private String userName;
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
        this.serverReplyLock = new Object();
        this.terminal = TerminalBuilder.terminal();
        clearScreen(terminal, false);
    }

    public GameServer getGameServer() {
        return this.gameServer;
    }

    public GameModel getGameModel() {
        return this.gameModel;
    }

    public Object getServerReplyLock() {
        return serverReplyLock;
    }

    public String getUserName() {
        return this.userName;
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
                    case GAME_RUNNING -> {
                        //manage game logic
                        //when end game message arrives from the server -> transition to end game
                    }
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
            if (gameServer != null)
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

        int hostTcpPort;
        try {
            hostTcpPort = Integer.parseInt(readLine(" ", terminal, List.of(node("36803")), false, null));
        } catch (NumberFormatException e) {
            clearScreen(terminal, false);
            printError(terminal, "Wrong data provided or server unreachable.");
            return;
        }

        try (Socket hostSocket = new Socket(hostIp, hostTcpPort)) {
            hostSocket.setSoTimeout(10000);
            this.gameServer = new GameServer(hostSocket, this);
            new Thread(this.gameServer).start();
            setClientState(ClientStates.MAIN_MENU);
            clearScreen(terminal, false);
        } catch (IOException e) {
            clearScreen(terminal, false);
            printError(terminal, "Wrong data provided or server unreachable.");
            return;
        }

        if (!this.getClientState().equals(ClientStates.START_SCREEN))
            clearScreen(terminal, false);
    }

    private void manageMainMenu() {
        MainMenu.print(terminal);
        String option = readLine(" ", terminal, List.of(node("1"), node("2"), node("exit")), false, null);
        clearScreen(terminal, false);
        switch (option) {
            case "1" -> this.setClientState(ClientStates.GAME_CREATION);
            case "2" -> this.setClientState(ClientStates.JOIN_GAME);
            case "exit" -> {
                if (this.gameServer != null)
                    this.gameServer.disconnected();
                this.setClientState(ClientStates.START_SCREEN);
                gameModel = null;
            }
            default -> {
                printError(terminal, "Wrong command.");
                this.setClientState(ClientStates.MAIN_MENU);
            }
        }

        if (!this.getClientState().equals(ClientStates.MAIN_MENU))
            clearScreen(terminal, false);
    }

    private void manageGameCreation() {

        int expectedPlayers;
        boolean expert;

        GameCreation.print(terminal);

        String playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);

        switch (playersNumber) {
            case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
            case "exit" -> {
                clearScreen(terminal, false);
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            }
            default -> {
                clearScreen(terminal, false);
                printError(terminal, "Wrong command.");
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
                clearScreen(terminal, false);
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            }
            default -> {
                clearScreen(terminal, false);
                printError(terminal, "Wrong command.");
                return;
            }
        }

        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.gameCreation(expectedPlayers, expert));
        //System.out.println("gameCreation request");

        if (tryConnection())
            return;

        clearScreen(terminal, false);

        if (this.getClientState().equals(ClientStates.GAME_CREATION)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageJoinGame() {

        JoinGame.print(terminal);

        String gameCode = readLine(" ", terminal, List.of(node("exit")), false, null);
        if (gameCode.equals("exit")) {
            clearScreen(terminal, false);
            this.setClientState(ClientStates.MAIN_MENU);
            this.resetGame();
            return;
        }

        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.enterGame(gameCode));

        if (tryConnection())
            return;

        if (this.getClientState().equals(ClientStates.JOIN_GAME)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageGameLogin() {

        Login.print(terminal, this.getGameModel().getWaitingRoom(), this.getGameModel().getPlayersNumber());

        String username = readLine(" ", terminal, List.of(node("exit")), false, null);

        if (username.equals("exit")) {
            clearScreen(terminal, false);
            this.setClientState(ClientStates.MAIN_MENU);
            this.resetGame();
            return;
        }

        if (this.getGameModel().getWaitingRoom().containsKey(username) && this.getGameModel().getWaitingRoom().get(username).equals(true)) {
            clearScreen(terminal, false);
            printError(terminal, "Invalid username.");
            return;
        }

        this.userName = username;
        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.login(username));

        if (tryConnection())
            return;

        clearScreen(terminal, false);

        if (this.getClientState().equals(ClientStates.GAME_LOGIN)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageWaitingRoom() {
        clearScreen(terminal, false);
        System.out.println("* waitingRoom screen.");
        //TODO Print waiting room screen on cli.


        String command;
        do {
            command = readLine(" ", terminal, List.of(node("exit")), false, null);
            if (command.equals("exit")) {
                clearScreen(terminal, false);
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
            } else {
                printError(terminal, "Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.GAME_WAITING_ROOM));

        if (tryConnection())
            return;

        clearScreen(terminal, false);
    }

    private void manageGameRunning() {
        /* clearScreen(terminal, false);
        //TODO Print current status screen on cli.

        String command = readLine(" ", terminal, null, false, null);
        */
    }

    private boolean tryConnection() {

        synchronized (this.serverReplyLock) {
            try {
                this.serverReplyLock.wait(10000);
            } catch (InterruptedException e) {
                clearScreen(terminal, false);
                printError(terminal, "Connection error.");
                this.resetGame();
                return true;
            }
        }

        clearScreen(terminal, false);

        return false;
    }

    private void manageEndGame() {
        clearScreen(terminal, false);
        //TODO Print end game screen on cli.

        String command;
        do {
            command = readLine(" ", terminal, List.of(node("exit")), false, null);
            if (command.equals("exit")) {
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            } else {
                printError(terminal, "Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.END_GAME));
    }

    public void manageConnectionLost() {
        this.resetGame();
        this.getGameServer().disconnected();
        this.gameServer = null;
        clearScreen(terminal, false);
        this.setClientState(ClientStates.START_SCREEN);
    }

    public ClientStates getClientState() {
        return this.state;
    }

    public void setClientState(ClientStates newState) {
        this.state = newState;
        //this.serverReplyLock.notify();
    }

    public void initializeGameStatus(GameModel newGameModel) {
        this.gameModel = newGameModel;
    }

    private void resetGame() {
        this.setClientState(ClientStates.MAIN_MENU);
        this.gameModel = null;
    }
}
