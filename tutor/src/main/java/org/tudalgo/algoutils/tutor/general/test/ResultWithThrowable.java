package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithThrowable<TT extends Test, RT extends Result<TT, RT>, T extends Throwable> extends Result<TT, RT> {

    T actualThrowable();
}
