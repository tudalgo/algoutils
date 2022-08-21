package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Supplier;

public interface TestOfThrowableCall<T extends Throwable> extends Test {

    ResultOfThrowableCall<T> test(Callable callable, Context context, Supplier<String> commentSupplier);
}
