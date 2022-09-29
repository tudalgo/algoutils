package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing a supplier of a pre-comment for a test result.
 *
 * @param <TR> the type of test result
 * @author Dustin Glaser
 */
@FunctionalInterface
public interface PreCommentSupplier<TR extends Result<?, ?>> {

    /**
     * Returns the pre-comment for the given test result.
     *
     * @param result the test result
     * @return the pre-comment for the given test result
     */
    String getPreComment(TR result);
}
