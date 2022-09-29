package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Predicate;

/**
 * A test type testing an object.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface TestOfObject<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>> extends Test<TT, RT> {

    /**
     * Tests if the given object is as expected.
     *
     * @param object the object under test
     * @return the result of the test
     */
    RT run(T object);

    interface Builder<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, RT, BT> {

        BT evaluator(Predicate<T> evaluator);

        interface Factory<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, RT, BT> {

            Builder<T, TT, RT, BT> builder();
        }
    }
}
