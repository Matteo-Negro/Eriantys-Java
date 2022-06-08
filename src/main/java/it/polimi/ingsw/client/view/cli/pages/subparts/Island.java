package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.coordinates.IslandNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.IslandReset;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import org.fusesource.jansi.Ansi;

import java.util.EnumMap;
import java.util.Map;

// ╔═══[TEMPLATE_ISL]═══╗
// ║       I···0        ║
// ║··  /‾‾‾‾‾‾‾‾‾\  ·· ║
// ║···/   ●   ●   \··· ║
// ║··/             \···║
// ║·/               \··║
// ║·\  ●x0 ●x0 ●x0  /··║
// ║··\             /···║
// ║···\  ●x0 ●x0  /··· ║
// ║··  \_________/  ·· ║
// ║       I···0        ║
// ║        ···         ║
// ╚════════════════════╝

/**
 * Class used for printing a single island.
 * It always returns to the starting point.
 *
 * @author Riccardo Motta
 */
public class Island {

    private Island() {
    }

    /**
     * Prints the island.
     *
     * @param id           Id of the island.
     * @param students     Map of students which are on the island.
     * @param tower        Tower which is on the island (null, otherwise).
     * @param motherNature Indicates whether Mother Nature is on the island or not.
     * @param ban          Indicates whether the island has been banned or not.
     * @param next         Indicates whether the island is connected to the next one or not.
     * @param prev         Indicates whether the island is connected to the previous one or not.
     * @return The Ansi stream to print to terminal.
     */
    public static Ansi print(int id,
                             Map<HouseColor, Integer> students,
                             TowerType tower,
                             boolean motherNature,
                             boolean ban,
                             boolean next,
                             boolean prev) {

        Ansi ansi = new Ansi();
        Map<IslandConnection, Boolean> connections = parseConnections(id, next, prev);
        String centeredDots = "        ···         ";
        String blankLine = "                    ";

        ansi.a(Utilities.foreground(Grey.getInstance()));

        // Line #1

        if (id >= 1 && id <= 6) {
            ansi.a(Utilities.bold(true));
            ansi.a(String.format("       ISL%02d        ", id));
            ansi.a(Utilities.bold(false));
        } else if (Boolean.TRUE.equals(connections.get(IslandConnection.NORTH)))
            ansi.a(centeredDots);
        else
            ansi.a(blankLine);
        ansi.a(newLine());

        // Line #2

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_WEST)) ? "··  " : "    ");
        ansi.a(defaultForeground(ban));
        ansi.a("/‾‾‾‾‾‾‾‾‾\\");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)) ? "  ·· " : "     ");
        ansi.a(newLine());

        // Line #3

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_WEST)) ? "···" : "   ");
        ansi.a(defaultForeground(ban));
        ansi.a("/   ");
        if (tower != null) {
            switch (tower) {
                case BLACK -> ansi.a(Utilities.foreground(TowerBlack.getInstance()));
                case GREY -> ansi.a(Utilities.foreground(TowerGrey.getInstance()));
                case WHITE -> ansi.a(Utilities.foreground(TowerWhite.getInstance()));
            }
            ansi.a("●");
            ansi.a(defaultForeground(ban));
        } else ansi.a(" ");
        ansi.a("   ");
        if (motherNature) {
            ansi.a(Utilities.foreground(MotherNature.getInstance()));
            ansi.a("●");
            ansi.a(defaultForeground(ban));
        } else ansi.a(" ");
        ansi.a("   \\");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)) ? "··· " : "    ");
        ansi.a(newLine());

        // Line #4

        ansi.a(connections.get(IslandConnection.WEST) || connections.get(IslandConnection.NORTH_WEST) ? "··" : "  ");
        ansi.a(defaultForeground(ban));
        ansi.a("/             \\");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        if (Boolean.TRUE.equals(connections.get(IslandConnection.EAST)))
            ansi.a("···");
        else if (Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)))
            ansi.a("·· ");
        else
            ansi.a("   ");
        ansi.a(newLine());

        // Line #5

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.WEST)) ? "·" : " ");
        ansi.a(defaultForeground(ban));
        ansi.a("/               \\");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.EAST)) ? "··" : "  ");
        ansi.a(newLine());

        // Line #6

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.WEST)) ? "·" : " ");
        ansi.a(defaultForeground(ban));
        ansi.a("\\  ");
        ansi.a(parseStudent(students.get(HouseColor.GREEN), HouseColor.GREEN, ban));
        ansi.a(" ");
        ansi.a(parseStudent(students.get(HouseColor.RED), HouseColor.RED, ban));
        ansi.a(" ");
        ansi.a(parseStudent(students.get(HouseColor.YELLOW), HouseColor.YELLOW, ban));
        ansi.a("  /");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.EAST)) ? "··" : "  ");
        ansi.a(newLine());

        // Line #7

        ansi.a(connections.get(IslandConnection.WEST) || connections.get(IslandConnection.SOUTH_WEST) ? "··" : "  ");
        ansi.a(defaultForeground(ban));
        ansi.a("\\             /");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        if (Boolean.TRUE.equals(connections.get(IslandConnection.EAST)))
            ansi.a("···");
        else if (Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)))
            ansi.a("·· ");
        else
            ansi.a("   ");
        ansi.a(newLine());

        // Line #8

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_WEST)) ? "···" : "   ");
        ansi.a(defaultForeground(ban));
        ansi.a("\\  ");
        ansi.a(parseStudent(students.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ban));
        ansi.a(" ");
        ansi.a(parseStudent(students.get(HouseColor.BLUE), HouseColor.BLUE, ban));
        ansi.a("  /");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)) ? "··· " : "    ");
        ansi.a(newLine());

        // Line #9

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_WEST)) ? "··  " : "    ");
        ansi.a(defaultForeground(ban));
        ansi.a("\\_________/");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)) ? "  ·· " : "     ");
        ansi.a(Utilities.foreground(Grey.getInstance()));
        ansi.a(newLine());

        // Line #10

        if (id >= 7 && id <= 12) {
            ansi.a(Utilities.bold(true));
            ansi.a(String.format("       ISL%02d        ", id));
            ansi.a(Utilities.bold(false));
        } else if (Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH)))
            ansi.a(centeredDots);
        else
            ansi.a(blankLine);
        ansi.a(newLine());

        // Line #11

        ansi.a(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH)) ? centeredDots : blankLine);
        ansi.a(resetCursor());

        return ansi;
    }

    /**
     * Gets the connections from the position (id) and connected neighbours.
     *
     * @param id   Id of the island.
     * @param next Next island connection.
     * @param prev Previous island connection.
     * @return The Map of connections of that island.
     */
    private static Map<IslandConnection, Boolean> parseConnections(int id, boolean next, boolean prev) {
        Map<IslandConnection, Boolean> connections = new EnumMap<>(IslandConnection.class);
        connections.put(IslandConnection.EAST, (next && (id == 2 || id == 3 || id == 4)) || (prev && (id == 9 || id == 10 || id == 11)));
        connections.put(IslandConnection.NORTH, (next && id == 12) || (prev && id == 7));
        connections.put(IslandConnection.NORTH_EAST, (next && id == 1) || (prev && id == 8));
        connections.put(IslandConnection.NORTH_WEST, (next && id == 11) || (prev && id == 6));
        connections.put(IslandConnection.SOUTH, (next && id == 6) || (prev && id == 1));
        connections.put(IslandConnection.SOUTH_EAST, (next && id == 5) || (prev && id == 12));
        connections.put(IslandConnection.SOUTH_WEST, (next && id == 7) || (prev && id == 2));
        connections.put(IslandConnection.WEST, (next && (id == 8 || id == 9 || id == 10)) || (prev && (id == 3 || id == 4 || id == 5)));
        return connections;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @return Ansi stream.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(IslandNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi resetCursor() {
        return Utilities.moveCursor(IslandReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ban true if the island has been banned.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi defaultForeground(boolean ban) {
        if (ban)
            return Utilities.foreground(Ban.getInstance());
        else
            return Utilities.foreground(White.getInstance());
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param studentsNumber The number of students of a specific color.
     * @param houseColor     The color of the students.
     * @param ban            true if the island has been banned.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi parseStudent(int studentsNumber, HouseColor houseColor, boolean ban) {
        Ansi ansi = new Ansi();
        if (studentsNumber != 0) {
            ansi.a(Utilities.foreground(Utilities.getColourFrom(houseColor)));
            ansi.a("●");
            ansi.a(defaultForeground(ban));
            ansi.a(String.format("x%01d", studentsNumber));
        } else {
            ansi.a(Utilities.foreground(DarkGrey.getInstance()));
            ansi.a("●x0");
            ansi.a(defaultForeground(ban));
        }
        return ansi;
    }

    /**
     * Enumeration of all the six possible connections.
     */
    private enum IslandConnection {EAST, NORTH, NORTH_EAST, NORTH_WEST, SOUTH, SOUTH_EAST, SOUTH_WEST, WEST}
}
