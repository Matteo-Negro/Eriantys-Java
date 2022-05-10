package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.cli.colours.Grey;
import it.polimi.ingsw.view.cli.colours.White;
import org.fusesource.jansi.Ansi;

import static it.polimi.ingsw.view.cli.Utilities.foreground;

public class ServerSettings {

    private ServerSettings() {
    }

    static void print(Ansi ansi) {

        foreground(ansi, Grey.getInstance());

        ansi.append("┌─────────────────────────────────────────────────────────────────┐");
        ansi.cursorMove(-67, 1);
        ansi.append("│ Server address (IP or domain):                                  │");
        ansi.cursorMove(-67, 1);
        ansi.append("│ Server port:                                                    │");
        ansi.cursorMove(-67, 1);
        ansi.append("└─────────────────────────────────────────────────────────────────┘");
        ansi.cursorMove(-34, -2);

        foreground(ansi, White.getInstance());
    }
}
