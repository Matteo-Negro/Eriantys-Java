package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import it.polimi.ingsw.clientController.GameServer;
import it.polimi.ingsw.clientController.GameStatus;
import it.polimi.ingsw.clientStatus.Status;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.view.cli.GameCreation;
import it.polimi.ingsw.view.cli.JoinGame;
import it.polimi.ingsw.view.cli.MainMenu;
import it.polimi.ingsw.view.cli.SplashScreen;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli extends Thread {
    private final Terminal terminal;
    private String userName;
    private GameServer gameServer;
    private GameStatus gameStatus;
    private ClientStates state;
    private Object serverReplyLock;

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.gameServer = null;
        this.gameStatus = null;
        this.userName = null;
        this.serverReplyLock = new Object();
        this.terminal = TerminalBuilder.terminal();
        clearScreen(terminal, false);
    }

    public GameServer getGameServer() {
        return this.gameServer;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public String getUserName() {
        return this.userName;
    }


    public void run() {
        boolean process = true;
        try {
            while (process) {
                switch (getClientState()) {
                    case START_SCREEN -> // Splash screen printing and control
                            manageStartScreen();

                    case MAIN_MENU -> //wait for user input (game creation or join game)
                            manageMainMenu();

                    case GAME_CREATION -> //wait for user input (player number and difficulty)
                            //then wait for server reply
                            //transition to game login
                            manageGameCreation();
                    case JOIN_GAME -> //wait for user input (game code)
                            //then wait for server reply
                            //transition to game login
                            manageJoinGame();
                    case GAME_LOGIN -> //wait for user input (username)
                            //then wait for server reply
                            //transition to game waiting room
                            manageGameLogin();
                    case GAME_WAITINGROOM -> //wait for game start message from the server
                            //transition to game running
                            manageWaitingRoom();
                    case GAME_RUNNING -> {
                        //manage game logic
                        //when end game message arrives from the server -> transition to end game
                    }
                    case END_GAME -> {
                        //visualize end game screen
                        //transition to main menu

                    }
                    case EXIT -> process = false;
                }
            }
        } catch (Exception e) {
            Log.error(e.getMessage());
        } finally {
            clearScreen(terminal, true);
            // System.exit(0);
        }
    }

    private void manageStartScreen() {
        clearScreen(terminal, false);
        do {
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
                gameServer.start();
                setClientState(ClientStates.MAIN_MENU);
            } catch (IOException | NumberFormatException e) {
                printError(terminal, "Wrong data provided or server unreachable.");
            }
        } while (gameServer == null);
    }

    private void manageMainMenu() {
        clearScreen(terminal, false);
        String option;

        do {
            MainMenu.print(terminal);
            option = readLine(" ", terminal, List.of(node("1"), node("2")), false, null);

            switch (option) {
                case "1" -> this.setClientState(ClientStates.GAME_CREATION);
                case "2" -> this.setClientState(ClientStates.JOIN_GAME);
                default -> printError(terminal, "Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.MAIN_MENU));
    }

    private void manageGameCreation() {
        clearScreen(terminal, false);

        boolean wrongCommand;
        int expectedPlayers = 2;
        boolean expert = false;
        do {
            wrongCommand = false;
            GameCreation.print(terminal);
            String playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("esc")), false, null);
            switch (playersNumber) {
                case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
                case "esc" -> {
                    this.setClientState(ClientStates.MAIN_MENU);
                    this.resetGame();
                    return;
                }
                default -> wrongCommand = true;
            }

            terminal.writer().print(ansi().restoreCursorPosition());
            terminal.writer().print(ansi().cursorMove(-1, 1));
            terminal.writer().print(ansi().saveCursorPosition());
            terminal.flush();
            String difficulty = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("esc")), false, null);
            switch (difficulty) {
                case "normal" -> expert = false;
                case "expert" -> expert = true;
                case "esc" -> {
                    this.setClientState(ClientStates.MAIN_MENU);
                    this.resetGame();
                    return;
                }
                default -> wrongCommand = true;
            }
            if (wrongCommand) printError(terminal, "Wrong command.");
        } while (wrongCommand);

        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.gameCreation(expectedPlayers, expert));
        try {
            this.serverReplyLock.wait(10000);
        } catch (InterruptedException ie) {
            printError(terminal, "Connection error.");
            this.resetGame();

        }
        if (this.getClientState().equals(ClientStates.GAME_CREATION)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageJoinGame() {
        clearScreen(terminal, false);
        JoinGame.print(terminal);


        String gameCode = readLine("", terminal, List.of(node("esc")), false, null);
        if ("esc".equals(gameCode)) {
            this.setClientState(ClientStates.MAIN_MENU);
            this.resetGame();
            return;
        }

        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.EnterGame(gameCode));

        try {
            this.serverReplyLock.wait(10000);
        } catch (InterruptedException ie) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
        if (this.getClientState().equals(ClientStates.JOIN_GAME)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageGameLogin() {
        clearScreen(terminal, false);
        //TODO Print game login screen on cli.

        String username;
        boolean usernameIsValid = true;
        do {
            username = readLine(" ", terminal, List.of(node("esc")), false, null);
            if ("esc".equals(username)) {
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            }
            if (this.getGameStatus().getWaitingRoom().containsKey(username) && this.getGameStatus().getWaitingRoom().get(username).equals("connected")) {
                usernameIsValid = false;
                printError(terminal, "Username not valid.");
            }
        } while (!usernameIsValid);

        this.userName = username;
        //Send command to the server and wait for a response.
        this.gameServer.sendCommand(MessageCreator.login(username));

        try {
            this.serverReplyLock.wait(10000);
        } catch (InterruptedException ie) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
        if (this.getClientState().equals(ClientStates.GAME_LOGIN)) {
            printError(terminal, "Connection error.");
            this.resetGame();
        }
    }

    private void manageWaitingRoom() {
        clearScreen(terminal, false);
        //TODO Print waiting room screen on cli.


        String command;
        do {
            command = readLine(" ", terminal, List.of(node("esc")), false, null);
            if (command.equals("esc")) {
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            } else {
                printError(terminal, "Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.GAME_WAITINGROOM));

    }

    private void manageGameRunning() {
        clearScreen(terminal, false);
        //TODO Print current status screen on cli.

        String command = readLine(" ", terminal, null, false, null);
        this.manageUserCommand(command);
    }

    private void manageEndGame() {
        clearScreen(terminal, false);
        //TODO Print end game screen on cli.

        String command;
        do {
            command = readLine(" ", terminal, List.of(node("esc")), false, null);
            if (command.equals("esc")) {
                this.setClientState(ClientStates.MAIN_MENU);
                this.resetGame();
                return;
            } else {
                printError(terminal, "Wrong command.");
            }
        } while (this.getClientState().equals(ClientStates.END_GAME));
    }

    public void manageMessage(JsonObject message) {

    }

    private void manageUserCommand(String command) {
    }

    public ClientStates getClientState() {
        return this.state;
    }

    public void setClientState(ClientStates newState) {
        this.state = newState;
    }

    private void resetGame() {
        this.setClientState(ClientStates.MAIN_MENU);
        if (this.gameStatus != null) {
            this.gameStatus = null;
        }
    }
}
