package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.util.function.Predicate;

public interface TestOfObjectCall<T> extends Test {

    default T assertSuccessful(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfObjectCall<T> test(ObjectCallable<T> callable);

    interface Builder<T> {

        TestOfObjectCall<T> build();

        Builder<T> evaluator(Predicate<T> evaluator);

        Builder<T> expectation(Object expectation);

        interface Factory {

            <T> TestOfObjectCall.Builder<T> builder();
        }
    }
}
