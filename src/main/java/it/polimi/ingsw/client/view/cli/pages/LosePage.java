package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.coordinates.LoseNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.LoseOptions;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Lose page CLI printer.
 *
 * @author Matteo Negro
 */
public class LosePage {
    private static final String[] title = {
            " __      __  ______  __    __      __        ______   ______  ________ ",
            "|  \\    /  \\/      \\|  \\  |  \\    |  \\      /      \\ /      \\|        \\",
            " \\██\\  /  ██  ██████\\ ██  | ██    | ██     |  ██████\\  ██████\\\\████████",
            "  \\██\\/  ██| ██  | ██ ██  | ██    | ██     | ██  | ██ ██___\\██  | ██   ",
            "   \\██  ██ | ██  | ██ ██  | ██    | ██     | ██  | ██\\██    \\   | ██   ",
            "    \\████  | ██  | ██ ██  | ██    | ██     | ██  | ██_\\██████\\  | ██   ",
            "    | ██   | ██__/ ██ ██__/ ██    | ██_____| ██__/ ██  \\__| ██  | ██   ",
            "    | ██    \\██    ██\\██    ██    | ██     \\\\██    ██\\██    ██  | ██   ",
            "     \\██     \\██████  \\██████      \\████████ \\██████  \\██████    \\██   "
    };

    private LosePage() {
    }

    /**
     * Prints the lose page.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 9) / 2, (terminal.getWidth() - 71) / 2));
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
        ansi.a(moveCursor(LoseOptions.getInstance()));
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

        // Line #1

        ansi.a("┌─────────────────────────────────────┐");
        ansi.a(newLine());

        // Line #2

        ansi.a("  Press Enter to go back to the menu.  ");
        ansi.a(newLine());

        // Line #3

        ansi.a("└─────────────────────────────────────┘");
        ansi.cursor(0, 0);

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(LoseNewLine.getInstance());
    }
}
