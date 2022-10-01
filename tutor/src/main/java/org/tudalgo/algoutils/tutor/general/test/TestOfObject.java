package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject;

/**
 * A test type testing an object.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface TestOfObject<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>> extends Test<TT, ExpectedObject<T>, RT, ActualObject<T>> {

    /**
     * Tests if the given object is as expected.
     *
     * @param object the object under test
     * @return the result of the test
     */
    default RT run(T object) {
        return run(() -> object);
    }

    RT run(ObjectCallable<T> callable);

    interface Builder<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder<TT, ExpectedObject<T>, RT, ActualObject<T>, BT> {

        interface Factory<T, TT extends TestOfObject<T, TT, RT>, RT extends ResultOfObject<T, RT, TT>, BT extends Builder<T, TT, RT, BT>> extends Test.Builder.Factory<TT, ExpectedObject<T>, RT, ActualObject<T>, BT> {

            Builder<T, TT, RT, BT> builder();
        }
    }
}
