package org.tudalgo.algoutils.tutor.general.assertions;

/**
 * <p>A type representing a function for creating comments for a given result.</p>
 *
 * @param <TR> the type of result
 * @author Dustin Glaser
 */
@FunctionalInterface
public interface PreCommentSupplier<TR extends Result<?, ?, ?, ?>> {

    /**
     * Returns the pre-comment for the given result.
     *
     * @param result the result
     * @return the pre-comment for the given result
     */
    String getPreComment(TR result);
}
