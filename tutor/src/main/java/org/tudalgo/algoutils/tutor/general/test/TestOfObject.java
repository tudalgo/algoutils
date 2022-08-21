package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Supplier;

public interface TestOfObject<T> extends Test {

    ResultOfObject<T> test(T object, Context context, Supplier<String> commentSupplier);
}
