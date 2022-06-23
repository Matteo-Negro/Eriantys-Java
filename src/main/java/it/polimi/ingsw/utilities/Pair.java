package it.polimi.ingsw.utilities;

/**
 * A utility record for storing a couple of information into the same variable.
 *
 * @param first  The first element to store.
 * @param second The second elemento to store.
 * @param <E>    The type of the first element (it can be any).
 * @param <T>    The type of the second element (it can be any).
 */
public record Pair<E, T>(E first, T second) {
}
