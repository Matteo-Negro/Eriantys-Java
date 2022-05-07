package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
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
        terminal.writer().print(printClouds(3));
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

        moveCursor(ansi, IslandsReset.getInstance());

        return ansi;
    }

    /**
     * Prints every cloud.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printClouds(int playersNumber) {

        // Test clouds - Begin
        List<HouseColor> students = new ArrayList<>();
        students.add(HouseColor.RED);
        students.add(HouseColor.GREEN);
        students.add(HouseColor.BLUE);
        students.add(HouseColor.YELLOW);
        int studentsNumber = playersNumber == 3 ? 4 : 3;
        // Test clouds - End

        Ansi ansi = new Ansi();

        switch (playersNumber) {
            case 2 -> moveCursor(ansi, CloudFirst2Players.getInstance());
            case 3 -> moveCursor(ansi, CloudFirst3Players.getInstance());
            default -> moveCursor(ansi, CloudFirst4Players.getInstance());
        }

        Cloud.print(ansi, 1, students, studentsNumber);

        moveCursor(ansi, CloudE.getInstance());
        Cloud.print(ansi, 2, null, studentsNumber);

        moveCursor(ansi, CloudE.getInstance());
        Cloud.print(ansi, 3, students, studentsNumber);

        switch (playersNumber) {
            case 2 -> moveCursor(ansi, CloudsReset2Players.getInstance());
            case 3 -> moveCursor(ansi, CloudsReset3Players.getInstance());
            default -> moveCursor(ansi, CloudsReset4Players.getInstance());
        }

        return ansi;
    }
}
