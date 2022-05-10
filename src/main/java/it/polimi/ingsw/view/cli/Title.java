package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.cli.colours.Subtitle;
import it.polimi.ingsw.view.cli.colours.White;
import it.polimi.ingsw.view.cli.coordinates.TitleNewLine;
import it.polimi.ingsw.view.cli.coordinates.TitleReset;
import org.fusesource.jansi.Ansi;

import static it.polimi.ingsw.view.cli.Utilities.foreground;
import static it.polimi.ingsw.view.cli.Utilities.moveCursor;

//  ________          __                     __
// |        \        |  \                   |  \
// | ████████ ______  \██ ______  _______  _| ██_   __    __  _______
// | ██__    /      \|  \|      \|       \|   ██ \ |  \  |  \/       \
// | ██  \  |  ██████\ ██ \██████\ ███████\\██████ | ██  | ██  ███████
// | █████  | ██   \██ ██/      ██ ██  | ██ | ██ __| ██  | ██\██    \
// | ██_____| ██     | ██  ███████ ██  | ██ | ██|  \ ██__/ ██_\██████\
// | ██     \ ██     | ██\██    ██ ██  | ██  \██  ██\██    ██       ██
//  \████████\██      \██ \███████\██   \██   \████ _\███████\███████
//                                                 |  \__| ██
//                                                  \██    ██
//                                                   \██████

public class Title {

    private Title() {
    }

    static void printTitle(Ansi ansi) {

        foreground(ansi, it.polimi.ingsw.view.cli.colours.Title.getInstance());

        // Line #1

        ansi.append(" ________          __                     __                       ");
        newLine(ansi);

        // Line #2

        ansi.append("|        \\        |  \\                   |  \\                      ");
        newLine(ansi);

        // Line #3

        ansi.append("| ████████ ______  \\██ ______  _______  _| ██_   __    __  _______ ");
        newLine(ansi);

        // Line #4

        ansi.append("| ██__    /      \\|  \\|      \\|       \\|   ██ \\ |  \\  |  \\/       \\");
        newLine(ansi);

        // Line #5

        ansi.append("| ██  \\  |  ██████\\ ██ \\██████\\ ███████\\\\██████ | ██  | ██  ███████");
        newLine(ansi);

        // Line #6

        ansi.append("| █████  | ██   \\██ ██/      ██ ██  | ██ | ██ __| ██  | ██\\██    \\ ");
        newLine(ansi);

        // Line #7

        ansi.append("| ██_____| ██     | ██  ███████ ██  | ██ | ██|  \\ ██__/ ██_\\██████\\");
        newLine(ansi);

        // Line #8

        ansi.append("| ██     \\ ██     | ██\\██    ██ ██  | ██  \\██  ██\\██    ██       ██");
        newLine(ansi);

        // Line #9

        ansi.append(" \\████████\\██      \\██ \\███████\\██   \\██   \\████ _\\███████\\███████ ");
        newLine(ansi);

        // Line #10

        ansi.append("                                                |  \\__| ██         ");
        newLine(ansi);

        // Line #11

        ansi.append("                                                 \\██    ██         ");
        newLine(ansi);

        // Line #12

        ansi.append("                                                  \\██████          ");
        resetCursor(ansi);
    }

    static void printSubtitle(Ansi ansi) {

        foreground(ansi, Subtitle.getInstance());

        ansi.cursorMove(14, 0);
        ansi.append("A game by Leo Colovini for 2-4 players.");

        ansi.cursorMove(-53, 1);
        ansi.append("Project developed by Riccardo Milici, Riccardo Motta, Matteo Negro.");

        ansi.cursorMove(-67, -1);
    }

    /**
     * Moves the cursor in order to write a new line.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void newLine(Ansi ansi) {
        moveCursor(ansi, TitleNewLine.getInstance());
    }

    /**
     * Moves the cursor to the original position.
     *
     * @param ansi Ansi stream where to write.
     */
    private static void resetCursor(Ansi ansi) {
        moveCursor(ansi, TitleReset.getInstance());
    }
}
