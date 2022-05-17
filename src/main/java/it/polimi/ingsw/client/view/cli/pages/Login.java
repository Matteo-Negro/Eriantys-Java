package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.Options;
import it.polimi.ingsw.client.view.cli.coordinates.OptionsInput;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

public class Login {
    private static final String[] title = {
            " __                         __          ",
            "|  \\                       |  \\         ",
            "| ██       ______   ______  \\██_______  ",
            "| ██      /      \\ /      \\|  \\       \\ ",
            "| ██     |  ██████\\  ██████\\ ██ ███████\\",
            "| ██     | ██  | ██ ██  | ██ ██ ██  | ██",
            "| ██_____| ██__/ ██ ██__| ██ ██ ██  | ██",
            "| ██     \\\\██    ██\\██    ██ ██ ██  | ██",
            " \\████████ \\██████ _\\███████\\██\\██   \\██",
            "                  |  \\__| ██            ",
            "                   \\██    ██            ",
            "                    \\██████             "
    };

    private static final String[] options = {
            "┌────────────────────────────────┐",
            "  Chose a name:                   ",
            "└────────────────────────────────┘"
    };

    /**
     * Prints the whole game selection centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal, Map<String, Boolean> players, int expectedPlayers) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 17) / 2, (terminal.getWidth() - 40) / 2));
        terminal.writer().print(print(players, expectedPlayers));
        terminal.flush();
    }

    /**
     * Prints the whole game selection.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print(Map<String, Boolean> players, int expectedPlayers) {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.cursorMove(0, 1);
        ansi.a(" - PLAYERS - ");
        ansi.cursorMove(-11, 1);
        for(String player : players.keySet()){
            ansi.a(player);
            ansi.a(" ");
            ansi.a(players.get(player));
            ansi.cursorMove(-player.length(), 1);
        }
        ansi.cursorMove(-18, -2);
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

    private static Ansi newline(){
        Ansi ansi = new Ansi();
        ansi().restoreCursorPosition();
        ansi().cursorMove(20, 1);
        return ansi;
    }
}
