package org.tudalgo.algoutils.tutor.general.match;

import java.util.function.Predicate;

/**
 * <p>A matcher is a function mapping objects to positive/negative matches.</p>
 *
 * @param <T> the type of object to match
 */
public interface Matcher<T> {

    /**
     * <p>Returns an always positive-matching matcher.</p>
     *
     * @param <T> the type of object to match
     * @return the matcher
     */
    static <T> Matcher<T> always() {
        return Match::positive;
    }

    /**
     * <p>Returns an always negative-matching matcher.</p>
     *
     * @param <T> the type of object to match
     * @return the matcher
     */
    static <T> Matcher<T> never() {
        return Match::negative;
    }

    static <T> Matcher<T> of(Predicate<T> matcher) {
        return new Matcher<T>() {
            @Override
            public <ST extends T> Match<ST> match(ST object) {
                return matcher.test(object) ? Match.positive(object) : Match.<ST>negative(object);
            }
        };
    }

    /**
     * <p>Returns a match for the given object.</p>
     *
     * @param object the object
     * @param <ST>   the type of the object to match
     * @return the match
     */
    <ST extends T> Match<ST> match(ST object);
}
