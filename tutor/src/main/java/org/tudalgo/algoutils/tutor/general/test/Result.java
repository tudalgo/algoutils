package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.Actual;
import org.tudalgo.algoutils.tutor.general.test.expected.Expected;

/**
 * A type representing the result of a {@link Test}.
 *
 * @author Dustin Glaser
 */
public interface Result<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected> {

    /**
     * Returns the actual result of the test.
     * If the test has failed due to an exception, the actual result is {@code null} and the exception can be retrieved using {@link #exception()}.
     *
     * @return TODO
     */
    AT actual();

    RT check(Context context, PreCommentSupplier<? super RT> preCommentSupplier);

    /**
     * <p>If this result is not successful due to an exception, this method returns the exception.</p>
     *
     * @return the exception
     */
    Exception exception();

    default ET expected() {
        return test().expected();
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

    interface Builder<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> {

        BT actual(AT actual);

        RT build();

        BT exception(Exception exception);

        BT test(TT test);

        interface Factory<RT extends Result<RT, AT, TT, ET>, AT extends Actual, TT extends Test<TT, ET, RT, AT>, ET extends Expected, BT extends Builder<RT, AT, TT, ET, BT>> {

            Builder<RT, AT, TT, ET, BT> builder();
        }
    }
}
