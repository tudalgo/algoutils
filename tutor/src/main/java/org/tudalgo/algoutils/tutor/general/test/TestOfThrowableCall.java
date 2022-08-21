package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Supplier;

public interface TestOfThrowableCall<T extends Throwable> extends Test {

    default T assertSuccessful(Callable callable, Context context, Supplier<String> commentSupplier) {
        return test(callable).assertSuccessful(context, commentSupplier);
    }

    default T assertSuccessful(Callable callable, Context context, String comment) {
        return test(callable).assertSuccessful(context, comment);
    }

    ResultOfThrowableCall<T> test(Callable callable);
}
