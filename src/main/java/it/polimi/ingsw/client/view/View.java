package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.view.cli.Autocompletion;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.pages.*;
import it.polimi.ingsw.utilities.GraphicsType;
import it.polimi.ingsw.utilities.Pair;
import org.fusesource.jansi.Ansi;
import org.jline.builtins.Completers;
import org.jline.reader.History;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.*;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;
import static org.jline.builtins.Completers.TreeCompleter.node;

public class View {
    private final GraphicsType graphics;
    private final Terminal terminal;
    private final ClientController controller;
    private final History history;

    /**
     * Class constructor.
     *
     * @param graphics The graphic used (Cli/Gui).
     * @param controller The client game controller.
     * @throws IOException Thrown if autocompletion initialization doesn't work properly.
     */
    public View(GraphicsType graphics, ClientController controller) throws IOException {
        this.graphics = graphics;
        this.controller = controller;
        this.terminal = TerminalBuilder.terminal();
        Autocompletion.initialize(this.controller);
        this.history = new DefaultHistory();
    }

    /**
     * Method used to print the current game page.
     */
    static int waitingIteration = 0;

    public void printPage() {
        switch (this.graphics) {
            case CLI -> {
                switch (this.controller.getClientState()) {
                    case START_SCREEN -> SplashScreen.print(terminal);
                    case MAIN_MENU -> MainMenu.print(terminal);
                    case GAME_CREATION -> GameCreation.print(terminal);
                    case JOIN_GAME -> JoinGame.print(terminal);
                    case GAME_LOGIN -> Login.print(terminal, this.controller.getGameModel().getWaitingRoom(), this.controller.getGameModel().getPlayersNumber());
                    case GAME_WAITING_ROOM -> {
                        List<String> onlinePlayers = new ArrayList<>();
                        for (String name : this.controller.getGameModel().getWaitingRoom().keySet())
                            if (Boolean.TRUE.equals(this.controller.getGameModel().getWaitingRoom().get(name)))
                                onlinePlayers.add(name);

                        WaitingRoom.print(terminal, onlinePlayers, this.controller.getGameCode(), this.controller.getGameModel().getPlayersNumber(), waitingIteration++);
                    }
                    case GAME_RUNNING -> Game.print(terminal, this.controller.getGameModel(), this.controller.getGameCode(), this.controller.getGameModel().getRound(), this.controller.getGameModel().getPlayerByName(controller.getUserName()).isActive());
                    case END_GAME -> {
                        if (this.controller.getGameModel().isWinner()) WinPage.print(terminal);
                        else LosePage.print(terminal);
                    }
                }
            }

            //TODO manage gui screen transition.
            case GUI -> {
            }
        }
    }

    /**
     * Method used to clear the screen during screen update.
     *
     * @param def Parameter used by CLI clearScreen method.
     */
    public void clear(boolean def) {
        switch (this.graphics) {
            case CLI -> clearScreen(terminal, false);

            //TODO manage gui screen update.
            case GUI -> {
            }
        }
    }

    /**
     * This method is used in order to acquire a command written by the user.
     *
     * @param step The current page input step.
     * @return The command acquired.
     */
    public String acquire(int step) {
        String command = "";
        switch (this.graphics) {
            case CLI -> {
                switch (this.controller.getClientState()) {
                    case START_SCREEN -> {
                        switch (step) {
                            case 1 -> {
                                command = readLine(" ", terminal, List.of(node("localhost"), node("127.0.0.1")), false, null);
                                terminal.writer().print(ansi().restoreCursorPosition());
                                terminal.writer().print(ansi().cursorMove(-18, 1));
                                terminal.writer().print(ansi().saveCursorPosition());
                                terminal.flush();
                            }
                            case 2 -> command = readLine(" ", terminal, List.of(node("36803")), false, null);
                            default -> throw new IllegalArgumentException();
                        }
                    }

                    case MAIN_MENU -> command = readLine(" ", terminal, List.of(node("1"), node("2")), false, null);

                    case GAME_CREATION -> {
                        switch (step) {
                            case 1 -> {
                                command = readLine(" ", terminal, List.of(node("2"), node("3"), node("4"), node("exit")), false, null);
                                terminal.writer().print(ansi().restoreCursorPosition());
                                terminal.writer().print(ansi().cursorMove(-1, 1));
                                terminal.writer().print(ansi().saveCursorPosition());
                                terminal.flush();
                            }
                            case 2 -> command = readLine(" ", terminal, List.of(node("normal"), node("expert"), node("exit")), false, null);
                        }
                    }

                    case JOIN_GAME -> command = readLine(" ", terminal, List.of(node("exit")), false, null).toUpperCase(Locale.ROOT);

                    case GAME_LOGIN -> command = readLine(" ", terminal, playersToNodes(), false, null);

                    case END_GAME -> command = readLine(" ", terminal, List.of(node("exit")), false, null);

                    case GAME_WAITING_ROOM -> {
                    }
                    case GAME_RUNNING -> command = readLine(getPrettyUserName(), terminal, Autocompletion.get(), true, history).toLowerCase(Locale.ROOT);
                }
            }

            //TODO manage gui command acquisition.
            case GUI -> {
            }
        }

        return command;
    }

    private List<Completers.TreeCompleter.Node> playersToNodes() {
        List<Completers.TreeCompleter.Node> nodes = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : this.controller.getGameModel().getWaitingRoom().entrySet())
            if (Boolean.FALSE.equals(entry.getValue()))
                nodes.add(node(entry.getKey()));
        nodes.add(node("exit"));
        return Collections.unmodifiableList(nodes);
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
     * This method is used to print info on screen.
     *
     * @param message The message to print.
     */
    public void printInfo(Pair<String, String> message) {
        switch (this.graphics) {
            case CLI -> Utilities.printInfo(terminal, message.key(), message.value());

            //TODO manage gui info message.
            case GUI -> {
            }
        }
    }

    /**
     * This method is used to print an error on screen.
     *
     * @param message The error to print.
     */
    public void printError(String message) {
        switch (this.graphics) {
            case CLI -> Utilities.printError(terminal, message);

            //TODO manage gui error message.
            case GUI -> {
            }
        }

    }
}
