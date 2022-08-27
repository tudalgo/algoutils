package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link Test}.
 *
 * @author Dustin Glaser
 */
public interface Result<T extends Test> {

    /**
     * Returns an object describing the actual behavior of the unit under test or <code>null</code> if there is not such an object.
     *
     * @return the object describing the actual behavior or <code>null</code>
     */
    Object behaviorActual();

    /**
     * Returns an object describing the expected behavior of the unit under test or <code>null</code> if there is not such an object.
     *
     * @return the object describing the expected behavior or <code>null</code>
     */
    default Object behaviorExpected() {
        return test().expectation();
    }

    /**
     * Returns if the test was successful.
     *
     * @return if the test was successful
     */
    boolean successful();

    /**
     * Returns the test that was run.
     *
     * @return the test that was run
     */
    T test();
}
