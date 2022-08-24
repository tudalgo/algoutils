package org.tudalgo.algoutils.tutor.general.test;

public interface Result<T extends Test> {

    Object behaviorActual();

    default Object behaviorExpected() {
        return test().expectation();
    }

    boolean successful();

    T test();
}
