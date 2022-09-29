package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfCall}.
 *
 * @author Dustin Glaser
 */
public interface ResultOfCall<RT extends ResultOfCall<RT, TT>, TT extends TestOfCall<TT, RT>> extends Result<RT, TT> {

    Throwable throwable();

    interface Builder<RT extends ResultOfCall<RT, TT>, TT extends TestOfCall<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder<RT, TT, BT> {

        BT throwable(Throwable actualThrowable);

        interface Factory<RT extends ResultOfCall<RT, TT>, TT extends TestOfCall<TT, RT>, BT extends Builder<RT, TT, BT>> extends Result.Builder.Factory<RT, TT, BT> {

            Builder<RT, TT, BT> builder();
        }
    }
}
