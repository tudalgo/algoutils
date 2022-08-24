package org.tudalgo.algoutils.tutor.general.test;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;
import org.tudalgo.algoutils.tutor.general.test.basic.*;

import java.util.Objects;

public final class Assertions2 {

    private static final Environment environment = new BasicEnvironment();
    private static final TestOfCall.Builder.Factory TEST_OF_CALL_BUILDER_FACTORY = new BasicTestOfCall.Builder.Factory(environment);
    private static final TestOfObject.Builder.Factory TEST_OF_OBJECT_BUILDER_FACTORY = new BasicTestOfObject.Builder.Factory(environment);
    private static final TestOfObjectCall.Builder.Factory TEST_OF_OBJECT_CALL_BUILDER_FACTORY = new BasicTestOfObjectCall.Builder.Factory(environment);
    private static final TestOfThrowableCall.Builder.Factory TEST_OF_THROWABLE_CALL_BUILDER_FACTORY = new BasicTestOfThrowableCall.Builder.Factory(environment);
    private static final Context.Builder.Factory CONTEXT_BUILDER_FACTORY = new BasicContext.Builder.Factory();

    public static Context.Builder contextBuilder() {
        return CONTEXT_BUILDER_FACTORY.builder();
    }

    public static TestOfCall.Builder testOfCallBuilder() {
        return TEST_OF_CALL_BUILDER_FACTORY.builder();
    }

    public static <T> TestOfObject.Builder<T> testOfObjectBuilder() {
        return TEST_OF_OBJECT_BUILDER_FACTORY.builder();
    }

    public static <T> TestOfObjectCall.Builder<T> testOfObjectCallBuilder() {
        return TEST_OF_OBJECT_CALL_BUILDER_FACTORY.builder();
    }

    public static <T extends Throwable> TestOfThrowableCall.Builder<T> testOfThrowableCallBuilder() {
        return TEST_OF_THROWABLE_CALL_BUILDER_FACTORY.builder();
    }

    public static <T> T assertEquals(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectCallBuilder().expectation(expected).evaluator(o -> Objects.equals(expected, o)).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> T assertEquals(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expectation(expected).evaluator(o -> Objects.equals(expected, o)).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static boolean assertFalse(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectCallBuilder().expectation(false).evaluator(o -> !o).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static boolean assertFalse(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expectation(false).evaluator(o -> !o).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static boolean assertTrue(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectCallBuilder().expectation(true).evaluator(o -> o).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static boolean assertTrue(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expectation(true).evaluator(o -> o).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static <T> T assertNotEquals(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectCallBuilder().expectation(unexpected).evaluator(o -> !Objects.equals(unexpected, o)).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> T assertNotEquals(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expectation(unexpected).evaluator(o -> !Objects.equals(unexpected, o)).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static <T> T assertNotNull(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectCallBuilder().expectation(null).evaluator(Objects::nonNull).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> T assertNotNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expectation(null).evaluator(Objects::nonNull).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static <T> T assertNotSame(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectCallBuilder().expectation(unexpected).evaluator(o -> o != unexpected).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> T assertNotSame(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expectation(unexpected).evaluator(o -> o != unexpected).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static void assertNull(ObjectCallable<Object> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<Object>> preCommentSupplier) {
        Assertions2.testOfObjectCallBuilder().expectation(null).evaluator(Objects::isNull).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> void assertNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        Assertions2.<T>testOfObjectBuilder().expectation(null).evaluator(Objects::isNull).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static <T> T assertSame(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObjectCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectCallBuilder().expectation(expected).evaluator(o -> o == expected).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }

    public static <T> T assertSame(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expectation(expected).evaluator(o -> o == expected).build().assertSuccessful(actual, context, preCommentSupplier);
    }

    public static <T extends Throwable> T assertThrows(Class<T> expected, Callable callable, Context context, PreCommentSupplier<? super ResultOfThrowableCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfThrowableCallBuilder().expectation(expected).evaluator(expected, o -> true).build().test(callable).assertSuccessful(context, preCommentSupplier);
    }
}
