package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Supplier;

public interface TestOfCall extends Test {

    ResultOfCall test(Callable callable, Context context, Supplier<String> commentSupplier);
}
