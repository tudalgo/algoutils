package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithObject<TT extends Test, RT extends Result<TT>, T> extends Result<TT> {

    T actualObject();
}
