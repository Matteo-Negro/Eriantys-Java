package it.polimi.ingsw.network.client;

import com.google.gson.JsonObject;
import it.polimi.ingsw.clientController.GameServer;
import it.polimi.ingsw.clientStatus.Status;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.view.cli.MainMenu;
import it.polimi.ingsw.view.cli.SplashScreen;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
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
        do {
            SplashScreen.print(terminal);
            String hostIp = readLine(" ", terminal, new StringsCompleter("localhost", "127.0.0.1"), false, null);
            terminal.writer().print(ansi().restoreCursorPosition());
            terminal.writer().print(ansi().cursorMove(-18, 1));
            terminal.writer().print(ansi().saveCursorPosition());
            terminal.flush();
            int hostTcpPort = Integer.parseInt(readLine(" ", terminal, new StringsCompleter("", "36803"), false, null));
            try (Socket hostSocket = new Socket(hostIp, hostTcpPort)) {
                hostSocket.setSoTimeout(10000);
                this.gameServer = new GameServer(hostSocket, this);
                gameServer.start();
                setClientState(ClientStates.MAIN_MENU);
            } catch (IOException e) {
                printError(terminal, "Wrong data provided or server unreachable.");
            }
        } while (gameServer == null);
        clearScreen(terminal, false);
    }

    private void manageMainMenu() {
        MainMenu.print(terminal);
        int option;
        try {
            option = Integer.parseInt(readLine(" ", terminal, null, false, null));
        } catch (NumberFormatException e) {
            option = 0;
        }
        switch (option) {
            case 1 -> this.setClientState(ClientStates.GAME_CREATION);
            case 2 -> this.setClientState(ClientStates.JOIN_GAME);
            default -> printError(terminal, "Wrong command.");
        }
        if (!this.getClientState().equals(ClientStates.MAIN_MENU))
            clearScreen(terminal, false);
    }

    private void manageGameCreation() {
        //TODO Print game creation screen on cli.

        int playersNumber = Integer.parseInt(readLine(" ", terminal, null, false, null));
        String difficulty = readLine(" ", terminal, null, false, null);
        //TODO create and send gameCreation command to the server.
        //wait for server reply
    }

    private void manageJoinGame() {
        //TODO Print join game screen on cli.

        String gameCode = readLine(" ", terminal, null, false, null);
        //TODO Create and send joinGame command to the server.
        //wait for server reply
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

}
