package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithObject<RT extends Result<RT>, T> extends Result<RT> {

    T actualObject();
}
