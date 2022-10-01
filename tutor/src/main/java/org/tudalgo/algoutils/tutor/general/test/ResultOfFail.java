package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

public interface ResultOfFail<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>> extends Result<RT, NoActual, TT, Nothing> {

    interface Builder<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder<RT, NoActual, TT, Nothing, BT> {

        interface Factory<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder.Factory<RT, NoActual, TT, Nothing, BT> {

        }
    }
}
