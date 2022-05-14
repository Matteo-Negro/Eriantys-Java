package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.cli.colours.Grey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.view.cli.Utilities.*;
import static it.polimi.ingsw.view.cli.Utilities.moveCursor;
import static org.fusesource.jansi.Ansi.ansi;

public class JoinGameScreen {
    private JoinGameScreen() {
    }

    /**
     * Prints the whole splash screen centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 21) / 2, (terminal.getWidth() - 67) / 2));
        terminal.writer().print(printJoinGameScreen());
        terminal.flush();
    }

    /**
     * Prints the whole splash screen.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi printJoinGameScreen() {
        Ansi ansi = new Ansi();
        printTitle(ansi);
        moveCursor(ansi, it.polimi.ingsw.view.cli.coordinates.Subtitle.getInstance());
        printSubtitle(ansi);
        ansi.cursorMove(42,4);
        printSettings(ansi);
        return ansi;
    }

    /**
     * Prints the title.
     *
     * @param ansi Ansi stream where to write.
     */
    static void printTitle(Ansi ansi) {

        foreground(ansi, it.polimi.ingsw.view.cli.colours.Title.getInstance());

        // Line #1

        ansi.append(" ________          __                     __                       ");
        newLine(ansi, true);

        // Line #2

        ansi.append("|        \\        |  \\                   |  \\                      ");
        newLine(ansi, true);

        // Line #3

        ansi.append("| ████████ ______  \\██ ______  _______  _| ██_   __    __  _______ ");
        newLine(ansi, true);

        // Line #4

        ansi.append("| ██__    /      \\|  \\|      \\|       \\|   ██ \\ |  \\  |  \\/       \\");
        newLine(ansi, true);

        // Line #5

        ansi.append("| ██  \\  |  ██████\\ ██ \\██████\\ ███████\\\\██████ | ██  | ██  ███████");
        newLine(ansi, true);

        // Line #6

        ansi.append("| █████  | ██   \\██ ██/      ██ ██  | ██ | ██ __| ██  | ██\\██    \\ ");
        newLine(ansi, true);

        // Line #7

        ansi.append("| ██_____| ██     | ██  ███████ ██  | ██ | ██|  \\ ██__/ ██_\\██████\\");
        newLine(ansi, true);

        // Line #8

        ansi.append("| ██     \\ ██     | ██\\██    ██ ██  | ██  \\██  ██\\██    ██       ██");
        newLine(ansi, true);

        // Line #9

        ansi.append(" \\████████\\██      \\██ \\███████\\██   \\██   \\████ _\\███████\\███████ ");
        newLine(ansi, true);

        // Line #10

        ansi.append("                                                |  \\__| ██         ");
        newLine(ansi, true);

        // Line #11

        ansi.append("                                                 \\██    ██         ");
        newLine(ansi, true);

        // Line #12

        ansi.append("                                                  \\██████          ");
        resetCursor(ansi, true);
    }

    /**
     * Prints the subtitle.
     *
     * @param ansi Ansi stream where to write.
     */
    static void printSubtitle(Ansi ansi) {

        foreground(ansi, it.polimi.ingsw.view.cli.colours.Subtitle.getInstance());

        moveCursor(ansi, SubtitleGameCreation.getInstance());
        bold(ansi, true);
        ansi.append("- JOIN A GAME -");
        bold(ansi, false);
        resetCursor(ansi, false);
    }

    /**
     * Prints the server settings.
     *
     * @param ansi Ansi stream where to write.
     */
    static void printSettings(Ansi ansi) {
        foreground(ansi, Grey.getInstance());

        ansi.append("┌───────────────────────────────┐");
        newLine(ansi, false);
        ansi.append("  Insert the gamecode:           ");
        newLine(ansi, false);
        ansi.append("└───────────────────────────────┘");
        ansi.cursorMove(-11, -1);

        foreground(ansi, White.getInstance());
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi  Ansi stream where to write.
     * @param title True if title, false otherwise.
     */
    private static void newLine(Ansi ansi, boolean title) {
        if (title)
            moveCursor(ansi, TitleNewLine.getInstance());
        else
            ansi.cursorMove(-33,1);
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi  Ansi stream where to write.
     * @param title True if title, false otherwise.
     */
    private static void resetCursor(Ansi ansi, boolean title) {
        if (title)
            moveCursor(ansi, TitleReset.getInstance());
        else
            moveCursor(ansi, SubtitleReset.getInstance());
    }
}
