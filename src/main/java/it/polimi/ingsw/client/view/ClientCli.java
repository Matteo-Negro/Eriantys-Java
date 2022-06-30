package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.Pair;
import org.fusesource.jansi.Ansi;
import org.jline.builtins.Completers;
import org.jline.reader.History;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

/**
 * This is the main view class which manages the CLI graphics' operations.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class ClientCli implements Runnable, View {

    /**
     * Manages the waiting-room-screen's output.
     */
    static int waitingIteration = 0;
    private final ClientController controller;
    private final Terminal terminal;
    private final History history;
    private boolean process;

    /**
     * Default constructor.
     *
     * @throws IOException Thrown if an error occurs during the terminal building.
     */
    public ClientCli() throws IOException {
        this.terminal = TerminalBuilder.terminal();
        this.controller = new ClientController(this);
        Autocompletion.initialize(this.controller);
        this.history = new DefaultHistory();
        updateScreen(false);
        this.process = true;
    }

    /**
     * Ends this thread's process setting the "process" attribute to false.
     */
    private void endProcess() {
        this.process = false;
    }

    /**
     * The main client method, chooses the correct method to invoke basing on the current controller's state.
     */
    @Override
    public void run() {
        try {
            while (this.process) {
                switch (this.controller.getClientState()) {
                    case START_SCREEN -> runStartScreen();
                    case MAIN_MENU -> runMainMenu();
                    case GAME_CREATION -> runGameCreation();
                    case JOIN_GAME -> runJoinGame();
                    case GAME_LOGIN -> runGameLogin();
                    case GAME_WAITING_ROOM -> runWaitingRoom();
                    case GAME_RUNNING -> runGameRunning();
                    case END_GAME -> runEndGame();
                    case CONNECTION_LOST -> this.controller.manageConnectionLost();
                    case EXIT -> endProcess();
                }
            }
        } catch (Exception e) {
            Log.error(e);
        } finally {
            updateScreen(true);
            if (this.controller.getGameServer() != null)
                this.controller.getGameServer().disconnected();
        }
    }

    /**
     * Manages the start-screen's I/O.
     */
    private void runStartScreen() {
        SplashScreen.print(terminal);

        int hostTcpPort;
        String hostIp = readLine(" ", terminal, List.of(node("localhost"), node("127.0.0.1")), false, null);

        if (hostIp.isBlank())
            hostIp = "localhost";

        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-18, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();

        try {
            hostTcpPort = Integer.parseInt(readLine(" ", terminal, List.of(node("36803")), false, null));
        } catch (NumberFormatException e) {
            hostTcpPort = 36803;
        }

        try {
            this.controller.manageStartScreen(new Socket(hostIp, hostTcpPort));
        } catch (Exception e) {
            Log.warning(e);
            this.controller.errorOccurred("Wrong data provided or server unreachable.");
        }
    }

    /**
     * Manages the main-menu-screen's I/O.
     */
    private void runMainMenu() {
        MainMenu.print(terminal);
        try {
            this.controller.manageMainMenu(readLine(" ", terminal, List.of(node("1"), node("2"), node("exit")), false, null));
        } catch (UserInterruptException e) {
            updateScreen(false);
            controller.manageConnectionLost();
        }
    }

    /**
     * Manages the game-creation-screen's I/O.
     */
    private void runGameCreation() {
        int expectedPlayers;
        boolean expert;

        GameCreation.print(terminal);
        String playersNumber;
        try {
            playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);
        } catch (UserInterruptException e) {
            manageExit();
            return;
        }
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-1, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        switch (playersNumber) {
            case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
            case "exit" -> {
                if (!controller.getClientState().equals(ClientState.CONNECTION_LOST))
                    this.controller.setClientState(ClientState.MAIN_MENU);
                updateScreen(false);
                this.controller.resetGame();
                return;
            }
            default -> {
                this.controller.errorOccurred("Wrong command.");
                return;
            }
        }

        String difficulty;
        try {
            difficulty = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("exit")), false, null);
        } catch (UserInterruptException e) {
            manageExit();
            return;
        }
        switch (difficulty) {
            case "normal" -> expert = false;
            case "expert" -> expert = true;
            case "exit" -> {
                if (!controller.getClientState().equals(ClientState.CONNECTION_LOST))
                    this.controller.setClientState(ClientState.MAIN_MENU);
                updateScreen(false);
                this.controller.resetGame();
                return;
            }
            default -> {
                this.controller.errorOccurred("Wrong command.");
                return;
            }
        }
        this.controller.manageGameCreation(MessageCreator.gameCreation(expectedPlayers, expert));
        updateScreen(false);
    }

    /**
     * Manages the join-game-screen's I/O.
     */
    private void runJoinGame() {
        JoinGame.print(terminal);
        try {
            this.controller.manageJoinGame(readLine(" ", terminal, List.of(node("exit")), false, null).toUpperCase(Locale.ROOT));
        } catch (UserInterruptException e) {
            manageExit();
        }
    }

    /**
     * Manages the login-screen's I/O.
     */
    private void runGameLogin() {
        Login.print(terminal, this.controller.getGameModel().getWaitingRoom(), this.controller.getGameModel().getPlayersNumber());
        try {
            this.controller.manageGameLogin(readLine(" ", terminal, playersToNodes(), false, null));
        } catch (UserInterruptException e) {
            manageExit();
        }
    }

    /**
     * Manages the waiting room.
     */
    private void runWaitingRoom() {
        synchronized (this.controller.getLock()) {
            if (this.controller.getGameModel() != null) {
                List<String> onlinePlayers = new ArrayList<>();
                for (String name : this.controller.getGameModel().getWaitingRoom().keySet())
                    if (Boolean.TRUE.equals(this.controller.getGameModel().getWaitingRoom().get(name)))
                        onlinePlayers.add(name);
                WaitingRoom.print(terminal, onlinePlayers, this.controller.getGameCode(), this.controller.getGameModel().getPlayersNumber(), waitingIteration++);
                if (Utilities.pause(1000, terminal)) {
                    controller.getGameServer().sendCommand(MessageCreator.logout());
                    controller.resetGame();
                    manageExit();
                }
            }
        }
        updateScreen(false);
    }

    /**
     * Manages the game-screen's I/O.
     */
    private void runGameRunning() {

        synchronized (this.controller.getLock()) {
            if (this.controller.getGameServer() != null) {

                Game.print(terminal,
                        this.controller.getGameModel(),
                        this.controller.getGameCode(),
                        this.controller.getGameModel().getRound(),
                        this.controller.getGameModel().getPlayerByName(controller.getUserName()).isActive()
                );

                if (this.controller.hasCommunicationToken() && !this.controller.getClientState().equals(ClientState.CONNECTION_LOST)) {
                    String command;
                    try {
                        command = readLine(getPrettyUserName(), terminal, Autocompletion.get(), true, history).toLowerCase(Locale.ROOT);
                    } catch (UserInterruptException e) {
                        command = "logout";
                    }
                    terminal.writer().print(foreground(White.getInstance()));
                    terminal.writer().print(background(Black.getInstance()));
                    terminal.flush();
                    if (!command.equals("")) {
                        this.controller.manageGameRunning(command);
                        return;
                    }
                } else if (Utilities.pause(1000, terminal))
                    this.controller.manageGameRunning("logout");
            }
        }

        updateScreen(false);

        if (controller.getClientState().equals(ClientState.GAME_WAITING_ROOM))
            showError("One or more users disconnected.");
    }

    /**
     * Manages the end-game-screen's I/O.
     */
    private void runEndGame() {
        switch (this.controller.getEndState()) {
            case DRAW -> DrawPage.print(terminal);
            case LOSE -> LosePage.print(terminal);
            case WIN -> WinPage.print(terminal);
        }
        this.controller.manageEndGame(readLine(" ", terminal, List.of(node("exit")), false, null));
    }

    /**
     * This method is a visualization helper for CLI command acquisition.
     *
     * @return The decorated username.
     */
    private String getPrettyUserName() {
        Ansi ansi = new Ansi();
        ansi.a(" ");
        ansi.a(foreground(switch (this.controller.getGameModel().getPlayerByName(controller.getUserName()).getWizard()) {
            case FUCHSIA -> WizardFuchsia.getInstance();
            case GREEN -> WizardGreen.getInstance();
            case WHITE -> WizardWhite.getInstance();
            case YELLOW -> WizardYellow.getInstance();
        }));
        ansi.a(bold(true));
        ansi.a(controller.getUserName());
        ansi.a(bold(false));
        ansi.a(foreground(Grey.getInstance()));
        ansi.a(" > ");
        ansi.a(foreground(White.getInstance()));
        return ansi.toString();
    }

    /**
     * Clears the terminal screen.
     *
     * @param def Parameter required by the clearScreen method.
     */
    public void updateScreen(boolean def) {
        clearScreen(terminal, def);
    }

    /**
     * This method is a helper for the runGameLogin method, it grants username autocompletion.
     *
     * @return The completer required by autocompletion.
     */
    private List<Completers.TreeCompleter.Node> playersToNodes() {
        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : this.controller.getGameModel().getWaitingRoom().entrySet())
            if (Boolean.FALSE.equals(entry.getValue()))
                nodes.add(node(entry.getKey()));
        nodes.add(node("exit"));
        return Collections.unmodifiableList(nodes);
    }

    private void manageExit() {
        updateScreen(false);
        controller.setClientState(ClientState.MAIN_MENU);
    }

    /**
     * Prints an error on screen.
     *
     * @param message The message to show.
     */
    public void showError(String message) {
        Utilities.printError(terminal, message);
    }

    /**
     * Prints an info on screen.
     *
     * @param info The info to show.
     */
    public void showInfo(Pair<String, String> info) {
        Utilities.printInfo(terminal, info.first(), info.second());
    }

}
