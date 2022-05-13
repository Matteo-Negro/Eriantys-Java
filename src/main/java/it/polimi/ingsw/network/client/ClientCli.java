package it.polimi.ingsw.network.client;

import it.polimi.ingsw.clientController.GameServer;
import it.polimi.ingsw.clientStatus.Status;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.SplashScreen;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

public class ClientCli extends Thread {
    private String userName;
    private GameServer gameServer;
    private Status status;
    private ClientStates state;
    private final String phase;
    private final GameControllerStates subPhase;
    private final Terminal terminal;

    /**
     * Default constructor.
     */
    public ClientCli() throws IOException {
        this.state = ClientStates.START_SCREEN;
        this.phase = null;
        this.subPhase = null;

        this.terminal = TerminalBuilder.terminal();
        clearScreen(terminal, false);
        // Realm printing
        // Realm.print(terminal);

        // SchoolBoard
        //test(terminal);

        // clearScreen(terminal, true);
    }

    private void initializeMapInteger(Map<HouseColor, Integer> map) {
        for (HouseColor color : HouseColor.values())
            map.put(color, 0);
    }

    private void initializeMapBoolean(Map<HouseColor, Boolean> map) {
        for (HouseColor color : HouseColor.values())
            map.put(color, false);
    }

    public void run() {
        System.out.println("\n * Client is attempting to connect to the server.");

        /*try {
            Socket hostSocket = new Socket(serverIp, serverPort);
            this.gameServer = new GameServer(hostSocket, this);
            this.gameServer.start();
            System.out.println("\n * Client has successfully connected to the server.");
        } catch (IOException ioe) {
            this.setClientState(ClientStates.CONNECTION_LOST);
        }*/

        while (true) {
            switch (getClientState()) {
                case START_SCREEN -> {
                    // Splash screen printing and control
                    SplashScreen.print(terminal);
                    String hostIp = readLine(terminal, new StringsCompleter("localhost", "127.0.0.1"), false, " ");
                    terminal.writer().print(ansi().restoreCursorPosition());
                    terminal.writer().print(ansi().cursorMove(-18, 1));
                    terminal.writer().print(ansi().saveCursorPosition());
                    terminal.flush();
                    int hostTcpPort = Integer.parseInt(readLine(terminal, new StringsCompleter("", "36803"), false, " "));
                    try {
                        this.gameServer = new GameServer(new Socket(hostIp, hostTcpPort), this);
                        setClientState(ClientStates.MAIN_MENU);

                    } catch (IOException e) {
                        printError(terminal, "Wrong data provided or server unreachable.");
                    }
                }

                case MAIN_MENU -> {
                    //wait for user input (game creation or join game)
                }
                case GAME_CREATION -> {
                    //wait for user input (player number and difficulty)
                    //then wait for server reply
                    //transition to game login
                }
                case JOIN_GAME -> {
                    //wait for user input (game code)
                    //then wait for server reply
                    //transition to game login
                }
                case GAME_LOGIN -> {
                    //wait for user input (username)
                    //then wait for server reply
                    //transition to game waiting room
                }
                case GAME_WAITINGROOM -> {
                    //wait for game start message from the server
                    //transition to game running
                }
                case GAME_RUNNING -> {
                    //manage game logic
                    //when end game message arrives from the server -> transition to end game
                }
                case END_GAME -> {
                    //visualize end game screen
                    //transition to main menu
                }
            }
        }

        /*while (true) {
            while (!gameServer.isConnected()) {
                try {
                    gameServer.wait();
                } catch (InterruptedException ie) {
                    this.setClientState(ClientStates.CONNECTION_LOST);
                    break;
                }
            }*/
    }


    public void setClientState(ClientStates newState) {
        this.state = newState;
    }

    public ClientStates getClientState() {
        return this.state;
    }

}
