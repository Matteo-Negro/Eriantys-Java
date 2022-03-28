package it.polimi.ingsw.model.board.effects;

public interface Effect {

    public int getId();
    public void effect();
    public void clean();
    public int getCost();
}
