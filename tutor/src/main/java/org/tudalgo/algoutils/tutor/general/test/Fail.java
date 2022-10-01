package org.tudalgo.algoutils.tutor.general.test;


import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

public interface Fail<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>> extends Test<TT, Nothing, RT, NoActual> {

    RT run(Exception exception);

    default RT run() {
        return run(null);
    }

    interface Builder<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>, BT extends Builder<TT, RT, BT>> extends Test.Builder<TT, Nothing, RT, NoActual, BT> {

        interface Factory<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>, BT extends Builder<TT, RT, BT>> extends Test.Builder.Factory<TT, Nothing, RT, NoActual, BT> {

        }
    }
}
