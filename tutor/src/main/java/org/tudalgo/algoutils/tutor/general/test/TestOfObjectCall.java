package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

public interface TestOfObjectCall<T> extends Test {

    default T assertSuccessful(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfObjectCall<T> test(ObjectCallable<T> callable);
}
