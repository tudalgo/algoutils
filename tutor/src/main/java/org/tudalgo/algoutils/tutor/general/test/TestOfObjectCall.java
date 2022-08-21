package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.util.function.Supplier;

public interface TestOfObjectCall<T> extends Test {

    default T assertSuccessful(ObjectCallable<T> callable, Context context, Supplier<String> commentSupplier) {
        return test(callable).assertSuccessful(context, commentSupplier);
    }

    default T assertSuccessful(ObjectCallable<T> callable, Context context, String comment) {
        return test(callable).assertSuccessful(context, comment);
    }

    ResultOfObjectCall<T> test(ObjectCallable<T> callable);
}
