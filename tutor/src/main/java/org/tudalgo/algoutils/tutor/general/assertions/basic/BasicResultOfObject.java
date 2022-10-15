package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;

/**
 * <p>A basic implementation of a result of an object test.</p>
 *
 * @param <T> the type of object under test
 * @author Dustin Glaser
 */

public class BasicResultOfObject<T> extends BasicResult<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>> implements ResultOfObject<T> {

    /**
     * <p>Constructs a new result of an object test with the given environment, test, actual object, exception and state if the test was successful.</p>
     *
     * @param environment the environment
     * @param test        the test
     * @param actual      the actual object
     * @param exception   the exception
     * @param successful  the state if the test was successful
     */
    public BasicResultOfObject(Environment environment, TestOfObject<T> test, ActualObject<T> actual, Exception exception, boolean successful) {
        super(environment, test, actual, exception);
    }

    public static final class Builder<T> extends BasicResult.Builder<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>, ResultOfObject.Builder<T>> implements ResultOfObject.Builder<T> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfObject<T> build() {
            return new BasicResultOfObject<>(environment, test, actual, exception, successful);
        }

        public static final class Factory<T> extends BasicResult.Builder.Factory<ResultOfObject<T>, ActualObject<T>, TestOfObject<T>, ExpectedObject<T>, ResultOfObject.Builder<T>> implements ResultOfObject.Builder.Factory<T> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Builder<T> builder() {
                return new Builder<>(environment);
            }
        }
    }
}
