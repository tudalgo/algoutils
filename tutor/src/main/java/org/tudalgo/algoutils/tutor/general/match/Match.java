package org.tudalgo.algoutils.tutor.general.match;

/**
 * <p>A match represents a match of an {@linkplain Matcher object matcher}.</p>
 *
 * @param <T> the type of the matched object
 */
public interface Match<T> extends Comparable<Match<T>> {

    /**
     * <p>Returns a simple match with the given object and binary state.</p>
     *
     * @param object   the object
     * @param positive if the match is positive
     * @param <T>      the type of the object
     * @return the match
     */
    static <T> Match<T> match(T object, boolean positive) {
        return new Match<T>() {
            @Override
            public boolean matched() {
                return positive;
            }

            @Override
            public T object() {
                return object;
            }
        };
    }

    /**
     * <p>Returns a negative match with the given object.</p>
     *
     * @param object the object
     * @param <T>    the type of the object
     * @return the match
     */
    static <T> Match<T> negative(T object) {
        return match(object, false);
    }

    /**
     * <p>Returns a positive match with the given object.</p>
     *
     * @param object the object
     * @param <T>    the type of the object
     * @return the match
     */
    static <T> Match<T> positive(T object) {
        return match(object, true);
    }

    @Override
    default int compareTo(Match<T> other) {
        if (matched() == other.matched()) {
            return 0;
        }
        return matched() ? -1 : 1;
    }

    /**
     * <p>Returns if this match is a positive match.</p>
     *
     * @return if this match is a positive match
     */
    boolean matched();

    /**
     * <p>Returns if this match is a negative match.</p>
     *
     * @return if this match is a negative match
     */
    default boolean negative() {
        return !matched();
    }

    /**
     * <p>Returns the matched object.</p>
     *
     * @return the matched object
     */
    T object();
}
