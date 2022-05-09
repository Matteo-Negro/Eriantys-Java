package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.DarkGrey;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.CloudNewLine;
import it.polimi.ingsw.view.cli.coordinates.CloudReset;
import org.fusesource.jansi.Ansi;

import java.util.List;

import static it.polimi.ingsw.view.cli.Utilities.*;

// ╔[TEMPLATE_CL]╗
// ║   ·······   ║
// ║ ··       ·· ║
// ║·   ● ● ●   ·║
// ║·           ·║
// ║·   ●   ●   ·║
// ║ ··       ·· ║
// ║   ·······   ║
// ║     CL0     ║
// ╚═════════════╝

/**
 * Class used for printing a single cloud.
 * It always returns to the starting point.
 */
public class Cloud {

    private Cloud() {
    }

    /**
     * Prints the cloud.
     *
     * @param ansi           Ansi stream where to write.
     * @param id             Id of the cloud.
     * @param students       List of students which are on the cloud.
     * @param studentsNumber Number of students that should be at most on the cloud.
     */
    static void print(Ansi ansi, int id, List<HouseColor> students, int studentsNumber) {

        defaultForeground(ansi, students);

        // Line #1

        ansi.append("   ·······   ");
        newLine(ansi);

        // Line #2

        ansi.append(" ··       ·· ");
        newLine(ansi);

        // Line #3

        if (students != null) {
            if (studentsNumber == 4) {
                ansi.append("·   ");
                foreground(ansi, getColourFrom(students.get(0)));
                ansi.append("●");
                defaultForeground(ansi, students);
                ansi.append("   ");
                foreground(ansi, getColourFrom(students.get(1)));
                ansi.append("●");
                defaultForeground(ansi, students);
                ansi.append("   ·");
            } else {
                ansi.append("·     ");
                foreground(ansi, getColourFrom(students.get(0)));
                ansi.append("●");
                defaultForeground(ansi, students);
                ansi.append("     ·");
            }
        } else
            ansi.append(studentsNumber == 4 ? "·   ●   ●   ·" : "·     ●     ·");
        newLine(ansi);

        // Line #4

        ansi.append("·           ·");
        newLine(ansi);

        // Line #5

        if (students != null) {
            ansi.append("·   ");
            foreground(ansi, getColourFrom(students.get(studentsNumber == 4 ? 2 : 1)));
            ansi.append("●");
            defaultForeground(ansi, students);
            ansi.append("   ");
            foreground(ansi, getColourFrom(students.get(studentsNumber == 4 ? 3 : 2)));
            ansi.append("●");
            defaultForeground(ansi, students);
            ansi.append("   ·");
        } else
            ansi.append("·   ●   ●   ·");
        newLine(ansi);

        // Line #6

        ansi.append(" ··       ·· ");
        newLine(ansi);

        // Line #7

        ansi.append("   ·······   ");
        newLine(ansi);

        // Line #8

        ansi.append("     CL${id}     ".replace("${id}", String.format("%01d", id)));
        resetCursor(ansi);
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, CloudNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi) {
        moveCursor(ansi, CloudReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param ansi     Ansi stream where to write.
     * @param students List of students on the cloud.
     */
    private static void defaultForeground(Ansi ansi, List<HouseColor> students) {
        if (students == null)
            foreground(ansi, DarkGrey.getInstance());
        else
            foreground(ansi, White.getInstance());
    }
}
