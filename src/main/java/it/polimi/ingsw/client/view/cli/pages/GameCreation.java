package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.GameCreationFirstInput;
import it.polimi.ingsw.client.view.cli.coordinates.GameCreationOptions;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Prints the GameCreation screen on the CLI.
 *
 * @author Riccardo Motta
 */
public class GameCreation {

    private static final String[] title = {
            "  ______                                                                            __     __                   ",
            " /      \\                                                                          |  \\   |  \\                  ",
            "|  ██████\\ ______  ______ ____   ______        _______  ______   ______   ______  _| ██_   \\██ ______  _______  ",
            "| ██ __\\██|      \\|      \\    \\ /      \\      /       \\/      \\ /      \\ |      \\|   ██ \\ |  \\/      \\|       \\ ",
            "| ██|    \\ \\██████\\ ██████\\████\\  ██████\\    |  ███████  ██████\\  ██████\\ \\██████\\\\██████ | ██  ██████\\ ███████\\",
            "| ██ \\████/      ██ ██ | ██ | ██ ██    ██    | ██     | ██   \\██ ██    ██/      ██ | ██ __| ██ ██  | ██ ██  | ██",
            "| ██__| ██  ███████ ██ | ██ | ██ ████████    | ██_____| ██     | ████████  ███████ | ██|  \\ ██ ██__/ ██ ██  | ██",
            " \\██    ██\\██    ██ ██ | ██ | ██\\██     \\     \\██     \\ ██      \\██     \\\\██    ██  \\██  ██ ██\\██    ██ ██  | ██",
            "  \\██████  \\███████\\██  \\██  \\██ \\███████      \\███████\\██       \\███████ \\███████   \\████ \\██ \\██████ \\██   \\██"
    };
    private static final String[] options = {
            "┌──────────────────────────────────────┐",
            "  Number of players (2, 3 or 4):        ",
            "  Game mode (normal or expert):         ",
            "└──────────────────────────────────────┘"
    };

    private GameCreation() {
    }

    /**
     * Prints the whole game creation centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 15) / 2, (terminal.getWidth() - 112) / 2));
        terminal.writer().print(print());
        terminal.flush();
    }

    /**
     * Prints the whole game creation.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print() {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.a(moveCursor(GameCreationOptions.getInstance()));
        ansi.a(printOptions());
        return ansi;
    }

    /**
     * Prints the title.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi printTitle() {
        Ansi ansi = new Ansi();
        ansi.a(foreground(Title.getInstance()));
        ansi.a(printText(title));
        return ansi;
    }

    /**
     * Prints the options.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi printOptions() {
        Ansi ansi = new Ansi();
        ansi.a(foreground(Grey.getInstance()));
        ansi.a(printText(options));
        ansi.a(moveCursor(GameCreationFirstInput.getInstance()));
        ansi.a(foreground(White.getInstance()));
        return ansi;
    }
}
