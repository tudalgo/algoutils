package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithThrowable<T extends Test, E extends Throwable> extends Result<T> {

    E actualThrowable();
}
