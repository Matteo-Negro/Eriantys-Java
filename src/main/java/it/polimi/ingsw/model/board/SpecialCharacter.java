package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.board.effects.*;

/**
 * The character card containing the corresponding effect
 *
 * @author Riccardo Milici
 */

public class SpecialCharacter {

    private final int id;
    private final int effectCost;
    private Effect assignedEffect;
    private boolean alreadyPayed;
    private boolean payedInRound;
    private boolean isActive;

    /**
     * Class constructor.
     * It creates an instance of the class containing the given specific effect object and identified by the given numeric id.
     *
     * @param id
     */
    public SpecialCharacter(int id) {
        this.id = id;
        isActive = false;
        alreadyPayed = false;
        payedInRound = false;

        assignedEffect = switch (id) {
            case 1 -> new MonkEffect();
            case 2 -> new FarmerEffect();
            case 3 -> new HeraldEffect();
            case 4 -> new MessengerEffect();
            case 5 -> new HerablistEffect();
            case 6 -> new CentaurEffect();
            case 7 -> new JesterEffect();
            case 8 -> new KnightEffect();
            case 9 -> new MushroomerEffect();
            case 10 -> new MinstrelEffect();
            case 11 -> new PrincessEffect();
            case 12 -> new ThiefEffect();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };

        effectCost = assignedEffect.getCost();
    }

    /**
     * Returns the identification number of the object.
     *
     * @return id attribute
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the actual cost of the effect assigned to the object: the effect's native cost if it has never been activated; the native cost increased by 1 if it has already been activated.
     *
     * @return effectCost or effectCost+1 attribute
     */
    public int getEffectCost() {

        if (alreadyPayed) return effectCost + 1;

        else return effectCost;

    }

    /**
     * Returns the instance of the specific effect assigned to the object.
     *
     * @return assignedEffect attribute
     */
    public Effect getEffect() {
        return assignedEffect;
    }

    /**
     * Activates the specific effect assigned to the object.
     */
    public void activateEffect() {
        alreadyPayed = true;
        payedInRound = true;
        isActive = true;
        getEffect().effect();
    }

    /**
     * Deactivates the specific effect assigned to the object.
     */
    public void cleanEffect() {
        isActive = false;
        assignedEffect.clean();
    }

    /**
     * Sets the "payedInRound" attribute to false.
     */
    public void changedRound() {
        payedInRound = false;
    }
}
