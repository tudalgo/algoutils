package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Supplier;

public interface TestOfCall extends Test {

    default void assertSuccessful(Callable callable, Context context, Supplier<String> commentSupplier) {
        test(callable).assertSuccessful(context, commentSupplier);
    }

    default void assertSuccessful(Callable callable, Context context, String comment) {
        test(callable).assertSuccessful(context, comment);
    }

    ResultOfCall test(Callable callable);
}
