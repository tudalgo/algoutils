package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithThrowable<T extends Throwable> extends Result {

    T actualThrowable();
}
