package org.tudalgo.algoutils.tutor.general.test;

/**
 * An abstract type representing a result <code>with</code> an object.
 *
 * @param <T> the type of the test
 * @param <O> the type of the object
 */
public interface ResultWithObject<T extends Test, O> extends Result<T> {

    /**
     * Returns the actual object.
     *
     * @return the actual object
     */
    O actualObject();
}
