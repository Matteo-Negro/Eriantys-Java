package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.view.cli.colours.*;
import it.polimi.ingsw.view.cli.coordinates.SchoolBoardNewLine;
import it.polimi.ingsw.view.cli.coordinates.SchoolBoardReset;
import org.fusesource.jansi.Ansi;

import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.*;

// ╔═════[TEMP_SCHOOLBOARD]═════╗
// ║  NAME                      ║1
// ║ ┌─────┬──────┬─────┬─────┐ ║2
// ║ │ ENT │  DR  │ PRF │ TWR │ ║3
// ║ ├─────┼──────┼─────┼─────┤ ║4
// ║ │ ●x0 │ ●x00 │  ●  │ ●x0 │ ║5
// ║ │     │      │     ├─────┤ ║6
// ║ │ ●x0 │ ●x00 │  ●  │ AST │ ║7
// ║ │     │      │     ├─────┤ ║8
// ║ │ ●x0 │ ●x00 │  ●  │AS-00│ ║9
// ║ │     │      │     ├─────┤ ║10
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║11
// ║ │     │      │     │     │ ║12
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║13
// ║ └─────┴──────┴─────┴─────┘ ║14
// ╚════════════════════════════╝
//
// ╔═══[TEMPL_SCHOOLBOARD_EX]═══╗
// ║  NAME                      ║
// ║ ┌─────┬──────┬─────┬─────┐ ║
// ║ │ ENT │  DR  │ PRF │ TWR │ ║
// ║ ├─────┼──────┼─────┼─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ ●x0 │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ AST │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │AS-00│ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ CNS │ ║
// ║ │     │      │     ├─────┤ ║
// ║ │ ●x0 │ ●x00 │  ●  │ x00 │ ║
// ║ └─────┴──────┴─────┴─────┘ ║
// ╚════════════════════════════╝

/**
 * Class used for printing a single school board.
 * It always returns to the starting point.
 *
 * @author Matteo Negro
 */
class SchoolBoard {

    private SchoolBoard() {
    }

    /**
     * Print the school board.
     *
     * @param ansi         Ansi stream where to write.
     * @param entrance     Map of students which are on the entrance.
     * @param diningRoom   Map of students which are on the dining room.
     * @param professors   Map of professors are owned by the player.
     * @param tower        Color of the towers belong to the player.
     * @param towersNumber Number of the towers.
     * @param assistant    Id of the played assistant.
     * @param coins        Number of coins.
     * @param name         Name of the school board owner.
     * @param wizard       Wizard type link to the player.
     * @param active       Boolean value to set the active player.
     * @param exp          Boolean value to set whether the school board is for expert game mode.
     */
    public static void print(Ansi ansi,
                             Map<HouseColor, Integer> entrance,
                             Map<HouseColor, Integer> diningRoom,
                             Map<HouseColor, Boolean> professors,
                             TowerType tower,
                             int towersNumber,
                             int assistant,
                             int coins,
                             String name,
                             WizardType wizard,
                             boolean active,
                             boolean exp
    ) {

        int blankLineChars = 26;
        defaultForeground(ansi, active, wizard);

        // Line #1

        StringBuilder temp = new StringBuilder(" ");
        if (name.length() > blankLineChars) {
            for (int i = 0; i < blankLineChars - 1; i++) temp.append(name.charAt(i));
            ansi.append(temp.toString());
        } else {
            for (int i = 0; i < blankLineChars - 1; i++) temp.append((i < name.length()) ? name.charAt(i) : " ");
        }
        ansi.append(temp.toString());
        newLine(ansi);

        // Line #2

        ansi.append("┌─────┬──────┬─────┬─────┐");
        newLine(ansi);

        // Line #3

        ansi.append("│ ENT │  DR  │ PRF │ TWR │");
        newLine(ansi);

        // Line #4

        ansi.append("├─────┼──────┼─────┼─────┤");
        newLine(ansi);

        // Line #5

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.GREEN), HouseColor.GREEN, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.GREEN), HouseColor.GREEN, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.GREEN) ? 1 : 0, HouseColor.GREEN, ansi, 0, active, wizard);
        ansi.append(" │ ");
        switch (tower) {
            case BLACK -> foreground(ansi, TowerBlack.getInstance());
            case GREY -> foreground(ansi, TowerGrey.getInstance());
            case WHITE -> foreground(ansi, TowerWhite.getInstance());
        }
        ansi.append("●");
        defaultForeground(ansi, active, wizard);
        ansi.append(String.format("x%01d", towersNumber));

        ansi.append(" │");
        newLine(ansi);

        // Line #6

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #7

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.RED), HouseColor.RED, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.RED), HouseColor.RED, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.RED) ? 1 : 0, HouseColor.RED, ansi, 0, active, wizard);
        ansi.append(" │ AST │");
        newLine(ansi);

        // Line #8

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #9

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.YELLOW), HouseColor.YELLOW, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.YELLOW), HouseColor.YELLOW, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.YELLOW) ? 1 : 0, HouseColor.YELLOW, ansi, 0, active, wizard);
        ansi.append(" │");
        if(assistant!=0) ansi.append(String.format("AST%02d", assistant));
        else ansi.append("     ");
        ansi.append("│");
        newLine(ansi);

        // Line #10

        ansi.append("│     │      │     ├─────┤");
        newLine(ansi);

        // Line #11

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.FUCHSIA) ? 1 : 0, HouseColor.FUCHSIA, ansi, 0, active, wizard);
        if (exp) ansi.append(" │ CNS │");
        else ansi.append(" │     │");
        newLine(ansi);

        // Line #12

        if (exp) ansi.append("│     │      │     ├─────┤");
        else ansi.append("│     │      │     │     │");
        newLine(ansi);

        // Line #13

        ansi.append("│ ");
        parsePawn(entrance.get(HouseColor.BLUE), HouseColor.BLUE, ansi, 1, active, wizard);
        ansi.append(" │ ");
        parsePawn(diningRoom.get(HouseColor.BLUE), HouseColor.BLUE, ansi, 2, active, wizard);
        ansi.append(" │ ");
        parsePawn(professors.get(HouseColor.BLUE) ? 1 : 0, HouseColor.BLUE, ansi, 0, active, wizard);
        if (exp) ansi.append(String.format(" │ x%02d │", coins));
        else ansi.append(" │     │");
        newLine(ansi);

        // Line #14

        ansi.append("└─────┴──────┴─────┴─────┘");
        resetCursor(ansi);
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, SchoolBoardNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi) {
        moveCursor(ansi, SchoolBoardReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ansi   Ansi stream where to write.
     * @param active Boolean value to set the active player.
     * @param wizard Wizard type link to the player.
     */
    private static void defaultForeground(Ansi ansi, boolean active, WizardType wizard) {
        switch (wizard) {
            case FUCHSIA -> foreground(ansi, active ? WizardFuchsia.getInstance() : WizardDarkFuchsia.getInstance());
            case GREEN -> foreground(ansi, active ? WizardGreen.getInstance() : WizardDarkGreen.getInstance());
            case WHITE -> foreground(ansi, active ? WizardWhite.getInstance() : WizardDarkWhite.getInstance());
            case YELLOW -> foreground(ansi, active ? WizardYellow.getInstance() : WizardDarkYellow.getInstance());
        }
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param pawnsNumber Number of pawns.
     * @param houseColor  Pawn color.
     * @param ansi        Ansi stream where to write.
     * @param precision   Number of digits.
     * @param active      Boolean value to set the active player.
     * @param wizard      Wizard type link to the player.
     */
    private static void parsePawn(int pawnsNumber, HouseColor houseColor, Ansi ansi, int precision, boolean active, WizardType wizard) {
        if (precision == 0) {
            foreground(ansi, (pawnsNumber != 0) ? getColourFrom(houseColor) : DarkGrey.getInstance());
            ansi.append("●");
        }
        else {
            if (pawnsNumber != 0) {
                foreground(ansi, getColourFrom(houseColor));
                ansi.append("●");
                defaultForeground(ansi, active, wizard);
                printStudentsNumber(ansi, precision, pawnsNumber);
            } else {
                foreground(ansi, DarkGrey.getInstance());
                ansi.append("●x0");
            }
        }
        defaultForeground(ansi, active, wizard);
    }

    /**
     * Prints the number of students.
     *
     * @param ansi        Ansi stream where to write.
     * @param precision   Number of digits.
     * @param pawnsNumber Number of pawns.
     */
    private static void printStudentsNumber(Ansi ansi, int precision, int pawnsNumber) {
        if (precision == 1)
            ansi.append(String.format("x%01d", pawnsNumber));
        else
            ansi.append(String.format("x%02d", pawnsNumber));
    }
}
