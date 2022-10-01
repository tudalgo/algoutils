package org.tudalgo.algoutils.tutor.general.test;

/**
 * <p>A type representing a factory for creating result comments.</p>
 *
 * @param <RT> the type of supported results
 * @author Dustin Glaser
 */
@FunctionalInterface
public interface CommentFactory<RT extends Result<?, ?, ?, ?>> {

    /**
     * Returns the comment for the given test result, context and pre-comment supplier.
     *
     * @param result             the test result
     * @param context            the context of the test
     * @param preCommentSupplier the supplier for the pre-comment
     * @param <RTS>              the type of the test result
     * @return the comment
     */
    <RTS extends RT> String comment(RTS result, Context context, PreCommentSupplier<? super RTS> preCommentSupplier);
}
