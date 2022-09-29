package org.tudalgo.algoutils.tutor.general.test;

/**
 * An abstract test type testing the state, the behavior and/or the result of a unit under test.
 *
 * @author Dustin Glaser
 */
public interface Test<TT extends Test<TT, RT>, RT extends Result<RT, TT>> {

    /**
     * <p>Returns an object <b>describing</b> the expected behavior of the unit under test or <code>null</code> if there is not such an object.</p>
     * <p>In general, the object returned by this method is <b>not</b> the expected object.</p>
     *
     * @return the object describing the expected behavior or <code>null</code>
     */
    Object expectation();

    interface Builder<TT extends Test<TT, RT>, RT extends Result<RT, TT>, BT extends Test.Builder<TT, RT, BT>> {

        TT build();

        BT expectation(Object expectation);

        interface Factory<TT extends Test<TT, RT>, RT extends Result<RT, TT>, BT extends Builder<TT, RT, BT>> {

            Builder<TT, RT, BT> builder();
        }
    }
}
