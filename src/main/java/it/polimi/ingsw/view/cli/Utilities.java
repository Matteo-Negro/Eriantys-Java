package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.view.cli.colours.*;
import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static org.fusesource.jansi.Ansi.ansi;

public class Utilities {

    private Utilities() {
    }

    public static void clearScreen(Terminal terminal) {
        Ansi ansi = new Ansi();
        background(ansi, new Black());
        foreground(ansi, new White());
        for (int x = 0; x < terminal.getWidth(); x++)
            for (int y = 0; y < terminal.getHeight(); y++)
                ansi.append(" ");
        terminal.writer().print(ansi);
        terminal.writer().print(ansi().cursor(0, 0));
        terminal.flush();
    }

    public static void newLine(Ansi ansi, DeltaCoordinates delta) {
        ansi.cursorMove(delta.getX(), delta.getY());
    }

    public static void foreground(Ansi ansi, Colour colour) {
        ansi.fgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
    }

    public static void background(Ansi ansi, Colour colour) {
        ansi.bgRgb(colour.getR(), colour.getG(), colour.getB());
        ansi.saveCursorPosition();
    }

    static Colour getColourFrom(HouseColor houseColor) {
        return switch (houseColor) {
            case BLUE -> new HouseBlue();
            case FUCHSIA -> new HouseFuchsia();
            case GREEN -> new HouseGreen();
            case RED -> new HouseRed();
            case YELLOW -> new HouseYellow();
        };
    }
}
