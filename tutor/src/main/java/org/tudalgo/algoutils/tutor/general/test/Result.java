package org.tudalgo.algoutils.tutor.general.test;

public interface Result {

    boolean successful();

    Test test();

    Object actual();

    default Object expectation() {
        return test().expectation();
    }
}
