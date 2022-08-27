package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfCall}.
 *
 * @author Dustin Glaser
 */
public interface ResultOfCall extends ResultWithThrowable<TestOfCall, Throwable> {

    /**
     * Asserts that the test was successful and throws an error if it was not.
     *
     * @param context            the context of the callable under test
     * @param preCommentSupplier a supplier for a pre-comment to be added to the comment
     * @throws Error if the object is not as expected
     */
    void assertSuccessful(Context context, PreCommentSupplier<? super ResultOfCall> preCommentSupplier) throws Error;
}
