package org.tudalgo.algoutils.tutor.general.test;

public interface Result<TT extends Test> {

    Object behaviorActual();

    default Object behaviorExpected() {
        return test().expected();
    }

    boolean successful();

    TT test();
}
