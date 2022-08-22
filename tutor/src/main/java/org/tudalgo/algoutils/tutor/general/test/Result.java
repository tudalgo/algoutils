package org.tudalgo.algoutils.tutor.general.test;

public interface Result {

    Object behaviorActual();

    default Object behaviorExpected() {
        return test().expected();
    }

    boolean successful();

    Test test();
}
