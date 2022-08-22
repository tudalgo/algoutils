package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

public interface TestOfCall extends Test {

    default void assertSuccessful(Callable callable, Context context, PreCommentSupplier<ResultOfCall> preCommentSupplier) {
        test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfCall test(Callable callable);
}
