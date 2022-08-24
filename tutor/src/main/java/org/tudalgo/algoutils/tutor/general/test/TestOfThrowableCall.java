package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Predicate;

public interface TestOfThrowableCall<T extends Throwable> extends Test {

    default T assertSuccessful(Callable callable, Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier) {
        return test(callable).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfThrowableCall<T> test(Callable callable);

    interface Builder<T extends Throwable> {

        TestOfThrowableCall<T> build();

        Builder<T> evaluator(Class<T> throwable, Predicate<T> evaluator);

        Builder<T> expectation(Object expectation);

        interface Factory {

            <T extends Throwable> TestOfThrowableCall.Builder<T> builder();
        }
    }
}
