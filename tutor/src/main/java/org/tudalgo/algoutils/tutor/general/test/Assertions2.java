package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.basic.BasicContext;
import org.tudalgo.algoutils.tutor.general.test.basic.BasicTestOfObject;
import org.tudalgo.algoutils.tutor.general.test.basic.BasicTestOfThrowableCall;
import org.tudalgo.algoutils.tutor.general.test.expected.ExpectedExceptions;

import static org.tudalgo.algoutils.tutor.general.test.expected.ExpectedObjects.*;

public final class Assertions2 {

    private static final Environment environment = new BasicEnvironment();
    private static final TestOfObject.Builder.Factory<?, ?, ?, ?> TEST_OF_OBJECT_BUILDER_FACTORY = new BasicTestOfObject.Builder.Factory<>(environment);
    private static final TestOfThrowableCall.Builder.Factory<?, ?, ?, ?> TEST_OF_THROWABLE_CALL_BUILDER_FACTORY = new BasicTestOfThrowableCall.Builder.Factory<>(environment);
    private static final Context.Builder.Factory CONTEXT_BUILDER_FACTORY = new BasicContext.Builder.Factory();
    private static final Context CONTEXT_EMPTY = contextBuilder().build();

    public static <T> T assertCallEquals(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(equalTo(expected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static boolean assertCallFalse(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObject<Boolean, ?, ?>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsFalse()).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static <T> T assertCallNotEquals(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsTo(unexpected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static <T> T assertCallNotNull(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsNull()).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static <T> T assertCallNotSame(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notSameAs(unexpected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static Object assertCallNull(ObjectCallable<Object> callable, Context context, PreCommentSupplier<? super ResultOfObject<Object, ?, ?>> preCommentSupplier) {
        Assertions2.testOfObjectBuilder().expected(equalsNull()).build().run(callable).check(context, preCommentSupplier);
        return null;
    }

    public static <T> T assertCallSame(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(sameAs(expected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static boolean assertCallTrue(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObject<Boolean, ?, ?>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsTrue()).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static <T> T assertEquals(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(equalTo(expected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static boolean assertFalse(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean, ?, ?>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsFalse()).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static <T> T assertNotEquals(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsTo(unexpected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static <T> T assertNotNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsNull()).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static <T> T assertNotSame(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notSameAs(unexpected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static <T> T assertNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        Assertions2.<T>testOfObjectBuilder().expected(equalsNull()).build().run(actual).check(context, preCommentSupplier);
        return null;
    }

    public static <T> T assertSame(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(sameAs(expected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static <T extends Exception> T assertThrows(Class<T> expected, Callable callable, Context context, PreCommentSupplier<? super ResultOfThrowableCall<T, ?, ?>> preCommentSupplier) {
        return Assertions2.<T>testOfThrowableCallBuilder().expected(ExpectedExceptions.instanceOf(expected)).build().run(callable).check(context, preCommentSupplier).actual().object();
    }

    public static boolean assertTrue(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean, ?, ?>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsTrue()).build().run(actual).check(context, preCommentSupplier).object();
    }

    public static Context.Builder contextBuilder() {
        return CONTEXT_BUILDER_FACTORY.builder();
    }

    public static Context emptyContext() {
        return CONTEXT_EMPTY;
    }

    public static <T> TestOfObject.Builder<T, ?, ?, ?> testOfObjectBuilder() {
        //noinspection unchecked
        return (TestOfObject.Builder<T, ?, ?, ?>) TEST_OF_OBJECT_BUILDER_FACTORY.builder();
    }

    public static <T extends Exception> TestOfThrowableCall.Builder<T, ?, ?, ?> testOfThrowableCallBuilder() {
        //noinspection unchecked
        return (TestOfThrowableCall.Builder<T, ?, ?, ?>) TEST_OF_THROWABLE_CALL_BUILDER_FACTORY.builder();
    }
}
