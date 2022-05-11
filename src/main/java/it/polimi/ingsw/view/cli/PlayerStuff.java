package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.EnumMap;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.moveCursor;

/**
 * Prints the whole player platform.
 *
 * @author Matteo Negro
 */
public class PlayerStuff {

    private PlayerStuff() {
    }

    /**
     * Method that prints all the school board and in case of expert mode the character cards.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(printSchoolBoards(4));
        terminal.flush();
        //terminal.writer().print(printCharacters());
        //terminal.flush();
    }

    /**
     * Prints every island.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printSchoolBoards(int playersNumber) {

        // Test SchoolBoard - Begin
        Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
        Map<HouseColor, Integer> diningRoom = new EnumMap<>(HouseColor.class);
        Map<HouseColor, Boolean> professors = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) {
            entrance.put(color, 2);
            diningRoom.put(color, 5);
            if (color == HouseColor.BLUE || color == HouseColor.RED) professors.put(color, true);
            else professors.put(color, false);
        }
        // Test SchoolBoard - End

        Ansi ansi = new Ansi();

        // Draw

        moveCursor(ansi, SchoolBoardFirst.getInstance());
        SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.BLACK, 5, 0, 3, "Matteo", WizardType.YELLOW, true, false);

        moveCursor(ansi, SchoolBoardE.getInstance());
        SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.WHITE, 3, 4, 0, "Motta", WizardType.WHITE, false, true);

        moveCursor(ansi, SchoolBoardReset2Player.getInstance());

        switch (playersNumber) {
            case 3 -> {
                moveCursor(ansi, SchoolBoardThird.getInstance());
                SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.BLACK, 6, 5, 10, "Milici", WizardType.FUCHSIA, false, true);

                moveCursor(ansi, SchoolBoardReset3Player.getInstance());
            }
            case 4 -> {
                moveCursor(ansi, SchoolBoardS.getInstance());
                SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.BLACK, 6, 5, 10, "Milici", WizardType.FUCHSIA, false, true);

                moveCursor(ansi, SchoolBoardE.getInstance());
                SchoolBoard.print(ansi, entrance, diningRoom, professors, TowerType.WHITE, 2, 10, 0, "Lazzarin", WizardType.GREEN, false, false);

                moveCursor(ansi, SchoolBoardReset4Player.getInstance());
            }
            default -> {
            }
        }
        return ansi;
    }
}
