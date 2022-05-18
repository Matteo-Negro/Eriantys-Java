package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.coordinates.LoginNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptions;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptionsInput;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Login CLI printer.
 *
 * @author Matteo Negro
 */
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
     * @return The generated Ansi stream.
     */
    private static Ansi printOptions(Map<String, Boolean> players, int expectedPlayers) {
        Ansi ansi = new Ansi();
        ansi.a(foreground(Grey.getInstance()));

        // Line #1

        ansi.a(String.format("┌───────────────%1d/%1d───────────────┐", players.size(), expectedPlayers));
        ansi.a(newLine());

        // Line #2

        if (players.keySet().size() != 0) {
            int size = 35;
            String name;
            String shortName;
            StringBuilder adaptiveString;
            Iterator<String> itr = players.keySet().iterator();

            for (int i = 0; i < players.keySet().size(); i++) {
                name = itr.next();

                shortName = name;
                if (name.length()>22) shortName = name.substring(0, 22);

                adaptiveString = new StringBuilder();
                adaptiveString.append(" ".repeat(Math.max(0, (size - 2 - shortName.length() - 1 - 10))));

                ansi.a("  " + shortName + ":" + adaptiveString);
                ansi.a((players.get(name)) ? foreground(Green.getInstance()) : foreground(Red.getInstance()));
                ansi.a((players.get(name)) ? " ONLINE   " : " OFFLINE  ");

                ansi.a(foreground(Grey.getInstance()));
                ansi.a(newLine());
            }

        } else {
            ansi.append("  Not login players yet.           ");
            ansi.a(newLine());
        }

        // Line #3

        ansi.a("                                   ");
        ansi.a(newLine());

        // Line #4

        ansi.a("  Chose a name:                    ");
        ansi.a(newLine());

        // Line #5

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
