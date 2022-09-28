package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfObject}.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface ResultOfObject<T> extends ResultWithObject<TestOfObject<T>, T> {

    /**
     * Asserts that the test was successful and throws an error if it was not.
     *
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @return the object under test
     * @throws Error if the object is not as expected
     */
    T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier);

    interface Builder<T> extends Result.Builder<Builder<T>, TestOfObject<T>, ResultOfObject<T>> {

        interface Factory<T> extends Result.Builder.Factory<Builder<T>, TestOfObject<T>, ResultOfObject<T>> {

        }
    }
}
