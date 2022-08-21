package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface ResultOfCall extends ResultWithThrowable<Throwable> {

    void assertSuccessful(Context context, Supplier<String> commentSupplier);

    default void assertSuccessful(Context context, String comment) {
        assertSuccessful(context, () -> comment);
    }

    @Override
    TestOfCall test();
}
