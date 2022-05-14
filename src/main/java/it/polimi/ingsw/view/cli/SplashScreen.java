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

    private static final String[] title = {
            " ________          __                     __                       ",
            "|        \\        |  \\                   |  \\                      ",
            "| ████████ ______  \\██ ______  _______  _| ██_   __    __  _______ ",
            "| ██__    /      \\|  \\|      \\|       \\|   ██ \\ |  \\  |  \\/       \\",
            "| ██  \\  |  ██████\\ ██ \\██████\\ ███████\\\\██████ | ██  | ██  ███████",
            "| █████  | ██   \\██ ██/      ██ ██  | ██ | ██ __| ██  | ██\\██    \\ ",
            "| ██_____| ██     | ██  ███████ ██  | ██ | ██|  \\ ██__/ ██_\\██████\\",
            "| ██     \\ ██     | ██\\██    ██ ██  | ██  \\██  ██\\██    ██       ██",
            " \\████████\\██      \\██ \\███████\\██   \\██   \\████ _\\███████\\███████ ",
            "                                                |  \\__| ██         ",
            "                                                 \\██    ██         ",
            "                                                  \\██████          "
    };

    private SplashScreen() {
    }

    /**
     * Prints the whole splash screen centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 21) / 2, (terminal.getWidth() - 67) / 2));
        terminal.writer().print(print());
        terminal.flush();
    }

    /**
     * Prints the whole splash screen.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print() {
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
        ansi.a(printTile(title));

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

        ansi.a(resetCursor());

        return ansi;
    }

    /**
     * Prints the server settings.
     */
    static Ansi printSettings() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(Grey.getInstance()));

        ansi.a("┌─────────────────────────────────────────────────────────────────┐");
        ansi.a(newLine());
        ansi.a("  Server address (IP or domain):                                   ");
        ansi.a(newLine());
        ansi.a("  Server port:                                                     ");
        ansi.a(newLine());
        ansi.a("└─────────────────────────────────────────────────────────────────┘");
        ansi.a(moveCursor(ServerSettingsFirstInput.getInstance()));

        ansi.a(foreground(White.getInstance()));

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     */
    private static Ansi newLine() {
        return moveCursor(ServerSettingsNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     */
    private static Ansi resetCursor() {
        return moveCursor(SubtitleReset.getInstance());
    }
}
