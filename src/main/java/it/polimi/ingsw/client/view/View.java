package it.polimi.ingsw.client.view;

import it.polimi.ingsw.utilities.Pair;

public interface View {

    void showError(String message);

    void showInfo(Pair<String, String> info);

}
