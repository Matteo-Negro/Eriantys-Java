package it.polimi.ingsw.client.view;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.gui.updates.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an observer for the game model.
 *
 * @author Riccardo Milici.
 */
public class GameModelObserver {
    List<Observer> observers;

    /**
     * Default class constructor.
     *
     * @param controller The client controller instance which is currently running.
     */
    public GameModelObserver(ClientController controller) {
        controller.addModelObserver(this);
        this.observers = new ArrayList<>();
    }

    /**
     * Attaches a new object interested in game model updates.
     *
     * @param newObserver The new observer object.
     */
    public void attachObserver(Observer newObserver) {
        observers.add(newObserver);
    }

    /**
     * Notifies all observer objects about the game model changes.
     */
    public void notifyUpdate() {
        for (Observer observer : this.observers)
            observer.notifyUpdate();
    }
}
