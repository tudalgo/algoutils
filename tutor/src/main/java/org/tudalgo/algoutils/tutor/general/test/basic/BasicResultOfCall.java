package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.ResultOfCall;
import org.tudalgo.algoutils.tutor.general.test.TestOfCall;

public final class BasicResultOfCall extends BasicResult<TestOfCall> implements ResultOfCall {

    private final Throwable throwable;

    public BasicResultOfCall(Environment environment, TestOfCall test, boolean successful) {
        this(environment, test, successful, null);
    }

    public BasicResultOfCall(Environment environment, TestOfCall test, boolean successful, Throwable throwable) {
        super(environment, test, successful);
        this.throwable = throwable;
    }

    @Override
    public Throwable actualThrowable() {
        return throwable;
    }

    @Override
    public void assertSuccessful(Context context, PreCommentSupplier<? super ResultOfCall> preCommentSupplier) {
        check(this, context, preCommentSupplier);
    }

    @Override
    public Object behaviorActual() {
        return throwable;
    }

    @Override
    public Object behaviorExpected() {
        return super.behaviorExpected();
    }
}
