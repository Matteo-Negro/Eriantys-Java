package it.polimi.ingsw.model.board.effects;

public interface Effect {

    int getId();

    void effect();

    void clean();

    int getCost();
}
