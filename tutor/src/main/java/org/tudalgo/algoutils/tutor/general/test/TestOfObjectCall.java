package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.util.function.Supplier;

public interface TestOfObjectCall<T> extends Test {

    ResultOfObjectCall<T> test(ObjectCallable<T> callable, Context context, Supplier<String> commentSupplier);
}
