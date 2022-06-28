package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.Title;
import it.polimi.ingsw.client.view.cli.coordinates.WaitingNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.WaitingOptions;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.Collections;
import java.util.List;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Waiting room CLI printer.
 *
 * @author Matteo Negro
 */
public class WaitingRoom {
    private static final String[] title = {
            " __       __          __   __     __                                                                ",
            "|  \\  _  |  \\        |  \\ |  \\   |  \\                                                               ",
            "| ██ / \\ | ██ ______  \\██_| ██_   \\██_______   ______        ______   ______   ______  ______ ____  ",
            "| ██/  █\\| ██|      \\|  \\   ██ \\ |  \\       \\ /      \\      /      \\ /      \\ /      \\|      \\    \\ ",
            "| ██  ███\\ ██ \\██████\\ ██\\██████ | ██ ███████\\  ██████\\    |  ██████\\  ██████\\  ██████\\ ██████\\████\\",
            "| ██ ██\\██\\██/      ██ ██ | ██ __| ██ ██  | ██ ██  | ██    | ██   \\██ ██  | ██ ██  | ██ ██ | ██ | ██",
            "| ████  \\████  ███████ ██ | ██|  \\ ██ ██  | ██ ██__| ██    | ██     | ██__/ ██ ██__/ ██ ██ | ██ | ██",
            "| ███    \\███\\██    ██ ██  \\██  ██ ██ ██  | ██\\██    ██    | ██      \\██    ██\\██    ██ ██ | ██ | ██",
            " \\██      \\██ \\███████\\██   \\████ \\██\\██   \\██_\\███████     \\██       \\██████  \\██████ \\██  \\██  \\██",
            "                                             |  \\__| ██                                             ",
            "                                              \\██    ██                                             ",
            "                                               \\██████                                              "
    };

    private WaitingRoom() {
    }

    /**
     * Prints the whole wainting room centering it.
     *
     * @param terminal        Terminal where to write.
     * @param players         List of online player in the game.
     * @param gameCode        Id of the game.
     * @param expectedPlayers Number of the expected players.
     * @param iteration       Number used to print the suspension dots.
     */
    public static void print(Terminal terminal, List<String> players, String gameCode, int expectedPlayers, int iteration) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 12 - players.size() - 4) / 2, (terminal.getWidth() - 100) / 2));
        terminal.writer().print(print(players, gameCode, expectedPlayers, iteration));
        terminal.flush();
    }

    /**
     * Prints the whole game selection.
     *
     * @param players         List of online player in the game.
     * @param gameCode        Id of the game.
     * @param expectedPlayers Number of the expected players.
     * @param iteration       Number used to print the suspension dots.
     * @return The generated Ansi stream.
     */
    private static Ansi print(List<String> players, String gameCode, int expectedPlayers, int iteration) {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        ansi.a(moveCursor(WaitingOptions.getInstance()));
        ansi.a(printOptions(players, gameCode, expectedPlayers, iteration));
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
     * @param players         List of online player in the game.
     * @param gameCode        Id of the game.
     * @param expectedPlayers Number of the expected players.
     * @param iteration       Number used to print the suspension dots.
     * @return The generated Ansi stream.
     */
    private static Ansi printOptions(List<String> players, String gameCode, int expectedPlayers, int iteration) {

        Collections.sort(players);

        Ansi ansi = new Ansi();
        ansi.a(foreground(Grey.getInstance()));

        // Line #1

        ansi.a(String.format("┌──────────────[ %1d/%1d ]──────────────┐", players.size(), expectedPlayers));
        ansi.a(newLine());

        // Line #2

        int size = 37;
        String shortName;
        StringBuilder adaptiveString;

        for (String player : players) {

            shortName = player;
            if (player.length() > 33) shortName = player.substring(0, 33);

            adaptiveString = new StringBuilder();
            adaptiveString.append(" ".repeat(Math.max(0, (size - 2 - shortName.length() - 2))));

            ansi.a("  " + shortName + adaptiveString + "  ");
            ansi.a(newLine());
        }

        // Line #3 (manually printing)

        ansi.a("                                     ");
        ansi.a(newLine());

        // Line #4 (manually printing)

        ansi.a("  You're waiting for the others ");
        switch (iteration % 4) {
            case 1 -> ansi.a(".    ");
            case 2 -> ansi.a("..   ");
            case 3 -> ansi.a("...  ");
            default -> ansi.a("     ");
        }
        ansi.a(newLine());

        // Line #5 (manually printing)

        ansi.a("└─────────────[ " + gameCode + " ]─────────────┘");
        ansi.cursor(0, 0);

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(WaitingNewLine.getInstance());
    }
}
