package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.test.actual.ActualObject;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObject;

public class BasicResultOfObject<T> extends BasicResult<BasicResultOfObject<T>, ActualObject<T>, BasicTestOfObject<T>, ExpectedObject<T>> implements ResultOfObject<T, BasicResultOfObject<T>, BasicTestOfObject<T>> {

    private final ActualObject<T> actual;

    public BasicResultOfObject(Environment environment, BasicTestOfObject<T> test, ActualObject<T> actual, Exception exception) {
        super(environment, test, actual, exception);
        this.actual = actual;
    }

    @Override
    public ActualObject<T> actual() {
        return actual;
    }

    public static final class Builder<T> extends BasicResult.Builder<BasicResultOfObject<T>, ActualObject<T>, BasicTestOfObject<T>, ExpectedObject<T>, Builder<T>> implements ResultOfObject.Builder<T, BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> {

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfObject<T> build() {
            return new BasicResultOfObject<>(environment, test, actual, exception);
        }

        public static final class Factory<T> extends BasicResult.Builder.Factory<BasicResultOfObject<T>, ActualObject<T>, BasicTestOfObject<T>, ExpectedObject<T>, Builder<T>> implements ResultOfObject.Builder.Factory<T, BasicResultOfObject<T>, BasicTestOfObject<T>, Builder<T>> {

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
