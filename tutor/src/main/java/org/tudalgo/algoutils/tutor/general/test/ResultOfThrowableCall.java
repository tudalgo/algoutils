package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfThrowableCall}.
 *
 * @param <T> the exception type of the callable under test
 * @author Dustin Glaser
 */
public interface ResultOfThrowableCall<T extends Throwable> extends ResultWithThrowable<TestOfThrowableCall<T>, T> {

    /**
     * Asserts that the test was successful and throws an error if it was not.
     *
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @return the exception thrown by the callable under test
     * @throws Error if the object is not as expected
     */
    T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier);

    interface Builder<T extends Throwable> extends Result.Builder<Builder<T>, TestOfThrowableCall<T>, ResultOfThrowableCall<T>> {

        interface Factory<T extends Throwable> extends Result.Builder.Factory<Builder<T>, TestOfThrowableCall<T>, ResultOfThrowableCall<T>> {

        }
    }
}
