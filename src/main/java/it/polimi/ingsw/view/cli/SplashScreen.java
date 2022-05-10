package it.polimi.ingsw.view.cli;

import org.fusesource.jansi.Ansi;
import org.jline.terminal.Terminal;

import static org.fusesource.jansi.Ansi.ansi;

public class SplashScreen {

    private SplashScreen() {
    }

    public static void print(Terminal terminal) {
        terminal.writer().print(ansi().cursor((terminal.getHeight() - 21) / 2, (terminal.getWidth() - 67) / 2));
        terminal.writer().print(printTitle());
        terminal.flush();
    }

    private static Ansi printTitle() {
        Ansi ansi = new Ansi();
        Title.printTitle(ansi);
        ansi.cursorDown(14);
        Title.printSubtitle(ansi);
        ansi.cursorDown(4);
        ServerSettings.print(ansi);
        return ansi;
    }
}
