package org.tudalgo.algoutils.tutor.general.test;

/**
 * An abstract type representing a result <code>with</code> a exception.
 *
 * @param <T> the type of the test
 * @param <E> the type of the exception
 */
public interface ResultWithThrowable<T extends Test, E extends Throwable> extends Result<T> {

    /**
     * Returns the actual exception.
     *
     * @return the actual exception
     */
    E actualThrowable();
}
