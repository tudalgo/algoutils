package org.tudalgo.algoutils.tutor.general.test.expected;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

public interface Expected {

    Function<String, String> BRACKET_FORMATTER = s -> String.format("<%s>", s);

    Object object();

    default String string(Stringifier stringifier) {
        return BRACKET_FORMATTER.apply(stringifier.stringify(object()));
    }
}
