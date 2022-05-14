package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import it.polimi.ingsw.clientController.GameServer;
import it.polimi.ingsw.clientStatus.Status;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.view.cli.GameCreationScreen;
import it.polimi.ingsw.view.cli.JoinGameScreen;
import it.polimi.ingsw.view.cli.MainMenuScreen;
import it.polimi.ingsw.view.cli.SplashScreen;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

public class ClientCli extends Thread {
    private final String phase;
    private final GameControllerStates subPhase;
    private final Terminal terminal;
    private final Map<String, String> waitingRoom;
    private String userName;
    private GameServer gameServer;
    private Status status;
    private ClientStates state;

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.phase = null;
        this.subPhase = null;
        this.waitingRoom = new HashMap<>();
        this.terminal = TerminalBuilder.terminal();
        clearScreen(terminal, false);
    }

    public void run() {
        while (true) {
            switch (getClientState()) {
                case START_SCREEN -> {
                    // Splash screen printing and control
                    manageStartScreen();
                }

                case MAIN_MENU -> {
                    //wait for user input (game creation or join game)
                    manageMainMenu();
                }

                case GAME_CREATION -> {
                    //wait for user input (player number and difficulty)
                    //then wait for server reply
                    //transition to game login
                    manageGameCreation();
                }
                case JOIN_GAME -> {
                    //wait for user input (game code)
                    //then wait for server reply
                    //transition to game login
                    manageJoinGame();
                }
                case GAME_LOGIN -> {
                    //wait for user input (username)
                    //then wait for server reply
                    //transition to game waiting room
                    manageGameLogin();
                }
                case GAME_WAITINGROOM -> {
                    //wait for game start message from the server
                    //transition to game running
                    manageWaitingRoom();
                }
                case GAME_RUNNING -> {
                    //manage game logic
                    //when end game message arrives from the server -> transition to end game
                }
                case END_GAME -> {
                    //visualize end game screen
                    //transition to main menu
                }
                case EXIT -> {
                    clearScreen(terminal, true);
                    return;
                }
            }
        }
    }

    private void manageStartScreen() {
        System.out.print("\033[H\033[2J"); //clean terminal.
        do {
            SplashScreen.print(terminal);
            String hostIp = readLine(" ", terminal, new StringsCompleter("localhost", "127.0.0.1"), false, null);
            terminal.writer().print(ansi().restoreCursorPosition());
            terminal.writer().print(ansi().cursorMove(-18, 1));
            terminal.writer().print(ansi().saveCursorPosition());
            terminal.flush();
            try {
                int hostTcpPort = Integer.parseInt(readLine(terminal, new StringsCompleter("", "36803"), false, " "));
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
        System.out.print("\033[H\033[2J");
        do {
            MainMenuScreen.print(terminal);
            String option = readLine(terminal, new StringsCompleter("createGame", "joinGame"), false, " ");
            terminal.flush();
            switch (option) {
                case "createGame" -> this.setClientState(ClientStates.GAME_CREATION);
                case "joinGame" -> this.setClientState(ClientStates.JOIN_GAME);
                case "esc" -> this.setClientState(ClientStates.START_SCREEN);
                default -> printError(terminal, "Wrong command.");
            }
        } while (this.getClientState() == ClientStates.MAIN_MENU);
    }

    private void manageGameCreation() {
        System.out.print("\033[H\033[2J");
        String playersNumber;
        String difficulty;
        int expectedPlayers = 2;
        boolean expert = false;

        boolean wrongCommand = true;
        do {
            GameCreationScreen.print(terminal);
            playersNumber = readLine(terminal, new StringsCompleter("2", "3", "4"), false, " ");
            terminal.writer().print(ansi().restoreCursorPosition());
            terminal.writer().print(ansi().cursorMove(-4, 1));
            terminal.writer().print(ansi().saveCursorPosition());
            terminal.flush();
            switch (playersNumber) {
                case "2", "3", "4" -> {
                    wrongCommand = false;
                    expectedPlayers = Integer.parseInt(playersNumber);
                }
                case "esc" -> {
                    this.setClientState(ClientStates.MAIN_MENU);
                    return;
                }
            }


            difficulty = readLine(terminal, new StringsCompleter("easy", "expert"), false, " ");

            switch (difficulty) {
                case "easy" -> expert = false;
                case"expert" -> expert = true;
                case "esc" -> {
                    this.setClientState(ClientStates.MAIN_MENU);
                    return;
                }
                default -> wrongCommand = true;
            }
            if (wrongCommand) printError(terminal, "Wrong command.");
        } while (wrongCommand);

        this.setClientState(ClientStates.GAME_LOGIN);

        this.gameServer.sendCommand(MessageCreator.gameCreation(expectedPlayers, expert));
    }

    private void manageJoinGame() {
        System.out.print("\033[H\033[2J");
        JoinGameScreen.print(terminal);
        String gameCode = readLine(terminal, null, false, " ");
        terminal.flush();

        if(gameCode.equals("esc")) this.setClientState(ClientStates.MAIN_MENU);
        else this.gameServer.sendCommand(MessageCreator.EnterGame(gameCode));
    }

    private void manageGameLogin() {
        //TODO Print game login screen on cli.

        String username;
        username = readLine(" ", terminal, null, false, null);
        while (waitingRoom.containsKey(username) && waitingRoom.get(username).equals("connected")) {
            printError(terminal, "Username not valid.");
            username = readLine(" ", terminal, null, false, null);
        }
        //TODO Create and send login command to the server.
        //wait for serer reply.
    }

    private void manageWaitingRoom() {
        //TODO Print waiting room screen on cli.
    }

    private void manageGameRunning() {
        //TODO Print current status screen on cli.

        String command = readLine(" ", terminal, null, false, null);
        this.manageUserCommand(command);
    }

    private void manageEndGame() {
        //TODO Print end game screen on cli.

        String command = readLine(" ", terminal, null, false, null);
        while (!command.equals("exit")) {
            printError(terminal, "Wrong command.");
            command = readLine(" ", terminal, null, false, null);
        }
        this.setClientState(ClientStates.MAIN_MENU);
    }

    private void manageUserCommand(String command) {
    }

    public ClientStates getClientState() {
        return this.state;
    }

    public void setClientState(ClientStates newState) {
        this.state = newState;
    }

}
