package org.tudalgo.algoutils.assertions.options;

import org.tudalgo.algoutils.assertions.AssertionResult;
import org.tudalgo.algoutils.assertions.ErrorBuilder;

/**
 * This functional interface represents an option that can be applied to an {@link AssertionResult} and its associated {@link ErrorBuilder}.
 * It allows modifying the assertion result after a {@link org.tudalgo.algoutils.assertions.verification.AssertionVerification} was applied.
 * This can mean that the values of the result itself are changed or, if the assertion is unsuccessful, how the error is displayed.
 *
 * @param <E> the type of the expected value
 * @param <R> the type of the actual value returned by the verification
 * @see BasicOptions
 * @see SpecialOptions
 */
public interface AssertionOption<E, R> {

    /**
     * Applies the action of this option to the given result and returns a transformed result (possibly the same object).
     *
     * @param result the assertion result
     * @return the assertion result to use next (possibly modified or same as input)
     */
    AssertionResult<E, R> apply(AssertionResult<E, R> result);

    /**
     * Helper method to create an "inline" option, meaning it modifies the result in-place and returns the same instance.
     *
     * @param result   the assertion result
     * @param runnable the action that gets applied to the result
     * @return a potentially modified result, but always the same instance it was called with
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    static <E, R> AssertionResult<E, R> inline(AssertionResult<E, R> result, Runnable runnable) {
        runnable.run();
        return result;
    }
}
