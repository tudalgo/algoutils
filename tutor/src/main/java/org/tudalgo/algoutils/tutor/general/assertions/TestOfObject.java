package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;

/**
 * <p>A test of an object.</p>
 *
 * @param <T>  the type of the object under test
 * @param <TT> the type of the test
 * @param <RT> the type of the result
 * @author Dustin Glaser
 */
public interface TestOfObject<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>> extends Test<TT, ExpectedObject<T>, RT, ActualObject<T>> {

    /**
     * <p>Tests if the given object is as expected and returns the result.</p>
     *
     * @param object the object under test
     * @return the result of the test
     */
    default RT run(T object) {
        return run(() -> object);
    }

    /**
     * <p>Tests if the object retrieved by the given callable is as expected and returns the result.</p>
     *
     * @param callable the callable
     * @return the result of the test
     */
    RT run(ObjectCallable<T> callable);

    /**
     * <p>A builder for {@linkplain Test tests of objects}.</p>
     *
     * @param <T>  the type of the object under test
     * @param <TT> the type of the test
     * @param <RT> the type of the result
     * @param <BT> the type of the builder
     */
    interface Builder<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, ExpectedObject<T>, RT, ActualObject<T>, BT> {

        /**
         * A factory for {@linkplain Builder tests of object builders}.
         *
         * @param <T>  the type of the object under test
         * @param <TT> the type of the test
         * @param <RT> the type of the result
         * @param <BT> the type of the builder
         */
        interface Factory<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, ExpectedObject<T>, RT, ActualObject<T>, BT> {

        }
    }
}
