package org.tudalgo.algoutils.tutor.general.test;

public interface ResultOfThrowableCall<T extends Throwable> extends ResultWithThrowable<T> {

    @Override
    TestOfThrowableCall<T> test();
}
