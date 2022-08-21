package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface TestOfObject<T> extends Test {

    default T assertSuccessful(T object, Context context, Supplier<String> commentSupplier) {
        return test(object).assertSuccessful(context, commentSupplier);
    }

    default T assertSuccessful(T object, Context context, String comment) {
        return test(object).assertSuccessful(context, comment);
    }

    ResultOfObject<T> test(T object);
}
