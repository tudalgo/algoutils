package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

public interface TestOfObjectCall<T> extends Test {

    ResultOfObjectCall<T> test(ObjectCallable<T> callable);
}
