package org.tudalgo.algoutils.tutor.general.test;

public interface ResultWithObject<T extends Test, O> extends Result<T> {

    O actualObject();
}
