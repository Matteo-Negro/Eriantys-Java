package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.EnumMap;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.moveCursor;

/**
 * Prints the whole realm.
 */
public class Realm {

    private Realm() {
    }

    /**
     * Method that prints all the islands and then all the clouds.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(printIslands());
        terminal.flush();
    }

    /**
     * Prints every island.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printIslands() {

        // Test island - Begin
        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values())
            students.put(color, 1);
        // Test island - End

        Ansi ansi = new Ansi();

        // First half

        moveCursor(ansi, IslandFirst.getInstance());
        Island.print(ansi, 1, students, null, true, false, true, true);

        moveCursor(ansi, IslandNE.getInstance());
        Island.print(ansi, 2, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 3, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 4, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 5, students, null, false, false, true, true);

        moveCursor(ansi, IslandSE.getInstance());
        Island.print(ansi, 6, students, null, false, false, true, true);

        // Second half

        moveCursor(ansi, IslandLast.getInstance());
        Island.print(ansi, 12, students, null, false, false, true, true);

        moveCursor(ansi, IslandSE.getInstance());
        Island.print(ansi, 11, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 10, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 9, students, null, false, false, true, true);

        moveCursor(ansi, IslandE.getInstance());
        Island.print(ansi, 8, students, null, false, false, true, true);

        moveCursor(ansi, IslandNE.getInstance());
        Island.print(ansi, 7, students, null, false, false, true, true);

        return ansi;
    }
}
