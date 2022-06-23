package it.polimi.ingsw.utilities;

/**
 * Contains all the possible state in which the game can be.
 */
public enum GameControllerState {

    /**
     * The assistant has jast been played.
     */
    ASSISTANT_PLAYED,

    /**
     * The player has to select a cloud.
     */
    CHOOSE_CLOUD,

    /**
     * The player has just ended his/her round.
     */
    END_TURN,

    /**
     * The player has to move mother nature.
     */
    MOVE_MOTHER_NATURE,

    /**
     * The player has to move the first student.
     */
    MOVE_STUDENT_1,

    /**
     * The player has to move the second student.
     */
    MOVE_STUDENT_2,

    /**
     * The player has to move the third student.
     */
    MOVE_STUDENT_3,

    /**
     * The player has to move the fourth student (only with three players).
     */
    MOVE_STUDENT_4,

    /**
     * The player has to play an assistant.
     */
    PLAY_ASSISTANT
}
