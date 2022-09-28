package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfObjectCall}.
 *
 * @param <T> the return type of the callable under test
 * @author Dustin Glaser
 */
public interface ResultOfObjectCall<T> extends ResultWithObject<TestOfObjectCall<T>, T>, ResultWithThrowable<TestOfObjectCall<T>, Throwable> {

    /**
     * Asserts that the test was successful and throws an error if it was not.
     *
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @return the object returned by the callable under test
     * @throws Error if the object is not as expected
     */
    T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier);

    @Override
    default Object behaviorActual() {
        return actualThrowable() != null ? actualThrowable() : actualObject();
    }

    interface Builder<T> extends Result.Builder<Builder<T>, TestOfObjectCall<T>, ResultOfObjectCall<T>> {

        interface Factory<T> extends Result.Builder.Factory<Builder<T>, TestOfObjectCall<T>, ResultOfObjectCall<T>> {

        }
    }
}
