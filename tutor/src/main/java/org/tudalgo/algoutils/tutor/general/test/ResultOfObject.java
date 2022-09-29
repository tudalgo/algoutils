package org.tudalgo.algoutils.tutor.general.test;

/**
 * A type representing the result of a {@link TestOfObject}.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface ResultOfObject<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>> extends Result<RT, TT> {

    T object();

    interface Builder<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> {

        BT object(T actualObject);

        interface Factory<T, RT extends ResultOfObject<T, RT, TT>, TT extends TestOfObject<T, TT, RT>, BT extends Builder<T, RT, TT, BT>> {

            Builder<T, RT, TT, BT> builder();
        }
    }
}
