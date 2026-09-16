package org.tudalgo.algoutils.assertions.options;

import org.tudalgo.algoutils.assertions.AssertionResult;

/**
 * A collection of special {@link AssertionOption assertion options} that require specialized handling in the framework.
 */
public final class SpecialOptions {

    // Do not instantiate
    private SpecialOptions() {}

    /**
     * A special option that causes an assertion to not throw an {@link org.opentest4j.AssertionFailedError} if the verification failed.
     *
     * @return the option object
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static <E, R> AssertionOption<E, R> dontThrowOnFail() {
        return new DontThrowOnFailOption<>();
    }

    /**
     * Marker class for the {@link #dontThrowOnFail()} option.
     *
     * @param <E> the type of the expected value
     * @param <R> the type of the actual value returned by the verification
     */
    public static class DontThrowOnFailOption<E, R> implements AssertionOption<E, R> {

        @Override
        public AssertionResult<E, R> apply(AssertionResult<E, R> result) {
            return result;
        }
    }
}
