package org.tudalgo.algoutils.tutor.general.test.basic;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.test.Context;
import org.tudalgo.algoutils.tutor.general.test.PreCommentSupplier;
import org.tudalgo.algoutils.tutor.general.test.ResultOfObject;
import org.tudalgo.algoutils.tutor.general.test.TestOfObject;

public final class BasicResultOfObject<T> extends BasicResult<TestOfObject<T>> implements ResultOfObject<T> {

    private final T actualObject;

    public BasicResultOfObject(Environment environment, BasicTestOfObject<T> test, boolean successful, T actualObject) {
        super(environment, test, successful);
        this.actualObject = actualObject;
    }

    @Override
    public T actualObject() {
        return actualObject;
    }

    @Override
    public T assertSuccessful(Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        check(this, context, preCommentSupplier);
        return actualObject();
    }

    @Override
    public T behaviorActual() {
        return actualObject;
    }
}
