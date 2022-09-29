package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.ResultOfCall;

import static org.tudalgo.algoutils.tutor.general.Utils.none;

public final class BasicResultOfCall extends BasicResult<BasicResultOfCall, BasicTestOfCall> implements ResultOfCall<BasicResultOfCall, BasicTestOfCall> {

    private final Throwable throwable;

    public BasicResultOfCall(Environment environment, BasicTestOfCall test, boolean successful) {
        this(environment, test, successful, null);
    }

    public BasicResultOfCall(Environment environment, BasicTestOfCall test, boolean successful, Throwable throwable) {
        super(environment, test, successful);
        this.throwable = throwable;
    }

    @Override
    public Object actual() {
        return throwable != null ? throwable : none();
    }

    @Override
    public Object expected() {
        return super.expected();
    }

    @Override
    public Throwable throwable() {
        return throwable;
    }

    public static final class Builder extends BasicResult.Builder<BasicResultOfCall, BasicTestOfCall, Builder> implements ResultOfCall.Builder<BasicResultOfCall, BasicTestOfCall, Builder> {

        private Throwable throwable;

        public Builder(Environment environment) {
            super(environment);
        }

        @Override
        public BasicResultOfCall build() {
            return new BasicResultOfCall(environment, test, successful, throwable);
        }

        public Builder throwable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public static final class Factory extends BasicResult.Builder.Factory<BasicResultOfCall, BasicTestOfCall, Builder> implements ResultOfCall.Builder.Factory<BasicResultOfCall, BasicTestOfCall, Builder> {

            public Factory(Environment environment) {
                super(environment);
            }

            @Override
            public Builder builder() {
                return new Builder(environment);
            }
        }
    }
}
