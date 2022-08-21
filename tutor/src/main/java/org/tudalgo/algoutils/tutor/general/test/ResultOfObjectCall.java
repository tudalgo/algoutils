package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface ResultOfObjectCall<T> extends ResultWithObject<T>, ResultWithThrowable<Throwable> {

    T assertSuccessful(Context context, Supplier<String> commentSupplier);

    default T assertSuccessful(Context context, String comment) {
        return assertSuccessful(context, () -> comment);
    }

    @Override
    TestOfObjectCall<T> test();
}
