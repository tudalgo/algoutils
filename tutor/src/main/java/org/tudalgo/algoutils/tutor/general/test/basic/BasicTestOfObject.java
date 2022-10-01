package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.TestOfObject;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject;

public class BasicTestOfObject<T> extends BasicTest<BasicTestOfObject<T>, ExpectedObject<T>, BasicResultOfObject<T>, ActualObject<T>> implements TestOfObject<T, BasicTestOfObject<T>, BasicResultOfObject<T>> {

    public BasicTestOfObject(Environment environment, ExpectedObject<T> expected) {
        super(environment, expected);
    }

    @Override
    public BasicResultOfObject<T> run(ObjectCallable<T> callable) {
        var builder = new BasicResultOfObject.Builder<T>(environment()).test(this);
        try {
            var object = callable.call();
            builder.actual(object, expected().test(object));
        } catch (Exception e) {
            builder.exception(e);
        }
        return builder.build();
    }

    public static final class Builder<T> extends BasicTest.Builder<BasicTestOfObject<T>, ExpectedObject<T>, BasicResultOfObject<T>, ActualObject<T>, Builder<T>> implements TestOfObject.Builder<T, BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> {

        private Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicTestOfObject<T> build() {
            return new BasicTestOfObject<>(environment, expected);
        }

        public static final class Factory<T> extends BasicTest.Builder.Factory<BasicTestOfObject<T>, ExpectedObject<T>, BasicResultOfObject<T>, ActualObject<T>, Builder<T>> implements TestOfObject.Builder.Factory<T, BasicTestOfObject<T>, BasicResultOfObject<T>, Builder<T>> {

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
