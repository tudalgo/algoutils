package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.ResultOfThrowableCall;
import org.tudalgo.algoutils.tutor.general.test.TestOfThrowableCall;

public class BasicResultOfThrowableCall<T extends Throwable> extends BasicResult<TestOfThrowableCall<T>> implements ResultOfThrowableCall<T> {

    private final T actualThrowable;
    private final Object actualBehavior;

    public BasicResultOfThrowableCall(Environment environment, TestOfThrowableCall<T> test, boolean successful, T actualThrowable, Object actualBehavior) {
        super(environment, test, successful);
        this.actualThrowable = actualThrowable;
        this.actualBehavior = actualBehavior;
    }

    @Override
    public T actualThrowable() {
        return actualThrowable;
    }

    @Override
    public T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier) {
        check(this, context, preCommentSupplier);
        return actualThrowable();
    }


    @Override
    public Object behaviorActual() {
        return actualBehavior;
    }
}
