package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.DarkGrey;
import it.polimi.ingsw.view.cli.colours.Grey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterNewLine;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterReset;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterResetBan;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterResetStudents;
import org.fusesource.jansi.Ansi;

import java.util.List;

import static it.polimi.ingsw.view.cli.Utilities.*;

// ╔[CHARACTER]╗
// ║           ║
// ║ ┌───────┐ ║
// ║ │  CHR  │ ║
// ║ ├───────┤ ║
// ║ │ CHR00 │ ║
// ║ │       │ ║
// ║ │  0CS  │ ║
// ║ └───────┘ ║
// ║ ┌───────┐ ║
// ║ │ ● ● ● │ ║
// ║ │ ● ● ● │ ║
// ║ └───────┘ ║
// ╚═══════════╝

// ╔[CHARACTER]╗
// ║           ║
// ║ ┌───────┐ ║
// ║ │  CHR  │ ║
// ║ ├───────┤ ║
// ║ │ CHR00 │ ║
// ║ │       │ ║
// ║ │  0CS  │ ║
// ║ └───────┘ ║
// ║ ┌───────┐ ║
// ║ │  !x0  │ ║
// ║ └───────┘ ║
// ╚═══════════╝

// ╔[CHARACTER]╗
// ║           ║
// ║ ┌───────┐ ║
// ║ │  CHR  │ ║
// ║ ├───────┤ ║
// ║ │ CHR00 │ ║
// ║ │       │ ║
// ║ │  0CS  │ ║
// ║ └───────┘ ║
// ╚═══════════╝

/**
 * Class used for printing a single special character.
 * It always returns to the starting point.
 *
 * @author Matteo Negro
 */
public class SpecialCharacter {

    private SpecialCharacter() {
    }

    /**
     * Print the special character.
     *
     * @param id        Id of the special character.
     * @param price     Price to activate the card.
     * @param active    True whether the card has been paid, false otherwise.
     * @param banNumber Number of ban (-1, otherwise).
     * @param students  List of students on the card.
     */
    public static Ansi print(int id, int price, boolean active, int banNumber, List<HouseColor> students) {

        Ansi ansi = new Ansi();

        ansi.a(defaultForeground(active));

        // Line #1

        ansi.a("┌───────┐");
        ansi.a(newLine());

        // Line #2

        ansi.a("│  CHR  │");
        ansi.a(newLine());

        // Line #3

        ansi.a("├───────┤");
        ansi.a(newLine());

        // Line #4

        ansi.a(String.format("│ CHR%02d │", id));
        ansi.a(newLine());

        // Line #5

        ansi.a("│       │");
        ansi.a(newLine());

        // Line #6

        ansi.a(String.format("│  %01dCS  │", price));
        ansi.a(newLine());

        // Line #7

        ansi.a("└───────┘");
        ansi.a(newLine());

        // Print closure

        if (banNumber != -1) {
            ansi.a(printBan(banNumber, active));
        } else if (students != null) {
            ansi.a(printStudents(students, active));
        } else {
            ansi.a(resetCursor(ResetType.STANDARD));
        }

        return ansi;
    }

    /**
     * Private method to print the students on the card.
     *
     * @param students List of students on the card.
     * @param active   True whether the card has been paid, false otherwise.
     */
    private static Ansi printStudents(List<HouseColor> students, boolean active) {

        Ansi ansi = new Ansi();

        // Line #9

        ansi.a("┌───────┐");
        ansi.a(newLine());

        // Line #10

        ansi.a("│ ");
        ansi.a(parseStudent(students.get(0), active));
        ansi.a(" ");
        if (students.size() == 6) ansi.a(parseStudent(students.get(1), active));
        else ansi.a(" ");
        ansi.a(" ");
        ansi.a(parseStudent(students.get(2), active));
        ansi.a(" │");
        ansi.a(newLine());

        // Line #11

        ansi.a("│ ");
        ansi.a(parseStudent(students.get(3), active));
        ansi.a(" ");
        if (students.size() == 6) ansi.a(parseStudent(students.get(4), active));
        else ansi.a(" ");
        ansi.a(" ");
        ansi.a(parseStudent(students.get(5), active));
        ansi.a(" │");
        ansi.a(newLine());

        // Line #12

        ansi.a("└───────┘");
        ansi.a(resetCursor(ResetType.STUDENTS));

        return ansi;
    }

    /**
     * Private method to print the ban on the card.
     *
     * @param banNumber Number of ban (-1, otherwise).
     * @param active    True whether the card has been paid, false otherwise.
     */
    private static Ansi printBan(int banNumber, boolean active) {

        Ansi ansi = new Ansi();

        // Line #9

        ansi.a("┌───────┐");
        ansi.a(newLine());

        // Line #10

        if (banNumber != 0) ansi.a(String.format("│  !x%01d  │", banNumber));
        else {
            ansi.a("│  ");
            ansi.a(foreground(DarkGrey.getInstance()));
            ansi.a(String.format("!x%01d", banNumber));
            ansi.a(defaultForeground(active));
            ansi.a("  │");
        }
        ansi.a(newLine());

        // Line #11

        ansi.a("└───────┘");
        ansi.a(resetCursor(ResetType.BAN));

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     */
    private static Ansi newLine() {
        return moveCursor(SpecialCharacterNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     */
    private static Ansi resetCursor(ResetType when) {
        return switch (when) {
            case BAN -> moveCursor(SpecialCharacterResetBan.getInstance());
            case STUDENTS -> moveCursor(SpecialCharacterResetStudents.getInstance());
            default -> moveCursor(SpecialCharacterReset.getInstance());
        };
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param active Boolean value to set the active card.
     */
    private static Ansi defaultForeground(boolean active) {
        if (active) return foreground(White.getInstance());
        else return foreground(Grey.getInstance());
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param houseColor Color of the student.
     * @param active     Boolean value to set the active card.
     */
    private static Ansi parseStudent(HouseColor houseColor, boolean active) {

        Ansi ansi = new Ansi();

        if (houseColor != null) {
            ansi.a(foreground(getColourFrom(houseColor)));
            ansi.a("●");
        } else {
            ansi.a(foreground(DarkGrey.getInstance()));
            ansi.a("●");

        }
        ansi.a(defaultForeground(active));

        return ansi;
    }

    private enum ResetType {BAN, STANDARD, STUDENTS}
}
