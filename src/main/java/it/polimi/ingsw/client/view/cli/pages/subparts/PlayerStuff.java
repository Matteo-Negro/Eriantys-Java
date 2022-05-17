package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import it.polimi.ingsw.utilities.HouseColor;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.ArrayList;
import java.util.List;

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
    public static void print(Terminal terminal, List<Player> players, boolean exp, List<it.polimi.ingsw.client.model.SpecialCharacter> cards) {
        terminal.writer().print(printSchoolBoards(players, exp));
        terminal.flush();
        if (exp) {
            terminal.writer().print(printSpecialCharacters(cards, players.size()));
            terminal.flush();
        }
    }

    /**
     * Prints every school board.
     *
     * @param players The list with the players in the match.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printSchoolBoards(List<Player> players, Boolean exp) {

        // Test SchoolBoard - Begin
//        Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
//        Map<HouseColor, Integer> diningRoom = new EnumMap<>(HouseColor.class);
//        Map<HouseColor, Boolean> professors = new EnumMap<>(HouseColor.class);
//        for (HouseColor color : HouseColor.values()) {
//            entrance.put(color, 2);
//            diningRoom.put(color, 5);
//            if (color == HouseColor.BLUE || color == HouseColor.RED) professors.put(color, true);
//            else professors.put(color, false);
//        }
        // Test SchoolBoard - End

        Ansi ansi = new Ansi();

        // Draw

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) ansi.a(Utilities.moveCursor(SchoolBoardFirst.getInstance()));
            else if (i == 1 || i == 3) ansi.a(Utilities.moveCursor(SchoolBoardE.getInstance()));
            else ansi.a(Utilities.moveCursor(SchoolBoardS.getInstance()));

            ansi.a(SchoolBoard.print(players.get(i).getSchoolBoard().getEntrance(), players.get(i).getSchoolBoard().getDiningRoom(), players.get(i).getSchoolBoard().getProfessors(), players.get(i).getSchoolBoard().getTowerType(), players.get(i).getSchoolBoard().getTowersNumber(), (players.get(i).getCurrentPlayedAssistant() != null) ? players.get(i).getCurrentPlayedAssistant().getId() : 0, players.get(i).getCoins(), players.get(i).getName(), players.get(i).getWizard(), players.get(i).isActive(), exp));

            if (i == 1) ansi.a(Utilities.moveCursor(SchoolBoardReset2Player.getInstance()));
            else if (i == 2 && players.size() == 3) ansi.a(Utilities.moveCursor(SchoolBoardReset3Player.getInstance()));
            else if (i == 3 && players.size() == 4) ansi.a(Utilities.moveCursor(SchoolBoardReset4Player.getInstance()));
        }

        return ansi;
    }

    /**
     * Prints every special character.
     *
     * @param cards The list of special characters.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printSpecialCharacters(List<it.polimi.ingsw.client.model.SpecialCharacter> cards, int playersNumber) {

        // Test SchoolBoard - Begin
//        List<HouseColor> studentsSix = new ArrayList<>();
//        List<HouseColor> studentsFour = new ArrayList<>();
//        for (HouseColor color : HouseColor.values()) {
//            studentsSix.add(color);
//            if (!color.equals(HouseColor.FUCHSIA)) studentsFour.add(color);
//        }
//        studentsSix.add(HouseColor.RED);
        // Test SchoolBoard - End

        Ansi ansi = new Ansi();

        // Draw

        ansi.a(switch (playersNumber) {
            case 2 -> Utilities.moveCursor(SpecialCharacterFirst2Players.getInstance());
            case 3 -> Utilities.moveCursor(SpecialCharacterFirst3Players.getInstance());
            default -> Utilities.moveCursor(SpecialCharacterFirst4Players.getInstance());
        });

        for (int i=0; i<3; i++) {
            if (i!=0) ansi.a(Utilities.moveCursor(SpecialCharacterE.getInstance()));
            ansi.a(SpecialCharacter.print(cards.get(i).getId(), cards.get(i).getCost(), cards.get(i).isActive(), cards.get(i).getAvailableBans(), cards.get(i).getStudents()));
        }

        ansi.a(switch (playersNumber) {
            case 2 -> Utilities.moveCursor(SpecialCharactersReset2Players.getInstance());
            case 3 -> Utilities.moveCursor(SpecialCharactersReset3Players.getInstance());
            default -> Utilities.moveCursor(SpecialCharactersReset4Players.getInstance());
        });

        return ansi;
    }
}
