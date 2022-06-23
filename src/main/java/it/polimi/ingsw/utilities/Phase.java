package it.polimi.ingsw.utilities;

/**
 * Contains the two possible main game states in which the client can be.
 */
public enum Phase {

    /**
     * Planning phase: every player selects the assistant.
     */
    PLANNING,

    /**
     * Action phase: every player has his/her turn for moving students, mother nature, playing special character and refill the entrance with a cloud.
     */
    ACTION
}
