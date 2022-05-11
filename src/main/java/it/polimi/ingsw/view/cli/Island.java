package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.view.cli.colours.*;
import it.polimi.ingsw.view.cli.coordinates.IslandNewLine;
import it.polimi.ingsw.view.cli.coordinates.IslandReset;
import org.fusesource.jansi.Ansi;

import java.util.EnumMap;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;

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
 */
class Island {

    private Island() {
    }

    /**
     * Prints the island.
     *
     * @param ansi         Ansi stream where to write.
     * @param id           Id of the island.
     * @param students     Map of students which are on the island.
     * @param tower        Tower which is on the island (null, otherwise).
     * @param motherNature Indicates whether Mother Nature is on the island or not.
     * @param ban          Indicates whether the island has been banned or not.
     * @param next         Indicates whether the island is connected to the next one or not.
     * @param prev         Indicates whether the island is connected to the previous one or not.
     */
    static void print(Ansi ansi,
                      int id,
                      Map<HouseColor, Integer> students,
                      TowerType tower,
                      boolean motherNature,
                      boolean ban,
                      boolean next,
                      boolean prev) {

        Map<IslandConnection, Boolean> connections = parseConnections(id, next, prev);
        String centeredDots = "        ···         ";
        String blankLine = "                    ";

        foreground(ansi, Grey.getInstance());

        // Line #1

        if (id >= 1 && id <= 6) {
            bold(ansi, true);
            ansi.append("       ISL${id}        ".replace("${id}", String.format("%02d", id)));
            bold(ansi, false);
        } else if (Boolean.TRUE.equals(connections.get(IslandConnection.NORTH)))
            ansi.append(centeredDots);
        else
            ansi.append(blankLine);
        newLine(ansi);

        // Line #2

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_WEST)) ? "··  " : "    ");
        defaultForeground(ansi, ban);
        ansi.append("/‾‾‾‾‾‾‾‾‾\\");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)) ? "  ·· " : "     ");
        newLine(ansi);

        // Line #3

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_WEST)) ? "···" : "   ");
        defaultForeground(ansi, ban);
        ansi.append("/   ");
        if (tower != null) {
            switch (tower) {
                case BLACK -> foreground(ansi, TowerBlack.getInstance());
                case GREY -> foreground(ansi, TowerGrey.getInstance());
                case WHITE -> foreground(ansi, TowerWhite.getInstance());
            }
            ansi.append("●");
            defaultForeground(ansi, ban);
        } else ansi.append(" ");
        ansi.append("   ");
        if (motherNature) {
            foreground(ansi, MotherNature.getInstance());
            ansi.append("●");
            defaultForeground(ansi, ban);
        } else ansi.append(" ");
        ansi.append("   \\");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)) ? "··· " : "    ");
        newLine(ansi);

        // Line #4

        ansi.append(connections.get(IslandConnection.WEST) || connections.get(IslandConnection.NORTH_WEST) ? "··" : "  ");
        defaultForeground(ansi, ban);
        ansi.append("/             \\");
        foreground(ansi, Grey.getInstance());
        if (Boolean.TRUE.equals(connections.get(IslandConnection.EAST)))
            ansi.append("···");
        else if (Boolean.TRUE.equals(connections.get(IslandConnection.NORTH_EAST)))
            ansi.append("·· ");
        else
            ansi.append("   ");
        newLine(ansi);

        // Line #5

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.WEST)) ? "·" : " ");
        defaultForeground(ansi, ban);
        ansi.append("/               \\");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.EAST)) ? "··" : "  ");
        newLine(ansi);

        // Line #6

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.WEST)) ? "·" : " ");
        defaultForeground(ansi, ban);
        ansi.append("\\  ");
        parseStudent(students.get(HouseColor.BLUE), HouseColor.BLUE, ansi, ban);
        ansi.append(" ");
        parseStudent(students.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ansi, ban);
        ansi.append(" ");
        parseStudent(students.get(HouseColor.GREEN), HouseColor.GREEN, ansi, ban);
        ansi.append("  /");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.EAST)) ? "··" : "  ");
        newLine(ansi);

        // Line #7

        ansi.append(connections.get(IslandConnection.WEST) || connections.get(IslandConnection.SOUTH_WEST) ? "··" : "  ");
        defaultForeground(ansi, ban);
        ansi.append("\\             /");
        foreground(ansi, Grey.getInstance());
        if (Boolean.TRUE.equals(connections.get(IslandConnection.EAST)))
            ansi.append("···");
        else if (Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)))
            ansi.append("·· ");
        else
            ansi.append("   ");
        newLine(ansi);

        // Line #8

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_WEST)) ? "···" : "   ");
        defaultForeground(ansi, ban);
        ansi.append("\\  ");
        parseStudent(students.get(HouseColor.RED), HouseColor.RED, ansi, ban);
        ansi.append(" ");
        parseStudent(students.get(HouseColor.YELLOW), HouseColor.YELLOW, ansi, ban);
        ansi.append("  /");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)) ? "··· " : "    ");
        newLine(ansi);

        // Line #9

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_WEST)) ? "··  " : "    ");
        defaultForeground(ansi, ban);
        ansi.append("\\_________/");
        foreground(ansi, Grey.getInstance());
        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH_EAST)) ? "  ·· " : "     ");
        foreground(ansi, Grey.getInstance());
        newLine(ansi);

        // Line #10

        if (id >= 7 && id <= 12) {
            bold(ansi, true);
            ansi.append("       ISL${id}        ".replace("${id}", String.format("%02d", id)));
            bold(ansi, false);
        } else if (Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH)))
            ansi.append(centeredDots);
        else
            ansi.append(blankLine);
        newLine(ansi);

        // Line #11

        ansi.append(Boolean.TRUE.equals(connections.get(IslandConnection.SOUTH)) ? centeredDots : blankLine);
        resetCursor(ansi);
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
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, IslandNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi) {
        moveCursor(ansi, IslandReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ansi Ansi stream where to write.
     * @param ban  true if the island has been banned.
     */
    private static void defaultForeground(Ansi ansi, boolean ban) {
        if (ban)
            foreground(ansi, Ban.getInstance());
        else
            foreground(ansi, White.getInstance());
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param studentsNumber The number of students of a specific color.
     * @param houseColor     The color of the students.
     * @param ansi           Ansi stream where to write.
     * @param ban            true if the island has been banned.
     */
    private static void parseStudent(int studentsNumber, HouseColor houseColor, Ansi ansi, boolean ban) {
        if (studentsNumber != 0) {
            foreground(ansi, getColourFrom(houseColor));
            ansi.append("●");
            defaultForeground(ansi, ban);
            ansi.append(String.format("x%01d", studentsNumber));
        } else {
            foreground(ansi, DarkGrey.getInstance());
            ansi.append("●x0");
            defaultForeground(ansi, ban);
        }
    }

    /**
     * Enumeration of all the six possible connections.
     */
    private enum IslandConnection {EAST, NORTH, NORTH_EAST, NORTH_WEST, SOUTH, SOUTH_EAST, SOUTH_WEST, WEST}
}
