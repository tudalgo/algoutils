package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Predicate;

public interface TestOfObject<T> extends Test {

    default T assertSuccessful(T object, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return test(object).assertSuccessful(context, preCommentSupplier);
    }

    ResultOfObject<T> test(T object);

    interface Builder<T> {

        TestOfObject<T> build();

        Builder<T> evaluator(Predicate<T> evaluator);

        Builder<T> expectation(Object expectation);

        interface Factory {

            <T> TestOfObject.Builder<T> builder();
        }
    }
}
