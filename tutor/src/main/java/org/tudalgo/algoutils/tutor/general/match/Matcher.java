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

    /**
     * <p>returns a {@link Predicate} that returns {@code true} if the given object is matched by this matcher.</p>
     *
     * @param <U> the type of the object to match
     * @return the predicate
     */
    default <U extends T> Predicate<U> predicate() {
        return (U object) -> match(object).matched();
    }

    /**
     * <p>returns a Matcher that matches the logical negation of this matcher.</p>
     *
     * @return the negated matcher
     */
    default Matcher<T> negate() {
        return Matcher.of(Predicate.not(this.predicate()), this.object());
    }

    /**
     * <p>Returns a new matcher that logically combines the current matcher with the given matcher using the logical {@code and} operator.</p>
     *
     * @param other the other matcher
     * @return the new matcher
     */
    default Matcher<T> and(Matcher<? super T> other) {
        return Matcher.of(this.predicate().and(other.predicate()), this.object() == null ? other.object() : this.object());
    }

    /**
     * <p>Returns a new matcher that logically combines the current matcher with the given matcher using the logical {@code or} operator.</p>
     *
     * @param other the other matcher
     * @return the new matcher
     */
    default Matcher<T> or(Matcher<T> other) {
        return Matcher.of(this.predicate().or(other.predicate()), this.object());
    }
}
