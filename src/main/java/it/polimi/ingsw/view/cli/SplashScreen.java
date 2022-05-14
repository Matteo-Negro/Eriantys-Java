package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.cli.colours.Grey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

//  ________          __                     __
// |        \        |  \                   |  \
// | ████████ ______  \██ ______  _______  _| ██_   __    __  _______
// | ██__    /      \|  \|      \|       \|   ██ \ |  \  |  \/       \
// | ██  \  |  ██████\ ██ \██████\ ███████\\██████ | ██  | ██  ███████
// | █████  | ██   \██ ██/      ██ ██  | ██ | ██ __| ██  | ██\██    \
// | ██_____| ██     | ██  ███████ ██  | ██ | ██|  \ ██__/ ██_\██████\
// | ██     \ ██     | ██\██    ██ ██  | ██  \██  ██\██    ██       ██
//  \████████\██      \██ \███████\██   \██   \████ _\███████\███████
//                                                 |  \__| ██
//                                                  \██    ██
//                                                   \██████

public class SplashScreen {

    private SplashScreen() {
    }

    /**
     * Prints the whole splash screen centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 21) / 2, (terminal.getWidth() - 67) / 2));
        terminal.writer().print(printSplashScreen());
        terminal.flush();
    }

    /**
     * Prints the whole splash screen.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi printSplashScreen() {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.a(moveCursor(it.polimi.ingsw.view.cli.coordinates.Subtitle.getInstance()));
        ansi.a(printSubtitle());
        ansi.a(moveCursor(ServerSettings.getInstance()));
        ansi.a(printSettings());
        return ansi;
    }

    /**
     * Prints the title.
     */
    static Ansi printTitle() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(it.polimi.ingsw.view.cli.colours.Title.getInstance()));

        // Line #1

        ansi.a(" ________          __                     __                       ");
        ansi.a(newLine(true));

        // Line #2

        ansi.a("|        \\        |  \\                   |  \\                      ");
        ansi.a(newLine(true));

        // Line #3

        ansi.a("| ████████ ______  \\██ ______  _______  _| ██_   __    __  _______ ");
        ansi.a(newLine(true));

        // Line #4

        ansi.a("| ██__    /      \\|  \\|      \\|       \\|   ██ \\ |  \\  |  \\/       \\");
        ansi.a(newLine(true));

        // Line #5

        ansi.a("| ██  \\  |  ██████\\ ██ \\██████\\ ███████\\\\██████ | ██  | ██  ███████");
        ansi.a(newLine(true));

        // Line #6

        ansi.a("| █████  | ██   \\██ ██/      ██ ██  | ██ | ██ __| ██  | ██\\██    \\ ");
        ansi.a(newLine(true));

        // Line #7

        ansi.a("| ██_____| ██     | ██  ███████ ██  | ██ | ██|  \\ ██__/ ██_\\██████\\");
        ansi.a(newLine(true));

        // Line #8

        ansi.a("| ██     \\ ██     | ██\\██    ██ ██  | ██  \\██  ██\\██    ██       ██");
        ansi.a(newLine(true));

        // Line #9

        ansi.a(" \\████████\\██      \\██ \\███████\\██   \\██   \\████ _\\███████\\███████ ");
        ansi.a(newLine(true));

        // Line #10

        ansi.a("                                                |  \\__| ██         ");
        ansi.a(newLine(true));

        // Line #11

        ansi.a("                                                 \\██    ██         ");
        ansi.a(newLine(true));

        // Line #12

        ansi.a("                                                  \\██████          ");
        ansi.a(resetCursor(true));

        return ansi;
    }

    /**
     * Prints the subtitle.
     */
    static Ansi printSubtitle() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(it.polimi.ingsw.view.cli.colours.Subtitle.getInstance()));

        ansi.a(moveCursor(Subtitle1.getInstance()));
        ansi.a(bold(true));
        ansi.a("A game by Leo Colovini for 2-4 players.");
        ansi.a(bold(false));

        ansi.a(moveCursor(Subtitle2.getInstance()));
        ansi.a("Project developed by Riccardo Milici, Riccardo Motta, Matteo Negro.");

        ansi.a(resetCursor(false));

        return ansi;
    }

    /**
     * Prints the server settings.
     */
    static Ansi printSettings() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(Grey.getInstance()));

        ansi.a("┌─────────────────────────────────────────────────────────────────┐");
        ansi.a(newLine(false));
        ansi.a("  Server address (IP or domain):                                   ");
        ansi.a(newLine(false));
        ansi.a("  Server port:                                                     ");
        ansi.a(newLine(false));
        ansi.a("└─────────────────────────────────────────────────────────────────┘");
        ansi.a(moveCursor(ServerSettingsFirstInput.getInstance()));

        ansi.a(foreground(White.getInstance()));

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param title True if title, false otherwise.
     */
    private static Ansi newLine(boolean title) {
        if (title)
            return moveCursor(TitleNewLine.getInstance());
        else
            return moveCursor(ServerSettingsNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param title True if title, false otherwise.
     */
    private static Ansi resetCursor(boolean title) {
        if (title)
            return moveCursor(TitleReset.getInstance());
        else
            return moveCursor(SubtitleReset.getInstance());
    }
}
