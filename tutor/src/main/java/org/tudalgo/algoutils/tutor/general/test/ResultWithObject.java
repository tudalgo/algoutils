package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithObject<TT extends Test, RT extends Result<TT, RT>, T> extends Result<TT, RT> {

    T actualObject();
}
