package org.tudalgo.algoutils.tutor.general.test.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

/**
 * <p>A type representing the expected behavior in a test.</p>
 *
 * @author Dustin Glaser
 */
public interface Expected {

    /**
     * A function enclosing a string with brackets.
     */
    Function<String, String> BRACKET_FORMATTER = s -> String.format("<%s>", s);

    /**
     * <p>Returns an object representing the expected behavior of the test.</p>
     *
     * @return the object
     */
    Object behavior();

    /**
     * <p>Returns a string representation of the expected behavior using the given stringifier to make the behavior human-readable.</p>
     *
     * @param stringifier the stringifier
     * @return the string
     */
    default String string(Stringifier stringifier) {
        return BRACKET_FORMATTER.apply(stringifier.stringify(behavior()));
    }
}
