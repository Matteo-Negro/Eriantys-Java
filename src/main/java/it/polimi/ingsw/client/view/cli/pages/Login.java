package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.coordinates.LoginNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptions;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptionsInput;
import it.polimi.ingsw.utilities.Pair;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Login CLI printer.
 *
 * @author Matteo Negro
 */
public class Login {
    private static final String[] title = {" __                         __          ", "|  \\                       |  \\         ", "| ██       ______   ______  \\██_______  ", "| ██      /      \\ /      \\|  \\       \\ ", "| ██     |  ██████\\  ██████\\ ██ ███████\\", "| ██     | ██  | ██ ██  | ██ ██ ██  | ██", "| ██_____| ██__/ ██ ██__| ██ ██ ██  | ██", "| ██     \\\\██    ██\\██    ██ ██ ██  | ██", " \\████████ \\██████ _\\███████\\██\\██   \\██", "                  |  \\__| ██            ", "                   \\██    ██            ", "                    \\██████             "};

    private Login() {
    }

// ┌───────────────2/3───────────────┐
//   Matteo:                 ONLINE
//   Riccardo:               OFFLINE
//
//   Chose a name:
// └─────────────────────────────────┘

// ┌───────────────0/3───────────────┐
//   Not login players yet.
//
//   Chose a name:
// └─────────────────────────────────┘

    /**
     * Prints the whole game selection centering it.
     *
     * @param terminal        Terminal where to write.
     * @param players         List of online player in the game.
     * @param expectedPlayers Number of the expected players.
     */
    public static void print(Terminal terminal, Map<String, Boolean> players, int expectedPlayers) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - ((players.size() != 0) ? 16 + players.size() : 17)) / 2, (terminal.getWidth() - 40) / 2));
        terminal.writer().print(print(players, expectedPlayers));
        terminal.flush();
    }

    /**
     * Prints the whole login page.
     *
     * @param players         List of online player in the game.
     * @param expectedPlayers Number of the expected players.
     * @return The generated Ansi stream.
     */
    private static Ansi print(Map<String, Boolean> players, int expectedPlayers) {
        Ansi ansi = new Ansi();

        // Test players - start
        Map<String, Boolean> test = new HashMap<>();
        test.put("Matteo", true);
        test.put("Milici", true);
        test.put("Motta", false);
        //test.put("Lazzarin", false);
        // Test players - end

        ansi.a(printTitle());
        ansi.a(moveCursor(LoginOptions.getInstance()));
        ansi.a(printOptions(players, expectedPlayers));
        //ansi.a(printOptions(test, expectedPlayers));
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
     * @param expectedPlayers Number of the expected players.
     * @return The generated Ansi stream.
     */
    private static Ansi printOptions(Map<String, Boolean> players, int expectedPlayers) {

        List<Pair<String, Boolean>> sortedPlayers = players.keySet().stream().sorted()
                .map(name -> new Pair<>(name, players.get(name))).toList();

        Ansi ansi = new Ansi();
        ansi.a(foreground(Grey.getInstance()));

        // Line #1

        ansi.a(String.format("┌─────────────[ %1d/%1d ]─────────────┐", players.size(), expectedPlayers));
        ansi.a(newLine());

        // Line #2

        if (players.size() != 0) {
            int size = 35;
            String shortName;
            StringBuilder adaptiveString;

            for (Pair<String, Boolean> player : sortedPlayers) {
                shortName = player.first();

                if (player.first().length() > 22) shortName = player.first().substring(0, 22);

                adaptiveString = new StringBuilder();
                adaptiveString.append(" ".repeat(Math.max(0, (size - 2 - shortName.length() - 1 - (Boolean.TRUE.equals(player.second()) ? 9 : 10)))));

                ansi.a("  " + shortName + ":" + adaptiveString);
                ansi.a(Boolean.TRUE.equals(player.second()) ? foreground(Green.getInstance()) : foreground(Red.getInstance()));
                ansi.a(Boolean.TRUE.equals(player.second()) ? " ONLINE  " : " OFFLINE  ");

                ansi.a(foreground(Grey.getInstance()));
                ansi.a(newLine());
            }

        } else {
            ansi.append("  No one here, yet.                ");
            ansi.a(newLine());
        }

        // Line #3 (manually printing)

        ansi.a("                                   ");
        ansi.a(newLine());

        // Line #4 (manually printing)

        ansi.a("  Chose a name:                    ");
        ansi.a(newLine());

        // Line #5 (manually printing)

        ansi.a("└─────────────────────────────────┘");

        ansi.a(moveCursor(LoginOptionsInput.getInstance()));
        ansi.a(foreground(White.getInstance()));

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(LoginNewLine.getInstance());
    }
}
