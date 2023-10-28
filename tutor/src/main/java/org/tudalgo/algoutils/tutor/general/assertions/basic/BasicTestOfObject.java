package org.tudalgo.algoutils.tutor.general.assertions.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.TestOfObject;
import org.tudalgo.algoutils.tutor.general.assertions.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

/**
 * <p>A basic implementation of a test of an object.</p>
 *
 * @param <T> the type of object under test
 */
public class BasicTestOfObject<T> extends BasicTest<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>> implements TestOfObject<T> {

    /**
     * <p>Constructs a new test of an object with the given environment and expected object.</p>
     *
     * @param environment the environment
     * @param expected    the expected object
     */
    public BasicTestOfObject(Environment environment, ExpectedObject<T> expected) {
        super(environment, expected);
    }

    @Override
    public ResultOfObject<T> run(ObjectCallable<T> callable) {
        var builder = new BasicResultOfObject.Builder<T>(environment()).test(this);
        try {
            var object = callable.call();
            builder.object(object, expected().test(object));
        } catch (Throwable e) {
            builder.exception(e);
        }
        return builder.build();
    }

    public static final class Builder<T> extends BasicTest.Builder<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>, TestOfObject.Builder<T>> implements TestOfObject.Builder<T> {

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicTestOfObject<T> build() {
            return new BasicTestOfObject<>(environment, expected);
        }

        public static final class Factory<T> extends BasicTest.Builder.Factory<TestOfObject<T>, ExpectedObject<T>, ResultOfObject<T>, ActualObject<T>, TestOfObject.Builder<T>> implements TestOfObject.Builder.Factory<T> {

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
