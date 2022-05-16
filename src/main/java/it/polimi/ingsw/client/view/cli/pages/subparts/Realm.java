package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import it.polimi.ingsw.utilities.HouseColor;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Prints the whole realm.
 *
 * @author Riccardo Motta
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

        ansi.a(Utilities.moveCursor(IslandFirst.getInstance()));
        ansi.a(Island.print(1, students, null, true, false, true, true));

        ansi.a(Utilities.moveCursor(IslandNE.getInstance()));
        ansi.a(Island.print(2, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(3, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(4, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(5, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandSE.getInstance()));
        ansi.a(Island.print(6, students, null, false, false, true, true));

        // Second half

        ansi.a(Utilities.moveCursor(IslandLast.getInstance()));
        ansi.a(Island.print(12, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(11, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(10, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(9, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandE.getInstance()));
        ansi.a(Island.print(8, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandNE.getInstance()));
        ansi.a(Island.print(7, students, null, false, false, true, true));

        ansi.a(Utilities.moveCursor(IslandReset.getInstance()));

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
            case 2 -> Utilities.moveCursor(CloudFirst2Players.getInstance());
            case 3 -> Utilities.moveCursor(CloudFirst3Players.getInstance());
            default -> Utilities.moveCursor(CloudFirst4Players.getInstance());
        });

        ansi.a(Cloud.print(1, students, studentsNumber));

        ansi.a(Utilities.moveCursor(CloudE.getInstance()));
        ansi.a(Cloud.print(2, null, studentsNumber));

        ansi.a(Utilities.moveCursor(CloudE.getInstance()));
        ansi.a(Cloud.print(3, students, studentsNumber));

        ansi.a(
                switch (playersNumber) {
                    case 2 -> Utilities.moveCursor(CloudsReset2Players.getInstance());
                    case 3 -> Utilities.moveCursor(CloudsReset3Players.getInstance());
                    default -> Utilities.moveCursor(CloudsReset4Players.getInstance());
                }
        );

        return ansi;
    }
}
