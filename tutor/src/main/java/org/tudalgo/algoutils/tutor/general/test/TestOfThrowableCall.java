package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

public interface TestOfThrowableCall<T extends Throwable> extends Test {

    ResultOfThrowableCall<T> test(Callable callable);
}
