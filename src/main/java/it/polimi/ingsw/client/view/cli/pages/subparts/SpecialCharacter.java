package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.DarkGrey;
import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.SpecialCharacterNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.SpecialCharacterReset;
import it.polimi.ingsw.client.view.cli.coordinates.SpecialCharacterResetBan;
import it.polimi.ingsw.client.view.cli.coordinates.SpecialCharacterResetStudents;
import it.polimi.ingsw.utilities.HouseColor;
import org.fusesource.jansi.Ansi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.client.view.cli.Utilities.bold;
import static it.polimi.ingsw.client.view.cli.Utilities.foreground;

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
     * @param students  Map of students on the card.
     * @return The Ansi stream to print to terminal.
     */
    public static Ansi print(int id, int price, boolean active, Integer banNumber, Map<HouseColor, Integer> students) {

        Ansi ansi = new Ansi();

        ansi.a(defaultForeground(active));

        // Line #1

        ansi.a("┌───────┐");
        ansi.a(newLine());

        // Line #2

        ansi.a("│  ");
        ansi.a(bold(true));
        ansi.a("CHR");
        ansi.a(bold(false));
        ansi.a("  │");
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

        // Print closure

        if (banNumber != null) {
            ansi.a(newLine());
            ansi.a(printBan(banNumber, active));
        } else if (students != null) {
            ansi.a(newLine());
            ansi.a(printStudents(students, active));
        } else {
            ansi.a(resetCursor(ResetType.STANDARD));
        }

        return ansi;
    }

    /**
     * Private method to print the students on the card.
     *
     * @param students Map of students on the card.
     * @param active   True whether the card has been paid, false otherwise.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi printStudents(Map<HouseColor, Integer> students, boolean active) {

        Ansi ansi = new Ansi();

        List<HouseColor> tmp = new ArrayList<>();
        Arrays.stream(HouseColor.values()).forEach(color -> {
            for (int i = 0; i < students.get(color); i++) tmp.add(color);
        });

        // Line #9

        ansi.a("┌───────┐");
        ansi.a(newLine());

        // Line #10

        ansi.a("│ ");
        ansi.a(parseStudent((tmp.size() > 0) ? tmp.get(0) : null, active));
        ansi.a(" ");
        if (students.size() == 6) ansi.a(parseStudent((tmp.size() > 4) ? tmp.get(4) : null, active));
        else ansi.a(" ");
        ansi.a(" ");
        ansi.a(parseStudent((tmp.size() > 1) ? tmp.get(1) : null, active));
        ansi.a(" │");
        ansi.a(newLine());

        // Line #11

        ansi.a("│ ");
        ansi.a(parseStudent((tmp.size() > 2) ? tmp.get(2) : null, active));
        ansi.a(" ");
        if (students.size() == 6) ansi.a(parseStudent((tmp.size() > 5) ? tmp.get(5) : null, active));
        else ansi.a(" ");
        ansi.a(" ");
        ansi.a(parseStudent((tmp.size() > 3) ? tmp.get(3) : null, active));
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
     * @return The Ansi stream to print to terminal.
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
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(SpecialCharacterNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi resetCursor(ResetType when) {
        return switch (when) {
            case BAN -> Utilities.moveCursor(SpecialCharacterResetBan.getInstance());
            case STUDENTS -> Utilities.moveCursor(SpecialCharacterResetStudents.getInstance());
            default -> Utilities.moveCursor(SpecialCharacterReset.getInstance());
        };
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param active Boolean second to set the active card.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi defaultForeground(boolean active) {
        if (active) return foreground(White.getInstance());
        else return foreground(Grey.getInstance());
    }

    /**
     * Parses the number of students of a specific color in order to render them correctly.
     *
     * @param houseColor Color of the student.
     * @param active     Boolean second to set the active card.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi parseStudent(HouseColor houseColor, boolean active) {

        Ansi ansi = new Ansi();

        if (houseColor != null) {
            ansi.a(foreground(Utilities.getColourFrom(houseColor)));
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
