package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.DarkGrey;
import it.polimi.ingsw.view.cli.colours.Grey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterBanReset;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterNewLine;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterReset;
import it.polimi.ingsw.view.cli.coordinates.SpecialCharacterStudentsReset;
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
// ║ │       │ ║
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
// ║ │       │ ║
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

// TODO: Number of student, how manage them??

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
     */
    public static void print(Ansi ansi, int id, int price, boolean active, int banNumber, List<HouseColor> students) {

        defaultForeground(ansi, active);

        // Line #1

        ansi.append("┌───────┐");
        newLine(ansi);

        // Line #2

        ansi.append("│  CHR  │");
        newLine(ansi);

        // Line #3

        ansi.append("├───────┤");
        newLine(ansi);

        // Line #4

        ansi.append(String.format("│ CHR%02d │", id));
        newLine(ansi);

        // Line #5

        ansi.append("│       │");
        newLine(ansi);

        // Line #6

        ansi.append(String.format("│  %01dCS  │", price));
        newLine(ansi);

        // Line #7

        ansi.append("│       │");
        newLine(ansi);

        // Line #8

        if (banNumber != 0) {
            ansi.append(String.format("│  !x%01d  │", banNumber));
            newLine(ansi);
        } else if (students != null) {
            ansi.append("│ ");
            parseStudent(students.get(0), ansi, active);
            ansi.append(" ");
            parseStudent(students.get(1), ansi, active);
            ansi.append(" ");
            parseStudent(students.get(2), ansi, active);
            ansi.append(" │");
            newLine(ansi);
        } else {
            ansi.append("└───────┘");
            resetCursor(ansi, "standard");
        }

        // Line #9

        if (banNumber != 0) {
            ansi.append("└───────┘");
            resetCursor(ansi, "ban");
        } else {
            ansi.append("│ ");
            parseStudent(students.get(3), ansi, active);
            ansi.append(" ");
            parseStudent(students.get(4), ansi, active);
            ansi.append(" ");
            parseStudent(students.get(5), ansi, active);
            ansi.append(" │");
            newLine(ansi);
        }

        // Line #10

        if (students != null) {
            ansi.append("└───────┘");
            resetCursor(ansi, "students");
        }

    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, SpecialCharacterNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi, String when) {
        switch (when) {
            case "ban" -> moveCursor(ansi, SpecialCharacterBanReset.getInstance());
            case "students" -> moveCursor(ansi, SpecialCharacterStudentsReset.getInstance());
            default -> moveCursor(ansi, SpecialCharacterReset.getInstance());
        }
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ansi   Ansi stream where to write.
     * @param active Boolean value to set the active card.
     */
    private static void defaultForeground(Ansi ansi, boolean active) {
        if (active) foreground(ansi, White.getInstance());
        else foreground(ansi, Grey.getInstance());
    }


    /**
     * Parses the number of students of a specific color in order to render them correctly.
     */
    private static void parseStudent(HouseColor houseColor, Ansi ansi, boolean active) {
        if (houseColor != null) {
            foreground(ansi, getColourFrom(houseColor));
            ansi.append("●");
        } else {
            foreground(ansi, DarkGrey.getInstance());
            ansi.append("●");

        }
        defaultForeground(ansi, active);
    }
}
