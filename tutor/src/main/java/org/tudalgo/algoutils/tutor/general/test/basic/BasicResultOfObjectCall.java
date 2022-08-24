package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObjectCall;
import org.tudalgo.algoutils.tutor.general.test.TestOfObjectCall;

public class BasicResultOfObjectCall<T> extends BasicResult<TestOfObjectCall<T>> implements ResultOfObjectCall<T> {

    private final T actualObject;
    private final Throwable actualThrowable;

    public BasicResultOfObjectCall(Environment environment, TestOfObjectCall<T> test, boolean successful, T actualObject, Throwable actualThrowable) {
        super(environment, test, successful);
        this.actualObject = actualObject;
        this.actualThrowable = actualThrowable;
    }

    @Override
    public T actualObject() {
        return actualObject;
    }

    @Override
    public Throwable actualThrowable() {
        return actualThrowable;
    }

    @Override
    public T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        check(this, context, preCommentSupplier);
        return actualObject();
    }

    @Override
    public Object behaviorActual() {
        return actualObject;
    }
}
