package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.GameCode;
import it.polimi.ingsw.client.view.cli.coordinates.GameCodeInput;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Prints the JoinGame screen on the CLI.
 *
 * @author Riccardo Motta
 */
public class JoinGame {

    private static final String[] title = {
            "    _____          __                                                       ",
            "   |     \\        |  \\                                                      ",
            "    \\█████ ______  \\██_______        ______   ______  ______ ____   ______  ",
            "      | ██/      \\|  \\       \\      /      \\ |      \\|      \\    \\ /      \\ ",
            " __   | ██  ██████\\ ██ ███████\\    |  ██████\\ \\██████\\ ██████\\████\\  ██████\\",
            "|  \\  | ██ ██  | ██ ██ ██  | ██    | ██  | ██/      ██ ██ | ██ | ██ ██    ██",
            "| ██__| ██ ██__/ ██ ██ ██  | ██    | ██__| ██  ███████ ██ | ██ | ██ ████████",
            " \\██    ██\\██    ██ ██ ██  | ██     \\██    ██\\██    ██ ██ | ██ | ██\\██     \\",
            "  \\██████  \\██████ \\██\\██   \\██     _\\███████ \\███████\\██  \\██  \\██ \\███████",
            "                                   |  \\__| ██                               ",
            "                                    \\██    ██                               ",
            "                                     \\██████                                "
    };
    private static final String[] options = {
            "┌──────────────────┐",
            "  Game code:        ",
            "└──────────────────┘"
    };

    private JoinGame() {
    }

    /**
     * Prints the whole page centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 17) / 2, (terminal.getWidth() - 76) / 2));
        terminal.writer().print(print());
        terminal.flush();
    }

    /**
     * Prints the whole page.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print() {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.a(moveCursor(GameCode.getInstance()));
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
        ansi.a(moveCursor(GameCodeInput.getInstance()));
        ansi.a(foreground(White.getInstance()));
        return ansi;
    }
}
