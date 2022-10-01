package org.tudalgo.algoutils.tutor.general.test;


import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

/**
 * <p>A always-failing test.</p>>
 *
 * @param <TT> the type of fail
 * @param <RT> the type of the result of fail
 */
public interface Fail<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>> extends Test<TT, Nothing, RT, NoActual> {

    /**
     * <p>Fails this test (there is no other option) with the given cause.</p>
     *
     * @param cause the cause of the failure
     * @return the result of this fail
     */
    RT run(Exception cause);

    /**
     * <p>Fails this test (there is no other option).</p>
     *
     * @return the result of this fail
     */
    default RT run() {
        return run(null);
    }

    /**
     * <p>A builder for {@linkplain Fail fails}.</p>
     *
     * @param <TT> the type of fail
     * @param <RT> the type of the result of fail
     * @param <BT> the type of the builder
     * @author Dustin Glaser
     */
    interface Builder<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>, BT extends Builder<TT, RT, BT>> extends Test.Builder<TT, Nothing, RT, NoActual, BT> {

        /**
         * <p>A factory for {@link Builder fail builders}.</p>
         *
         * @param <TT> the type of fail
         * @param <RT> the type of the result of fail
         * @param <BT> the type of the builder
         * @author Dustin Glaser
         */
        interface Factory<TT extends Fail<TT, RT>, RT extends ResultOfFail<RT, TT>, BT extends Builder<TT, RT, BT>> extends Test.Builder.Factory<TT, Nothing, RT, NoActual, BT> {

        }
    }
}
