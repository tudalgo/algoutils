package org.tudalgo.algoutils.tutor.general.test.actual;

import org.tudalgo.algoutils.tutor.general.stringify.Stringifier;

import java.util.function.Function;

import static java.lang.String.format;

public interface Actual {

    Function<String, String> BRACKET_FORMATTER = s -> format("<%s>", s);

    Object object();

    default String string(Stringifier stringifier) {
        return BRACKET_FORMATTER.apply(stringifier.stringify(object()));
    }

    boolean successful(); // TODO remove
}
