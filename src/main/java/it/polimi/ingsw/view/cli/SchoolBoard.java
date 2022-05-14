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
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║
// ║ │     │      │     │     │ ║
// ║ │ ●x0 │ ●x00 │  ●  │     │ ║
// ║ └─────┴──────┴─────┴─────┘ ║
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
public class SchoolBoard {

    private SchoolBoard() {
    }

    /**
     * Print the school board.
     *
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
    public static Ansi print(Map<HouseColor, Integer> entrance, Map<HouseColor, Integer> diningRoom, Map<HouseColor, Boolean> professors, TowerType tower, int towersNumber, int assistant, int coins, String name, WizardType wizard, boolean active, boolean exp) {

        Ansi ansi = new Ansi();

        int blankLineChars = 26;
        ansi.a(defaultForeground(active, wizard));

        // Line #1

        StringBuilder temp = new StringBuilder(" ");
        if (name.length() > blankLineChars) {
            for (int i = 0; i < blankLineChars - 1; i++) temp.append(name.charAt(i));
            ansi.a(temp.toString());
        } else {
            for (int i = 0; i < blankLineChars - 1; i++) temp.append((i < name.length()) ? name.charAt(i) : " ");
        }
        ansi.a(temp.toString());
        ansi.a(newLine());

        // Line #2

        ansi.a("┌─────┬──────┬─────┬─────┐");
        ansi.a(newLine());

        // Line #3

        ansi.a("│ ENT │  DR  │ PRF │ TWR │");
        ansi.a(newLine());

        // Line #4

        ansi.a("├─────┼──────┼─────┼─────┤");
        ansi.a(newLine());

        // Line #5

        ansi.a("│ ");
        ansi.a(parsePawn(entrance.get(HouseColor.GREEN), HouseColor.GREEN, 1, active, wizard));
        ansi.a(" │ ");
        ansi.a(parsePawn(diningRoom.get(HouseColor.GREEN), HouseColor.GREEN, 2, active, wizard));
        ansi.a(" │  ");
        ansi.a(parsePawn(professors.get(HouseColor.GREEN) ? 1 : 0, HouseColor.GREEN, 0, active, wizard));
        ansi.a("  │ ");
        ansi.a(
                switch (tower) {
                    case BLACK -> foreground(TowerBlack.getInstance());
                    case GREY -> foreground(TowerGrey.getInstance());
                    case WHITE -> foreground(TowerWhite.getInstance());
                }
        );
        ansi.a("●");
        ansi.a(defaultForeground(true, wizard));
        ansi.a(String.format("x%01d", towersNumber));
        ansi.a(defaultForeground(active, wizard));
        ansi.a(" │");
        ansi.a(newLine());

        // Line #6

        ansi.a("│     │      │     ├─────┤");
        ansi.a(newLine());

        // Line #7

        ansi.a("│ ");
        ansi.a(parsePawn(entrance.get(HouseColor.RED), HouseColor.RED, 1, active, wizard));
        ansi.a(" │ ");
        ansi.a(parsePawn(diningRoom.get(HouseColor.RED), HouseColor.RED, 2, active, wizard));
        ansi.a(" │  ");
        ansi.a(parsePawn(professors.get(HouseColor.RED) ? 1 : 0, HouseColor.RED, 0, active, wizard));
        ansi.a("  │ AST │");
        ansi.a(newLine());

        // Line #8

        ansi.a("│     │      │     ├─────┤");
        ansi.a(newLine());

        // Line #9

        ansi.a("│ ");
        ansi.a(parsePawn(entrance.get(HouseColor.YELLOW), HouseColor.YELLOW, 1, active, wizard));
        ansi.a(" │ ");
        ansi.a(parsePawn(diningRoom.get(HouseColor.YELLOW), HouseColor.YELLOW, 2, active, wizard));
        ansi.a(" │  ");
        ansi.a(parsePawn(professors.get(HouseColor.YELLOW) ? 1 : 0, HouseColor.YELLOW, 0, active, wizard));
        ansi.a("  │");
        if (assistant != 0) {
            ansi.a(defaultForeground(true, wizard));
            ansi.a(String.format("AST%02d", assistant));
            ansi.a(defaultForeground(active, wizard));
        } else ansi.a("     ");
        ansi.a("│");
        ansi.a(newLine());

        // Line #10

        ansi.a("│     │      │     ├─────┤");
        ansi.a(newLine());

        // Line #11

        ansi.a("│ ");
        ansi.a(parsePawn(entrance.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, 1, active, wizard));
        ansi.a(" │ ");
        ansi.a(parsePawn(diningRoom.get(HouseColor.FUCHSIA), HouseColor.FUCHSIA, 2, active, wizard));
        ansi.a(" │  ");
        ansi.a(parsePawn(professors.get(HouseColor.FUCHSIA) ? 1 : 0, HouseColor.FUCHSIA, 0, active, wizard));
        if (exp) ansi.a("  │ CNS │");
        else ansi.a("  │     │");
        ansi.a(newLine());

        // Line #12

        if (exp) ansi.a("│     │      │     ├─────┤");
        else ansi.a("│     │      │     │     │");
        ansi.a(newLine());

        // Line #13

        ansi.a("│ ");
        ansi.a(parsePawn(entrance.get(HouseColor.BLUE), HouseColor.BLUE, 1, active, wizard));
        ansi.a(" │ ");
        ansi.a(parsePawn(diningRoom.get(HouseColor.BLUE), HouseColor.BLUE, 2, active, wizard));
        ansi.a(" │  ");
        ansi.a(parsePawn(professors.get(HouseColor.BLUE) ? 1 : 0, HouseColor.BLUE, 0, active, wizard));
        if (exp) {
            ansi.a("  │ ");
            ansi.a(defaultForeground(true, wizard));
            ansi.a(String.format("x%02d", coins));
            ansi.a(defaultForeground(active, wizard));
            ansi.a(" │");
        } else ansi.a("  │     │");
        ansi.a(newLine());

        // Line #14

        ansi.a("└─────┴──────┴─────┴─────┘");
        ansi.a(resetCursor());

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     */
    private static Ansi newLine() {
        return moveCursor(SchoolBoardNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     */
    private static Ansi resetCursor() {
        return moveCursor(SchoolBoardReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param active Boolean value to set the active player.
     * @param wizard Wizard type link to the player.
     */
    private static Ansi defaultForeground(boolean active, WizardType wizard) {
        return switch (wizard) {
            case FUCHSIA -> foreground(active ? WizardFuchsia.getInstance() : WizardDarkFuchsia.getInstance());
            case GREEN -> foreground(active ? WizardGreen.getInstance() : WizardDarkGreen.getInstance());
            case WHITE -> foreground(active ? WizardWhite.getInstance() : WizardDarkWhite.getInstance());
            case YELLOW -> foreground(active ? WizardYellow.getInstance() : WizardDarkYellow.getInstance());
        };
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param pawnsNumber Number of pawns.
     * @param houseColor  Pawn color.
     * @param precision   Number of digits.
     * @param active      Boolean value to set the active player.
     * @param wizard      Wizard type link to the player.
     */
    private static Ansi parsePawn(int pawnsNumber, HouseColor houseColor, int precision, boolean active, WizardType wizard) {

        Ansi ansi = new Ansi();

        if (precision == 0) {
            ansi.a(foreground((pawnsNumber != 0) ? getColourFrom(houseColor) : DarkGrey.getInstance()));
            ansi.a("●");
        } else {
            if (pawnsNumber != 0) {
                ansi.a(foreground(getColourFrom(houseColor)));
                ansi.a("●");
                ansi.a(defaultForeground(true, wizard));
                ansi.a(printStudentsNumber(precision, pawnsNumber));
            } else {
                ansi.a(foreground(DarkGrey.getInstance()));
                ansi.a(precision == 2 ? "●x00" : "●x0");
            }
        }
        ansi.a(defaultForeground(active, wizard));

        return ansi;
    }

    /**
     * Prints the number of students.
     *
     * @param precision   Number of digits.
     * @param pawnsNumber Number of pawns.
     */
    private static String printStudentsNumber(int precision, int pawnsNumber) {
        if (precision == 1) return String.format("x%01d", pawnsNumber);
        else return String.format("x%02d", pawnsNumber);
    }
}
