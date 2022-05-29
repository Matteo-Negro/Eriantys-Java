package it.polimi.ingsw.client.view.cli.pages.subparts;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.DarkGrey;
import it.polimi.ingsw.client.view.cli.colours.Grey;
import it.polimi.ingsw.client.view.cli.colours.White;
import it.polimi.ingsw.client.view.cli.coordinates.CloudNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.CloudReset;
import it.polimi.ingsw.utilities.HouseColor;
import org.fusesource.jansi.Ansi;

import java.util.List;

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
 *
 * @author Riccardo Motta
 */
public class Cloud {

    private Cloud() {
    }

    /**
     * Prints the cloud.
     *
     * @param id             Id of the cloud.
     * @param students       List of students which are on the cloud.
     * @param studentsNumber Number of students that should be at most on the cloud.
     * @return The Ansi stream to print to terminal.
     */
    public static Ansi print(int id, List<HouseColor> students, int studentsNumber) {

        Ansi ansi = new Ansi();

        ansi.a(defaultForeground(students));

        // Line #1

        ansi.a("   ·······   ");
        ansi.a(newLine());

        // Line #2

        ansi.a(" ··       ·· ");
        ansi.a(newLine());

        // Line #3

        if (students != null) {
            if (studentsNumber == 4) {
                ansi.a("·   ");
                ansi.a(Utilities.foreground(Utilities.getColourFrom(students.get(0))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("   ");
                ansi.a(Utilities.foreground(Utilities.getColourFrom(students.get(1))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("   ·");
            } else {
                ansi.a("·     ");
                ansi.a(Utilities.foreground(Utilities.getColourFrom(students.get(0))));
                ansi.a("●");
                ansi.a(defaultForeground(students));
                ansi.a("     ·");
            }
        } else
            ansi.a(studentsNumber == 4 ? "·   ●   ●   ·" : "·     ●     ·");
        ansi.a(newLine());

        // Line #4

        ansi.a("·           ·");
        ansi.a(newLine());

        // Line #5

        if (students != null) {
            ansi.a("·   ");
            ansi.a(Utilities.foreground(Utilities.getColourFrom(students.get(studentsNumber == 4 ? 2 : 1))));
            ansi.a("●");
            ansi.a(defaultForeground(students));
            ansi.a("   ");
            ansi.a(Utilities.foreground(Utilities.getColourFrom(students.get(studentsNumber == 4 ? 3 : 2))));
            ansi.a("●");
            ansi.a(defaultForeground(students));
            ansi.a("   ·");
        } else
            ansi.a("·   ●   ●   ·");
        ansi.a(newLine());

        // Line #6

        ansi.a(" ··       ·· ");
        ansi.a(newLine());

        // Line #7

        ansi.a("   ·······   ");
        ansi.a(newLine());

        // Line #8

        ansi.a(Utilities.bold(true));
        ansi.a(Utilities.foreground(students != null ? Grey.getInstance() : DarkGrey.getInstance()));
        ansi.a(String.format("     CL%01d     ", id));
        ansi.a(Utilities.bold(false));
        ansi.a(resetCursor());

        return ansi;
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi newLine() {
        return Utilities.moveCursor(CloudNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi resetCursor() {
        return Utilities.moveCursor(CloudReset.getInstance());
    }

    /**
     * Gets the default foreground for writing things.
     *
     * @param students List of students on the cloud.
     * @return The Ansi stream to print to terminal.
     */
    private static Ansi defaultForeground(List<HouseColor> students) {
        if (students == null)
            return Utilities.foreground(DarkGrey.getInstance());
        else
            return Utilities.foreground(White.getInstance());
    }
}
