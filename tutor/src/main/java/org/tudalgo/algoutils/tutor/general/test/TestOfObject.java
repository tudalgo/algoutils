package org.tudalgo.algoutils.tutor.general.test;

public interface TestOfObject<T> extends Test {

    ResultOfObject<T> test(T object);
}
