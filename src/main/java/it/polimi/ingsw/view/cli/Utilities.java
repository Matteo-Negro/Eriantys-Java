package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.*;
import org.fusesource.jansi.Ansi;
import org.jline.reader.Completer;
import org.jline.reader.History;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * CLI print utilities
 */
public class Utilities {

    /**
     * Private constructor: this class cannot be instanced.
     */
    private Utilities() {
    }

    /**
     * Clears the screen with a default background.
     *
     * @param terminal Terminal where to execute.
     * @param def      true if has to be reset to the default situation.
     */
    public static void clearScreen(Terminal terminal, boolean def) {
        Ansi ansi = new Ansi();
        ansi.cursor(0, 0);
        if (def) {
            ansi.bgDefault();
            ansi.fgDefault();
            ansi.saveCursorPosition();
        } else {
            ansi.a(background(Black.getInstance()));
            ansi.a(foreground(White.getInstance()));
        }
        for (int index = 0; index < terminal.getWidth() * terminal.getHeight(); index++)
            ansi.append(" ");
        ansi.cursor(0, 0);
        terminal.writer().print(ansi);
        terminal.flush();
    }

    /**
     * Moves the cursor to a specific position.
     *
     * @param delta Delta coordinates.
     */
    public static Ansi moveCursor(DeltaCoordinates delta) {
        return ansi().cursorMove(delta.getX(), delta.getY());
    }

    /**
     * Sets the foreground colour.
     *
     * @param colour Colour to set.
     */
    public static Ansi foreground(Colour colour) {
        Ansi ansi = new Ansi();
        ansi.fgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
        return ansi;
    }

    /**
     * Sets the background colour.
     *
     * @param colour Colour to set.
     */
    public static Ansi background(Colour colour) {
        Ansi ansi = new Ansi();
        ansi.bgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
        return ansi;
    }

    /**
     * Sets the font to bold or regular.
     *
     * @param enable Enable for bold, disable for regular.
     */
    public static Ansi bold(boolean enable) {
        Ansi ansi = new Ansi();
        if (enable)
            ansi.bold();
        else
            ansi.boldOff();
        ansi.saveCursorPosition();
        return ansi;
    }

    /**
     * Prints an error message on the top of the screen.
     *
     * @param terminal Terminal where to execute.
     * @param message  Message to print
     */
    public static void printError(Terminal terminal, String message) {
        Ansi ansi = new Ansi();
        ansi.cursor(0, 0);
        ansi.saveCursorPosition();
        ansi.bgRed();
        ansi.a(foreground(White.getInstance()));
        for (int index = 0; index < terminal.getWidth(); index++)
            ansi.append(" ");
        ansi.a(bold(true));
        ansi.cursor(0, (terminal.getWidth() - message.length() - 7) / 2);
        ansi.append("Error");
        ansi.a(bold(false));
        ansi.append(": ");
        ansi.append(message);
        ansi.cursor(0, 0);
        ansi.a(background(Black.getInstance()));
        terminal.writer().print(ansi);
        terminal.flush();
    }

    public static Ansi printTile(String[] title) {
        Ansi ansi = new Ansi();
        int length = title[0].length();
        for (String line : title) {
            ansi.a(line);
            ansi.cursorMove(-length, 1);
        }
        ansi.cursorUp(title.length);
        return ansi;
    }

    /**
     * Reads a line from the terminal with autocompletion.
     *
     * @param prefix      Prefix string to put before reading.
     * @param terminal    Terminal where to read and write.
     * @param completer   Completer for autocompletion.
     * @param suggestions Enables suggestions.
     * @param history     History where to write commands for future use
     * @return String to write.
     */
    public static String readLine(String prefix, Terminal terminal, Completer completer, boolean suggestions, History history) {
        return fixString(LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .option(LineReader.Option.CASE_INSENSITIVE, true)
                .option(LineReader.Option.AUTO_MENU, suggestions)
                .option(LineReader.Option.AUTO_LIST, suggestions)
                .history(history)
                .build().readLine(prefix));
    }

    /**
     * Removes extra spaces
     *
     * @param input Input string.
     * @return Cleaned string.
     */
    private static String fixString(String input) {
        Pattern start = Pattern.compile("^ +");
        Pattern middle = Pattern.compile(" +");
        Pattern end = Pattern.compile(" +$");
        input = start.matcher(input).replaceAll("");
        input = end.matcher(input).replaceAll("");
        input = middle.matcher(input).replaceAll(" ");
        return input;
    }

    /**
     * Gets the color of the student from HouseColor.
     *
     * @param houseColor The student's color.
     * @return The corresponding color.
     */
    static Colour getColourFrom(HouseColor houseColor) {
        return switch (houseColor) {
            case BLUE -> HouseBlue.getInstance();
            case FUCHSIA -> HouseFuchsia.getInstance();
            case GREEN -> HouseGreen.getInstance();
            case RED -> HouseRed.getInstance();
            case YELLOW -> HouseYellow.getInstance();
        };
    }
}
