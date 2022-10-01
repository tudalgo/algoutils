package org.tudalgo.algoutils.tutor.general.assertions.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

import static java.lang.String.format;

/**
 * <p>A type representing the actual behavior in a test.</p>
 *
 * @author Dustin Glaser
 */
public interface Actual {

    /**
     * A function enclosing a string with brackets.
     */
    Function<String, String> BRACKET_FORMATTER = s -> format("<%s>", s);

    /**
     * <p>Returns an object representing the behavior of the test.</p>
     *
     * @return the object
     */
    Object behavior();

    /**
     * <p>Returns a string representation of the behavior under test using the given stringifier to make the behavior human-readable.</p>
     *
     * @param stringifier the stringifier
     * @return the string
     */
    default String string(Stringifier stringifier) {
        return BRACKET_FORMATTER.apply(stringifier.stringify(behavior()));
    }
}
