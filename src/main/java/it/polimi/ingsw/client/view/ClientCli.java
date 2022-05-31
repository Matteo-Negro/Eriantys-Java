package it.polimi.ingsw.client.view;

import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.exceptions.ExitException;
import org.fusesource.jansi.Ansi;
import org.jline.builtins.Completers;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class ClientCli implements View {
    private final ClientController controller;
    private final Terminal terminal;
    private final History history;

    public ClientCli(ClientController controller) throws IOException {
        this.controller = controller;
        this.terminal = TerminalBuilder.terminal();
        Autocompletion.initialize(this.controller);
        this.history = new DefaultHistory();
    }

    public Socket runStartScreen() throws IOException {
        SplashScreen.print(terminal);
        String hostIp = readLine(" ", terminal, List.of(node("localhost"), node("127.0.0.1")), false, null);
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-18, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        int hostTcpPort = Integer.parseInt(readLine(" ", terminal, List.of(node("36803")), false, null));

        return new Socket(hostIp, hostTcpPort);
    }

    public String runMainMenu() {
        MainMenu.print(terminal);
        return readLine(" ", terminal, List.of(node("1"), node("2")), false, null);
    }

    public JsonObject runGameCreation() throws ExitException, IllegalArgumentException {
        int expectedPlayers;
        boolean expert;

        GameCreation.print(terminal);
        String playersNumber = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);
        terminal.writer().print(ansi().restoreCursorPosition());
        terminal.writer().print(ansi().cursorMove(-1, 1));
        terminal.writer().print(ansi().saveCursorPosition());
        terminal.flush();
        switch (playersNumber) {
            case "2", "3", "4" -> expectedPlayers = Integer.parseInt(playersNumber);
            case "exit" -> throw new ExitException();
            default -> throw new IllegalArgumentException();
        }

        String difficulty = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("exit")), false, null);
        switch (difficulty) {
            case "normal" -> expert = false;
            case "expert" -> expert = true;
            case "exit" -> throw new ExitException();
            default -> throw new IllegalArgumentException();
        }
        return MessageCreator.gameCreation(expectedPlayers, expert);
    }


    public String runJoinGame() {
        JoinGame.print(terminal);
        return readLine(" ", terminal, List.of(node("exit")), false, null).toUpperCase(Locale.ROOT);
    }


    public String runGameLogin() {
        Login.print(terminal, this.controller.getGameModel().getWaitingRoom(), this.controller.getGameModel().getPlayersNumber());
        return readLine(" ", terminal, playersToNodes(), false, null);
    }


    static int waitingIteration = 0;

    public void runWaitingRoom() {
        List<String> onlinePlayers = new ArrayList<>();
        for (String name : this.controller.getGameModel().getWaitingRoom().keySet())
            if (Boolean.TRUE.equals(this.controller.getGameModel().getWaitingRoom().get(name)))
                onlinePlayers.add(name);

        WaitingRoom.print(terminal, onlinePlayers, this.controller.getGameCode(), this.controller.getGameModel().getPlayersNumber(), waitingIteration++);
    }


    public String runGameRunning() {
        Game.print(terminal, this.controller.getGameModel(), this.controller.getGameCode(), this.controller.getGameModel().getRound(), this.controller.getGameModel().getPlayerByName(controller.getUserName()).isActive());

        if (this.controller.hasCommunicationToken())
            return readLine(getPrettyUserName(), terminal, Autocompletion.get(), true, history).toLowerCase(Locale.ROOT);
        return null;
    }


    public String runEndGame() {
        if (this.controller.getGameModel().isWinner()) WinPage.print(terminal);
        else LosePage.print(terminal);
        return readLine(" ", terminal, List.of(node("exit")), false, null);
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


    public void updateScreen(boolean def) {
        clearScreen(terminal, def);
    }

    private List<Completers.TreeCompleter.Node> playersToNodes() {
        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : this.controller.getGameModel().getWaitingRoom().entrySet())
            if (Boolean.FALSE.equals(entry.getValue()))
                nodes.add(node(entry.getKey()));
        nodes.add(node("exit"));
        return Collections.unmodifiableList(nodes);
    }

    public void printError(String message) {
        Utilities.printError(terminal, message);
    }

    public void printInfo(Pair<String, String> info) {
        Utilities.printInfo(terminal, info.key(), info.value());
    }

}
