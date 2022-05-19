package it.polimi.ingsw.client.view.cli.pages;

import it.polimi.ingsw.client.view.cli.Utilities;
import it.polimi.ingsw.client.view.cli.colours.*;
import it.polimi.ingsw.client.view.cli.coordinates.LoginNewLine;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptions;
import it.polimi.ingsw.client.view.cli.coordinates.LoginOptionsInput;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static it.polimi.ingsw.client.view.cli.Utilities.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Login CLI printer.
 *
 * @author Matteo Negro
 */
public class Game {
    private static final String[] title = {
            " - THE GAME - "
    };

    private Game() {
    }

    /**
     * Prints the whole game selection centering it.
     *
     * @param terminal Terminal where to write.
     */
    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 17) / 2, (terminal.getWidth() - 40) / 2));
        terminal.writer().print(print());
        terminal.flush();
    }

    /**
     * Prints the whole game selection.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi print() {
        Ansi ansi = new Ansi();
        ansi.a(printTitle());
        return ansi;
    }

    /**
     * Prints the title.
     *
     * @return The generated Ansi stream.
     */
    private static Ansi printTitle() {
        Ansi ansi = new Ansi();
        ansi.a(foreground(Title.getInstance()));
        ansi.a(printText(title));
        return ansi;
    }
}
