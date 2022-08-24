package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.test.Test;

public abstract class BasicTest implements Test {

    private final Object expectation;

    public BasicTest(Object expectation) {
        this.expectation = expectation;
    }

    public Object expectation() {
        return expectation;
    }
}
