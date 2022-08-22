package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface ResultOfThrowableCall<T extends Throwable> extends ResultWithThrowable<ResultOfThrowableCall<T>, T> {

    T assertSuccessful(Context context, Supplier<String> commentSupplier);

    default T assertSuccessful(Context context, String comment) {
        return assertSuccessful(context, () -> comment);
    }

    @Override
    TestOfThrowableCall<T> test();
}
