package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

public interface TestOfThrowableCall<T extends Throwable> extends Test {

    default T assertSuccessful(Callable callable, Context context, PreCommentSupplier<ResultOfThrowableCall<T>> preCommentSupplier) {
        return test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfThrowableCall<T> test(Callable callable);
}
