package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.Callable;

import java.util.function.Predicate;

/**
 * A test type testing the behavior of a callable that is expected to throw an exception.
 *
 * @param <T> the type of the exception to test
 * @author Dustin Glaser
 */
public interface TestOfThrowableCall<T extends Throwable> extends Test {

    /**
     * Tests if the callable throws an exception and if the exception is as expected.
     *
     * @param callable           the callable under test
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     */
    default T assertSuccessful(Callable callable, Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier) {
        return test(callable).assertSuccessful(context, preCommentSupplier);
    }

    /**
     * Tests if the callable throws an exception and if the exception is as expected.
     *
     * @param callable the callable under test
     * @return the result of the test
     */
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
