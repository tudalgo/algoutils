package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.Actual;
import org.tudalgo.algoutils.tutor.general.test.expected.Expected;

/**
 * An abstract test type testing the state, the behavior and/or the result of a unit under test.
 *
 * @author Dustin Glaser
 */
public interface Test<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual> {

    ET expected();

    interface Builder<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual, BT extends Test.Builder<TT, ET, RT, AT, BT>> {

        TT build();

        BT expected(ET expected);

        interface Factory<TT extends Test<TT, ET, RT, AT>, ET extends Expected, RT extends Result<RT, AT, TT, ET>, AT extends Actual, BT extends Builder<TT, ET, RT, AT, BT>> {

            Builder<TT, ET, RT, AT, BT> builder();
        }
    }
}
