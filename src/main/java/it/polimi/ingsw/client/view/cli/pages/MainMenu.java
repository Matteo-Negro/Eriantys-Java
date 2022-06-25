package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.Options;
import it.polimi.ingsw.client.view.cli.coordinates.OptionsInput;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Prints the MainMenu screen on the CLI.
 *
 * @author Riccardo Motta
 */
public class MainMenu {

    private static final String[] title = {
            " __       __                            ",
            "|  \\     /  \\                           ",
            "| ██\\   /  ██ ______  _______  __    __ ",
            "| ███\\ /  ███/      \\|       \\|  \\  |  \\",
            "| ████\\  ████  ██████\\ ███████\\ ██  | ██",
            "| ██\\██ ██ ██ ██    ██ ██  | ██ ██  | ██",
            "| ██ \\███| ██ ████████ ██  | ██ ██__/ ██",
            "| ██  \\█ | ██\\██     \\ ██  | ██\\██    ██",
            " \\██      \\██ \\███████\\██   \\██ \\██████ "
    };
    private static final String[] options = {
            "┌────────────────────────────────┐",
            "  Possible actions:               ",
            "  1) Create a new game            ",
            "  2) Enter an existing game code  ",
            "  Select (1 or 2):                ",
            "└────────────────────────────────┘"
    };

    private MainMenu() {
    }

    /**
     * Prints the whole game selection centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 17) / 2, (terminal.getWidth() - 40) / 2));
        terminal.writer().print(print());
        terminal.flush();
    }

    /**
     * Prints the whole game selection.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print() {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.a(moveCursor(Options.getInstance()));
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
        ansi.a(moveCursor(OptionsInput.getInstance()));
        ansi.a(foreground(White.getInstance()));
        return ansi;
    }
}
