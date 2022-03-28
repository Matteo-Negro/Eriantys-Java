package it.polimi.ingsw.model.board.effects;

public class HerablistEffect implements Effect {

    private int availableBans;

    public HerablistEffect() {
        availableBans = 5;
    }

    @Override
    public int getId() {
        return 5;
    }

    @Override
    public void effect() {
        takeBan();
    }

    @Override
    public void clean() {

    }

    @Override
    public int getCost() {
        return 2;
    }

    private int getAvailableBans() {
        return availableBans;
    }

    private void takeBan() {
        availableBans--;
    }

    private void putBan() {
        availableBans++;
    }
}
