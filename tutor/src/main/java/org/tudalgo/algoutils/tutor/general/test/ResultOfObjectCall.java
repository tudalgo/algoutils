package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface ResultOfObjectCall<T> extends ResultWithObject<ResultOfObjectCall<T>, T>, ResultWithThrowable<ResultOfObjectCall<T>, Throwable> {

    T assertSuccessful(Context context, Supplier<String> commentSupplier);

    default T assertSuccessful(Context context, String comment) {
        return assertSuccessful(context, () -> comment);
    }

    @Override
    default Object behaviorActual() {
        return actualThrowable() != null ? actualThrowable() : actualObject();
    }

    @Override
    TestOfObjectCall<T> test();
}
