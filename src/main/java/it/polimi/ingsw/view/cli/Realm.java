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

        ansi.a(moveCursor(IslandFirst.getInstance()));
        ansi.a(Island.print(1, students, null, true, false, true, true));

        ansi.a(moveCursor(IslandNE.getInstance()));
        ansi.a(Island.print(2, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(3, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(4, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(5, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandSE.getInstance()));
        ansi.a(Island.print(6, students, null, false, false, true, true));

        // Second half

        ansi.a(moveCursor(IslandLast.getInstance()));
        ansi.a(Island.print(12, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(11, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(10, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(9, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(8, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandNE.getInstance()));
        ansi.a(Island.print(7, students, null, false, false, true, true));

        ansi.a(moveCursor(IslandReset.getInstance()));

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

        ansi.a(switch (playersNumber) {
            case 2 -> moveCursor(CloudFirst2Players.getInstance());
            case 3 -> moveCursor(CloudFirst3Players.getInstance());
            default -> moveCursor(CloudFirst4Players.getInstance());
        });

        ansi.a(Cloud.print(1, students, studentsNumber));

        ansi.a(moveCursor(CloudE.getInstance()));
        ansi.a(Cloud.print(2, null, studentsNumber));

        ansi.a(moveCursor(CloudE.getInstance()));
        ansi.a(Cloud.print(3, students, studentsNumber));

        ansi.a(
            switch (playersNumber) {
                case 2 -> moveCursor(CloudsReset2Players.getInstance());
                case 3 -> moveCursor(CloudsReset3Players.getInstance());
                default -> moveCursor(CloudsReset4Players.getInstance());
            }
        );

        return ansi;
    }
}
