package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.test.actual.NoActual;
import org.tudalgo.algoutils.tutor.general.test.expected.Nothing;

/**
 * <p>A result of a {@linkplain Fail fail}.</p>
 *
 * @param <RT> the type of the result of fail
 * @param <TT> the type of fail
 */
public interface ResultOfFail<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>> extends Result<RT, NoActual, TT, Nothing> {

    /**
     * <p>A builder for {@linkplain Context results of fails}.</p>
     *
     * @param <RT> the type of the result of fail
     * @param <TT> the type of fail
     * @param <BT> the type of the builder
     */
    interface Builder<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder<RT, NoActual, TT, Nothing, BT> {

        /**
         * <p>A factory for {@link Builder result of fail builders}.</p>
         *
         * @param <RT> the type of the result of fail
         * @param <TT> the type of fail
         * @param <BT> the type of the builder
         */
        interface Factory<RT extends ResultOfFail<RT, TT>, TT extends Fail<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder.Factory<RT, NoActual, TT, Nothing, BT> {

        }
    }
}
