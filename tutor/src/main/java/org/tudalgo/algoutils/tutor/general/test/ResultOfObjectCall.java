package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfObjectCall<T> extends ResultWithObject<T>, ResultWithThrowable<Throwable> {

    @Override
    TestOfObjectCall<T> test();
}
