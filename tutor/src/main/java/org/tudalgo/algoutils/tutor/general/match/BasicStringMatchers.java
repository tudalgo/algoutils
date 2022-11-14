package org.tudalgo.algoutils.tutor.general.match;

/**
 * <p>A collection of basic matchers for stringifiable objects.</p>
 */
public final class BasicStringMatchers {

    private BasicStringMatchers() {
    }

    public static <T extends Stringifiable> Matcher<T> identical(String string) {
        return Matcher.of(stringifiable -> stringifiable.string().equals(string), string);
    }
}
