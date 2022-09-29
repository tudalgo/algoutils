package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link Test}.
 *
 * @author Dustin Glaser
 */
public interface Result<RT extends Result<RT, TT>, TT extends Test<TT, RT>> {

    /**
     * Returns an object describing the actual behavior of the unit under test or <code>null</code> if there is not such an object.
     *
     * @return the object describing the actual behavior or <code>null</code>
     */
    Object actual();

    RT check(Context context, PreCommentSupplier<? super RT> preCommentSupplier);

    /**
     * Returns an object describing the expected behavior of the unit under test or <code>null</code> if there is not such an object.
     *
     * @return the object describing the expected behavior or <code>null</code>
     */
    default Object expected() {
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
    TT test();

    interface Builder<RT extends Result<RT, TT>, TT extends Test<TT, RT>, BT extends Builder<RT, TT, BT>> {

        RT build();

        BT successful(boolean successful);

        BT test(TT test);

        interface Factory<RT extends Result<RT, TT>, TT extends Test<TT, RT>, BT extends Builder<RT, TT, BT>> {

            Builder<RT, TT, BT> builder();
        }
    }
}
