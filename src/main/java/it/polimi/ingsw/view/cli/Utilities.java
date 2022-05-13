package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.*;
import org.fusesource.jansi.Ansi;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;

import java.util.regex.Pattern;

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
            background(ansi, Black.getInstance());
            foreground(ansi, White.getInstance());
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
     * @param ansi  Ansi stream where to write.
     * @param delta Delta coordinates.
     */
    public static void moveCursor(Ansi ansi, DeltaCoordinates delta) {
        ansi.cursorMove(delta.getX(), delta.getY());
    }

    /**
     * Sets the foreground colour.
     *
     * @param ansi   Ansi stream where to write.
     * @param colour Colour to set.
     */
    public static void foreground(Ansi ansi, Colour colour) {
        ansi.fgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
    }

    /**
     * Sets the background colour.
     *
     * @param ansi   Ansi stream where to write.
     * @param colour Colour to set.
     */
    public static void background(Ansi ansi, Colour colour) {
        ansi.bgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
    }

    /**
     * Sets the font to bold or regular.
     *
     * @param ansi   Ansi stream where to write.
     * @param enable Enable for bold, disable for regular.
     */
    public static void bold(Ansi ansi, boolean enable) {
        if (enable)
            ansi.bold();
        else
            ansi.boldOff();
        ansi.saveCursorPosition();
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
        foreground(ansi, White.getInstance());
        for (int index = 0; index < terminal.getWidth(); index++)
            ansi.append(" ");
        bold(ansi, true);
        ansi.cursor(0, (terminal.getWidth() - message.length() - 7) / 2);
        ansi.append("Error");
        bold(ansi, false);
        ansi.append(": ");
        ansi.append(message);
        ansi.cursor(0, 0);
        background(ansi, Black.getInstance());
        terminal.writer().print(ansi);
        terminal.flush();
    }

    public static String readLine(Terminal terminal, Completer completer, boolean suggestions, String prefix) {
        return fixString(LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .option(LineReader.Option.CASE_INSENSITIVE, suggestions)
                .option(LineReader.Option.AUTO_MENU, !suggestions)
                .option(LineReader.Option.AUTO_LIST, !suggestions)
                .build().readLine(prefix));
    }

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
