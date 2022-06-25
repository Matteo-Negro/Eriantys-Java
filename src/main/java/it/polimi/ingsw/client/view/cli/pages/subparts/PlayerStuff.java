package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.model.Player;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.List;

import static it.polimi.ingsw.client.view.cli.Utilities.foreground;
import static it.polimi.ingsw.client.view.cli.Utilities.moveCursor;

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
     * @param players  The list of players.
     * @param exp      Indicates whether the game is in expert mode.
     * @param cards    The special characters in the game (if in expert mode).
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
        Ansi ansi = new Ansi();

        // Draw

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) ansi.a(moveCursor(SchoolBoardFirst.getInstance()));
            else if (i == 1 || i == 3) ansi.a(moveCursor(SchoolBoardE.getInstance()));
            else if (!exp && i == 2 && players.size() == 3) ansi.a(moveCursor(SchoolBoardThirdNoExp.getInstance()));
            else ansi.a(moveCursor(SchoolBoardS.getInstance()));

            ansi.a(SchoolBoard.print(players.get(i).getSchoolBoard().getEntrance(), players.get(i).getSchoolBoard().getDiningRoom(), players.get(i).getSchoolBoard().getProfessors(), players.get(i).getSchoolBoard().getTowerType(), players.get(i).getSchoolBoard().getTowersNumber(), (players.get(i).getCurrentPlayedAssistant() != null) ? players.get(i).getCurrentPlayedAssistant().getId() : 0, players.get(i).getCoins(), players.get(i).getName(), players.get(i).getWizard(), players.get(i).isActive(), exp));

            if (i == 1) ansi.a(moveCursor(SchoolBoardReset2Player.getInstance()));
            else if (i == 2 && players.size() == 3 && exp) ansi.a(moveCursor(SchoolBoardReset3PlayerExp.getInstance()));
            else if (i == 2 && players.size() == 3) ansi.a(moveCursor(SchoolBoardReset3PlayerNoExp.getInstance()));
            else if (i == 3 && players.size() == 4) ansi.a(moveCursor(SchoolBoardReset4Player.getInstance()));
        }

        ansi.a(foreground(White.getInstance()));

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
            case 2 -> moveCursor(SpecialCharacterFirst2Players.getInstance());
            case 3 -> moveCursor(SpecialCharacterFirst3Players.getInstance());
            default -> moveCursor(SpecialCharacterFirst4Players.getInstance());
        });

        for (int i = 0; i < 3; i++) {
            if (i != 0) ansi.a(moveCursor(SpecialCharacterE.getInstance()));
            ansi.a(SpecialCharacter.print(cards.get(i).getId(), cards.get(i).getCost(), cards.get(i).isActive(), cards.get(i).getAvailableBans(), cards.get(i).getStudents()));
        }

        ansi.a(switch (playersNumber) {
            case 2 -> moveCursor(SpecialCharactersReset2Players.getInstance());
            case 3 -> moveCursor(SpecialCharactersReset3Players.getInstance());
            default -> moveCursor(SpecialCharactersReset4Players.getInstance());
        });

        return ansi;
    }
}
