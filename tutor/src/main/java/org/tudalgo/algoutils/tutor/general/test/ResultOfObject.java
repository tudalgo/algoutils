package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface ResultOfObject<T> extends ResultWithObject<T> {

    T assertSuccessful(Context context, Supplier<String> commentSupplier);

    default T assertSuccessful(Context context, String comment) {
        return assertSuccessful(context, () -> comment);
    }

    @Override
    TestOfObject<T> test();
}
