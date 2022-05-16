package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
        terminal.writer().print(printSchoolBoards(3));
        terminal.flush();
        terminal.writer().print(printSpecialCharactes(3));
        terminal.flush();
    }

    /**
     * Prints every school board.
     *
     * @param playersNumber The number of players.
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

        ansi.a(Utilities.moveCursor(SchoolBoardFirst.getInstance()));
        ansi.a(SchoolBoard.print(entrance, diningRoom, professors, TowerType.BLACK, 5, 0, 3, "Matteo", WizardType.YELLOW, true, false));

        ansi.a(Utilities.moveCursor(SchoolBoardE.getInstance()));
        ansi.a(SchoolBoard.print(entrance, diningRoom, professors, TowerType.WHITE, 3, 4, 0, "Motta", WizardType.WHITE, false, true));

        ansi.a(Utilities.moveCursor(SchoolBoardReset2Player.getInstance()));

        if (playersNumber == 3) {
            ansi.a(Utilities.moveCursor(SchoolBoardS.getInstance()));
            ansi.a(SchoolBoard.print(entrance, diningRoom, professors, TowerType.BLACK, 6, 5, 10, "Milici", WizardType.FUCHSIA, false, true));
            ansi.a(Utilities.moveCursor(SchoolBoardReset3Player.getInstance()));
        } else {
            ansi.a(Utilities.moveCursor(SchoolBoardS.getInstance()));
            ansi.a(SchoolBoard.print(entrance, diningRoom, professors, TowerType.BLACK, 6, 5, 10, "Milici", WizardType.FUCHSIA, false, true));

            ansi.a(Utilities.moveCursor(SchoolBoardE.getInstance()));
            ansi.a(SchoolBoard.print(entrance, diningRoom, professors, TowerType.WHITE, 2, 10, 0, "Lazzarin", WizardType.GREEN, false, false));

            ansi.a(Utilities.moveCursor(SchoolBoardReset4Player.getInstance()));
        }

        return ansi;
    }

    /**
     * Prints every special character.
     *
     * @param playersNumber The number of players.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printSpecialCharactes(int playersNumber) {

        // Test SchoolBoard - Begin
        List<HouseColor> studentsSix = new ArrayList<>();
        List<HouseColor> studentsFour = new ArrayList<>();
        for (HouseColor color : HouseColor.values()) {
            studentsSix.add(color);
            if (!color.equals(HouseColor.FUCHSIA)) studentsFour.add(color);
        }
        studentsSix.add(HouseColor.RED);
        // Test SchoolBoard - End

        Ansi ansi = new Ansi();

        // Draw

        ansi.a(switch (playersNumber) {
            case 2 -> Utilities.moveCursor(SpecialCharacterFirst2Players.getInstance());
            case 3 -> Utilities.moveCursor(SpecialCharacterFirst3Players.getInstance());
            default -> Utilities.moveCursor(SpecialCharacterFirst4Players.getInstance());
        });

        ansi.a(SpecialCharacter.print(3, 1, false, 5, null));

        ansi.a(Utilities.moveCursor(SpecialCharacterE.getInstance()));
        ansi.a(SpecialCharacter.print(12, 4, true, -1, studentsSix));

        ansi.a(Utilities.moveCursor(SpecialCharacterE.getInstance()));
        ansi.a(SpecialCharacter.print(7, 3, false, -1, null));

        ansi.a(switch (playersNumber) {
            case 2 -> Utilities.moveCursor(SpecialCharactersReset2Players.getInstance());
            case 3 -> Utilities.moveCursor(SpecialCharactersReset3Players.getInstance());
            default -> Utilities.moveCursor(SpecialCharactersReset4Players.getInstance());
        });

        return ansi;
    }
}
