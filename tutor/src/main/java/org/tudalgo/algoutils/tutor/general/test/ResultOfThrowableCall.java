package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfThrowableCall}.
 *
 * @param <T> the exception type of the callable under test
 * @author Dustin Glaser
 */
public interface ResultOfThrowableCall<T extends Throwable, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>> extends Result<RT, TT> {

    T throwable();

    interface Builder<T extends Throwable, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, TT, BT> {

        BT actual(Object actual);

        BT throwable(T actualThrowable);


        interface Factory<T extends Throwable, RT extends ResultOfThrowableCall<T, RT, TT>, TT extends TestOfThrowableCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder.Factory<RT, TT, BT> {

            Builder<T, RT, TT, BT> builder();
        }
    }
}
