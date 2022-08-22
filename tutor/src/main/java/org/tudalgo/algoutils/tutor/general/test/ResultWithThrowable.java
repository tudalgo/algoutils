package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithThrowable<RT extends Result<RT>, T extends Throwable> extends Result<RT> {

    T actualThrowable();
}
