package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Prints the SplashScreen screen on the CLI.
 *
 * @author Riccardo Motta
 */
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
    private static final String[] settings = {
            "┌─────────────────────────────────────────────────────────────────┐",
            "  Server address (IP or domain):                                   ",
            "  Server port:                                                     ",
            "└─────────────────────────────────────────────────────────────────┘"
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
        ansi.a(moveCursor(Subtitle.getInstance()));
        ansi.a(printSubtitle());
        ansi.a(moveCursor(ServerSettings.getInstance()));
        ansi.a(printSettings());
        return ansi;
    }

    /**
     * Prints the title.
     *
     * @return The generated Ansi stream.
     */
    static Ansi printTitle() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(Title.getInstance()));
        ansi.a(printText(title));

        return ansi;
    }

    /**
     * Prints the subtitle.
     *
     * @return The generated Ansi stream.
     */
    static Ansi printSubtitle() {

        Ansi ansi = new Ansi();

        ansi.a(foreground(it.polimi.ingsw.client.view.cli.colours.Subtitle.getInstance()));

        ansi.a(moveCursor(Subtitle1.getInstance()));
        ansi.a(bold(true));
        ansi.a("A game by Leo Colovini for 2-4 players.");
        ansi.a(bold(false));

        ansi.a(moveCursor(Subtitle2.getInstance()));
        ansi.a("Project developed by Riccardo Milici, Riccardo Motta, Matteo Negro.");

        ansi.a(moveCursor(SubtitleReset.getInstance()));

        return ansi;
    }

    /**
     * Prints the server settings.
     *
     * @return The generated Ansi stream.
     */
    static Ansi printSettings() {
        Ansi ansi = new Ansi();
        ansi.a(foreground(Grey.getInstance()));
        ansi.a(printText(settings));
        ansi.a(moveCursor(ServerSettingsFirstInput.getInstance()));
        ansi.a(foreground(White.getInstance()));
        return ansi;
    }
}
