package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Test;

public abstract class BasicTest implements Test {

    protected final Environment environment;

    private final Object expectation;

    public BasicTest(Environment environment, Object expectation) {
        this.environment = environment;
        this.expectation = expectation;
    }

    public Object expectation() {
        return expectation;
    }

    public static abstract class Builder<T extends Builder<T>> implements Test.Builder {

        protected final Environment environment;

        protected Object expectation;

        protected Builder(Environment environment) {
            this.environment = environment;
        }

        @Override
        public T expectation(Object expectation) {
            this.expectation = expectation;
            //noinspection unchecked
            return (T) this;
        }

        public static abstract class Factory implements Test.Builder.Factory {

            protected final Environment environment;

            public Factory(Environment environment) {
                this.environment = environment;
            }
        }
    }
}
