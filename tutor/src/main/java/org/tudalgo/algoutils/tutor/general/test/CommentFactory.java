package org.tudalgo.algoutils.tutor.general.test;

/**
 * A factory type creating comments for test results using a given context and pre-comment supplier.
 *
 * @param <T> the type of supported tests
 * @author Dustin Glaser
 */
public interface CommentFactory<T extends Result<?, ?, ?, ?>> {

    /**
     * Returns a comment for the given test result, context and pre-comment supplier.
     *
     * @param result             the test result
     * @param context            the context of the test
     * @param preCommentSupplier a supplier for a pre-comment
     * @param <TS>               the type of the test result
     * @return the comment
     */
    <TS extends T> String comment(TS result, Context context, PreCommentSupplier<? super TS> preCommentSupplier);
}
