package org.tudalgo.algoutils.tutor.general.match;

import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>A matcher is a function mapping objects to positive/negative matches.</p>
 *
 * @param <T> the type of object to match
 */
@FunctionalInterface
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

    /**
     * <p>Creates a Matcher with the given Characteristic</p>
     *
     * @param matcher        the matcher predicate
     * @param characteristic the characteristic of the matcher
     * @param <T>            the type of object to match
     * @return the matcher
     */
    static <T> Matcher<T> of(Predicate<T> matcher, String characteristic) {
        return new Matcher<T>() {
            @Override
            public <ST extends T> Match<ST> match(ST object) {
                return matcher.test(object) ? Match.positive(object) : Match.<ST>negative(object);
            }

            @Override
            public String characteristic() {
                return characteristic;
            }
        };
    }

    /**
     * <p>Creates a Matcher with the given Characteristic</p>
     *
     * @param matcher the matcher predicate
     * @param object  the characteristic of the matcher. Will be stringified using the default
     *                {@link org.tudalgo.algoutils.tutor.general.stringify.Stringifier}
     * @param <T>     the type of object to match
     * @return the matcher
     */
    static <T> Matcher<T> of(Predicate<T> matcher, Object object) {
        return of(matcher, BasicEnvironment.getInstance().getStringifier().stringify(object));
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

    /**
     * The characteristic of this matcher. This is used to describe the matcher in error messages.
     *
     * @return the characteristic of this matcher
     */
    default String characteristic() {
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
        return Matcher.of(
            Predicate.not(this.predicate()),
            String.format("not (%s)", this.characteristic())
        );
    }

    /**
     * <p>Returns a new matcher that logically combines the current matcher with the given matcher using the logical
     * {@code and} operator.</p>
     *
     * @param other the other matcher
     * @return the new matcher
     */
    default Matcher<T> and(Matcher<? super T> other) {
        return Matcher.of(
            this.predicate().and(other.predicate()),
            String.format("(%s) and (%s)", this.characteristic(), other.characteristic())
        );
    }

    /**
     * <p>Returns a new matcher that logically combines the current matcher with the given matcher using the logical
     * {@code or} operator.</p>
     *
     * @param other the other matcher
     * @return the new matcher
     */
    default Matcher<T> or(Matcher<T> other) {
        return Matcher.of(
            this.predicate().or(other.predicate()),
            String.format("(%s) or (%s)", this.characteristic(), other.characteristic())
        );
    }

    /**
     * <p>Returns a new matcher that maps the given mapper before matching with this matcher.</p>
     *
     * @param mapper the mapper
     * @param <U>    the type of the object to match
     * @return the new matcher
     */
    default <U> Matcher<U> map(Function<U, T> mapper) {
        return Matcher.of(
            e -> this.match(mapper.apply(e)).matched(),
            this.characteristic()
        );
    }
}
