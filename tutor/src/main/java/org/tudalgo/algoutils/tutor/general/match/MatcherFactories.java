package org.tudalgo.algoutils.tutor.general.match;

public class MatcherFactories {

    private MatcherFactories() {
    }

    public interface StringMatcherFactory {

        <T extends Stringifiable> Matcher<T> matcher(String string);
    }
}
