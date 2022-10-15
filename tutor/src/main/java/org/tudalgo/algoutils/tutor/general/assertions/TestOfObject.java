package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

/**
 * <p>A test of an object.</p>
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface TestOfObject<T> extends Test<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>> {

    /**
     * <p>Tests if the given object is as expected and returns the result.</p>
     *
     * @param object the object under test
     * @return the result of the test
     */
    default ResultOfObject<T> run(T object) {
        return run(() -> object);
    }

    /**
     * <p>Tests if the object retrieved by the given callable is as expected and returns the result.</p>
     *
     * @param callable the callable
     * @return the result of the test
     */
    ResultOfObject<T> run(ObjectCallable<T> callable);

    /**
     * <p>A builder for {@linkplain Test tests of objects}.</p>
     *
     * @param <T>  the type of the object under test
     */
    interface Builder<T> extends Test.Builder<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>, Builder<T>> {

        /**
         * A factory for {@linkplain Builder tests of object builders}.
         *
         * @param <T>  the type of the object under test
         */
        interface Factory<T> extends Test.Builder.Factory<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>, Builder<T>> {

        }
    }
}
