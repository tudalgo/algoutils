package org.tudalgo.algoutils.tutor.general.test;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

/**
 * A type representing the result of a {@link TestOfObjectCall}.
 *
 * @param <T> the return type of the callable under test
 * @author Dustin Glaser
 */
public interface ResultOfObjectCall<T, RT extends ResultOfObjectCall<T, RT, TT>, TT extends TestOfObjectCall<T, TT, RT>> extends Result<RT, TT> {

    @Override
    default Object actual() {
        return throwable() != none() ? throwable() : object();
    }

    T object();

    Throwable throwable();


    interface Builder<T, RT extends ResultOfObjectCall<T, RT, TT>, TT extends TestOfObjectCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder<RT, TT, BT> {

        BT object(T actualObject);

        BT throwable(Throwable actualThrowable);

        interface Factory<T, RT extends ResultOfObjectCall<T, RT, TT>, TT extends TestOfObjectCall<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> extends Result.Builder.Factory<RT, TT, BT> {

            Builder<T, RT, TT, BT> builder();
        }
    }
}
