package org.tudalgo.algoutils.assertions.verification;

import org.tudalgo.algoutils.assertions.ActualWrapper;
import org.tudalgo.algoutils.assertions.AssertionResult;

/**
 * This functional interface represents the verification of an assertion.
 * Its functional method {@link #verify(ActualWrapper)} takes an actual value and
 * returns an {@link AssertionResult} containing the result of the verification.
 *
 * @param <E> the type of the expected value
 * @param <A> the type of the actual value passed to the verification
 * @param <R> the type of the actual value returned by the verification
 * @see BasicVerifications
 * @see ReflectionVerifications
 * @see MiscVerifications
 */
@FunctionalInterface
public interface AssertionVerification<E, A, R> {

    /**
     * Verifies the given actual value and returns an {@link AssertionResult} containing the result of the verification.
     *
     * <p>
     *     The type of the actual value in the result may differ from the one the verification was invoked with.
     *     This is deliberate to decouple the result from the verification, allowing for more meaningful actual values and error messages.
     *     An example of this would be to check the runtime type of the actual value.
     *     In that case, the verification needs a class to compare against, so the expected value would be a class object
     *     but the actual value would be a regular object.
     *     Having a different type for the actual value in the result allows the verification to store the class object
     *     for the actual value's type, which makes much more sense in context.
     * </p>
     *
     * @param actual the actual value to be verified, wrapped in an {@link ActualWrapper}
     * @return the result of this verification as an {@link AssertionResult}
     */
    AssertionResult<E, R> verify(ActualWrapper<A> actual);

    /**
     * Inverts the success of the given verification.
     * The expected and actual values are kept the same,
     * only the return value of {@link AssertionResult#successful()} is the inverse of what it was.
     *
     * @param verification the verification to be inverted
     * @return a new verification that returns the inverse success value of the given verification
     * @param <E> the type of the expected value
     * @param <A> the type of the actual value passed to the verification
     * @param <R> the type of the actual value returned by the verification
     */
    static <E, A, R> AssertionVerification<E, A, R> not(AssertionVerification<E, A, R> verification) {
        return actual -> {
            AssertionResult<E, R> result = verification.verify(actual);
            return AssertionResult.of(!result.successful(),
                result.getExpected().orElse(null),
                result.getActual().orElse(null),
                result.getErrorBuilder().getMessage());
        };
    }

    /**
     * Returns a new verification that represents the logical OR of this verification and another verification.
     *
     * <p>
     *     Note that the result of this verification is evaluated before the given one.
     *     If this verification was successful, the method short-circuits and
     *     returns the result of this verification without evaluating the other one.
     *     Otherwise, both are evaluated and only the result of the given verification is returned.
     * </p>
     *
     * @param verification the verification to be combined with this verification
     * @return a new verification that applies a logical OR on the result of this verification and the given one
     */
    default AssertionVerification<E, A, R> or(AssertionVerification<E, A, R> verification) {
        return actual -> {
            AssertionResult<E, R> result = this.verify(actual);
            if (result.successful()) {
                return result;
            }
            return verification.verify(actual);
        };
    }

    /**
     * Returns a new verification that represents the logical AND of this verification and another verification.
     *
     * <p>
     *     Note that the result of this verification is evaluated before the given one.
     *     If this verification was not successful, the method short-circuits and
     *     returns the result of this verification without evaluating the other one.
     *     Otherwise, both are evaluated and only the result of the given verification is returned.
     * </p>
     *
     * @param verification the verification to be combined with this verification
     * @return a new verification that applies a logical AND on the result of this verification and the given one
     */
    default AssertionVerification<E, A, R> and(AssertionVerification<E, A, R> verification) {
        return actual -> {
            AssertionResult<E, R> result = this.verify(actual);
            if (!result.successful()) {
                return result;
            }
            return verification.verify(actual);
        };
    }
}
