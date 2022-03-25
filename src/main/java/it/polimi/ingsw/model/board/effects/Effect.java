package it.polimi.ingsw.model.board.effects;

public interface Effect {

    public int getId();
    public int getCost();
    public void effect();
    public void clean();

}
