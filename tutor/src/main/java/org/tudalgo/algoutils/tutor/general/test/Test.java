package org.tudalgo.algoutils.tutor.general.test;

/**
 * An abstract test type testing the state, the behavior and/or the result of a unit under test.
 *
 * @author Dustin Glaser
 */
public interface Test {

    /**
     * <p>Returns an object <b>describing</b> the expected behavior of the unit under test or <code>null</code> if there is not such an object.</p>
     * <p>In general, the object returned by this method is <b>not</b> the expected object.</p>
     *
     * @return the object describing the expected behavior or <code>null</code>
     */
    Object expectation();

    interface Builder {

        Test build();

        Test.Builder expectation(Object expectation);

        interface Factory {

            Test.Builder builder();
        }
    }
}
