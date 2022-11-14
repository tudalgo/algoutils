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

    static <T> Matcher<T> of(Predicate<T> matcher, Object object) {
        return new Matcher<T>() {
            @Override
            public <ST extends T> Match<ST> match(ST object) {
                return matcher.test(object) ? Match.positive(object) : Match.<ST>negative(object);
            }

            @Override
            public Object object() {
                return object;
            }
        };
    }

    static <T> Matcher<T> of(Predicate<T> matcher) {
        return of(matcher, null);
    }

    /**
     * <p>Returns a match for the given object.</p>
     *
     * @param object the object
     * @param <ST>   the type of the object to match
     * @return the match
     */
    <ST extends T> Match<ST> match(ST object);

    default Object object() {
        return null;
    }

    default <U extends T> Matcher<U> and(Matcher<U> other) {
        return new Matcher<U>() {
            @Override
            public <ST extends U> Match<ST> match(ST object) {
                Match<ST> match = Matcher.this.match(object);
                if (match.matched()) {
                    return other.match(object);
                }
                return match;
            }

            @Override
            public Object object() {
                return Matcher.this.object();
            }
        };
    }
}
