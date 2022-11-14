package org.tudalgo.algoutils.tutor.general.assertions;

import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.basic.*;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedExceptional;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.lang.reflect.InvocationTargetException;

import static org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObjects.*;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.nothing;

/**
 * A collection of assertion methods.
 *
 * @author Dustin Glaser
 */
public final class Assertions2 {

    private static final Environment environment = BasicEnvironment.getInstance();
    private static final TestOfCall.Builder.Factory TEST_OF_CALL_BUILDER_FACTORY = new BasicTestOfCall.Builder.Factory(environment);
    private static final TestOfObject.Builder.Factory<?> TEST_OF_OBJECT_BUILDER_FACTORY = new BasicTestOfObject.Builder.Factory<>(environment);
    private static final TestOfExceptionalCall.Builder.Factory<?> TEST_OF_THROWABLE_CALL_BUILDER_FACTORY = new BasicTestOfExceptionalCall.Builder.Factory<>(environment);
    private static final Fail.Builder.Factory FAIL_BUILDER_FACTORY = new BasicFail.Builder.Factory(environment);
    private static final Context.Builder.Factory<?> CONTEXT_BUILDER_FACTORY = new BasicContext.Builder.Factory();

    // no instantiation allowed
    private Assertions2() {
    }

    /**
     * <p>Asserts that the callable returns an object equal to the given expected object.</p>
     *
     * @param expected           the expected object
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertCallEquals(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(equalTo(expected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the callable returns {@link Boolean#FALSE}.</p>
     *
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static boolean assertCallFalse(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsFalse()).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Asserts that the callable returns an object not equal to the given object.
     *
     * @param unexpected         the unexpected object
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertCallNotEquals(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsTo(unexpected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Asserts that the callable returns an object.
     *
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertCallNotNull(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsNull()).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Asserts that the given callable does not return the given object.
     *
     * @param unexpected         the unexpected object
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertCallNotSame(T unexpected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notSameAs(unexpected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given callable returns null.</p>
     *
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static <T> T assertCallNull(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(equalsNull()).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Asserts that the given callable returns the given object.
     *
     * @param expected           the expected object
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertCallSame(T expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(sameAs(expected)).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given callable returns {@link Boolean#TRUE}.</p>
     *
     * @param callable           the callable to retrieve the object from
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static boolean assertCallTrue(ObjectCallable<Boolean> callable, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsTrue()).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given actual object is equal to the given expected object.</p>
     *
     * @param expected the expected object
     * @param actual   the actual object
     * @param context  the context of the test
     * @param <T>      the type of the object
     * @return the result of the test
     */
    public static <T> T assertEquals(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(equalTo(expected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the callable returns {@link Boolean#FALSE}.</p>
     *
     * @param actual             the actual value
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static boolean assertFalse(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsFalse()).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given actual object is not equal to the given unexpected object.</p>
     *
     * @param unexpected the unexpected object
     * @param actual     the actual object
     * @param context    the context of the test
     * @param <T>        the type of the object
     * @return the result of the test
     */
    public static <T> T assertNotEquals(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsTo(unexpected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given actual object is not null.</p>
     *
     * @param actual             the actual object
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertNotNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notEqualsNull()).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given actual object is not the same as the given unexpected object.</p>
     *
     * @param unexpected the unexpected object
     * @param actual     the actual object
     * @param context    the context of the test
     * @param <T>        the type of the object
     * @return the result of the test
     */
    public static <T> T assertNotSame(T unexpected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(notSameAs(unexpected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given actual object is null.</p>
     *
     * @param actual             the actual object
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the type of the object
     * @return the result of the test
     */
    public static <T> T assertNull(T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        Assertions2.<T>testOfObjectBuilder().expected(equalsNull()).build().run(actual).check(context, preCommentSupplier);
        return null;
    }

    /**
     * <p>Asserts that the given actual object is the same as the given expected object.</p>
     *
     * @param expected the expected object
     * @param actual   the actual object
     * @param context  the context of the test
     * @param <T>      the type of the object
     * @return the result of the test
     */
    public static <T> T assertSame(T expected, T actual, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(sameAs(expected)).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * <p>Asserts that the given callable throws an exception of the given expected type.</p>
     *
     * @param expected           the expected type of the exception
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static <T extends Exception> T assertThrows(Class<T> expected, Callable callable, Context context, PreCommentSupplier<? super ResultOfExceptionalCall<T>> preCommentSupplier) {
        return Assertions2.<T>testOfThrowableCallBuilder().expected(ExpectedExceptional.instanceOf(expected)).build().run(callable).check(context, preCommentSupplier).actual().behavior();
    }

    public static void call(Callable callable, Context context, PreCommentSupplier<? super ResultOfCall> preCommentSupplier) {
        Assertions2.testOfCallBuilder().expected(nothing()).build().run(callable).check(context, preCommentSupplier);
    }

    public static <T> T callObject(ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(something()).build().run(callable).check(context, preCommentSupplier).object();
    }

    /**
     * Asserts that the given boolean value is true.
     *
     * @param actual             the boolean value
     * @param context            the context of the test
     * @param preCommentSupplier the supplier of the pre-comment
     * @return the result of the test
     */
    public static boolean assertTrue(boolean actual, Context context, PreCommentSupplier<? super ResultOfObject<Boolean>> preCommentSupplier) {
        return Assertions2.<Boolean>testOfObjectBuilder().expected(equalsTrue()).build().run(actual).check(context, preCommentSupplier).object();
    }

    /**
     * Returns a {@linkplain Context contexts} builder.
     *
     * @return the context builder
     */
    public static Context.Builder<?> contextBuilder() {
        return CONTEXT_BUILDER_FACTORY.builder();
    }

    /**
     * Returns an empty {@linkplain Context contexts}.
     *
     * @return the empty context
     */
    public static Context emptyContext() {
        return contextBuilder().build();
    }

    /**
     * Fails.
     *
     * @param context            the context of the fail
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the return type (for convenience reasons)
     * @return nothing
     */
    public static <T> T fail(Context context, PreCommentSupplier<? super ResultOfFail> preCommentSupplier) {
        return fail(null, context, preCommentSupplier);
    }

    /**
     * Fails.
     *
     * @param cause              the cause of the fail
     * @param context            the context of the fail
     * @param preCommentSupplier the supplier of the pre-comment
     * @param <T>                the return type (for convenience reasons)
     * @return nothing
     */
    public static <T> T fail(Exception cause, Context context, PreCommentSupplier<? super ResultOfFail> preCommentSupplier) {
        failBuilder().expected(nothing()).build().run(nothing(), cause).check(context, preCommentSupplier);
        return null;
    }

    public static <T> T fail(Expected expected, Actual actual, Context context, PreCommentSupplier<? super ResultOfFail> preCommentSupplier) {
        failBuilder().expected(expected).build().run(actual, null).check(context, preCommentSupplier);
        return null;
    }

    /**
     * Returns a {@linkplain Fail fail} builder.
     *
     * @return the fail builder
     */
    public static Fail.Builder failBuilder() {
        return FAIL_BUILDER_FACTORY.builder();
    }

    public static TestOfCall.Builder testOfCallBuilder() {
        return TEST_OF_CALL_BUILDER_FACTORY.builder();
    }

    /**
     * Returns a {@linkplain TestOfObject object test} builder.
     *
     * @return the object test builder
     */
    public static <T> TestOfObject.Builder<T> testOfObjectBuilder() {
        //noinspection unchecked
        return (TestOfObject.Builder<T>) TEST_OF_OBJECT_BUILDER_FACTORY.builder();
    }

    /**
     * Returns a {@linkplain TestOfExceptionalCall throwable call test} builder.
     *
     * @return the throwable call test builder
     */
    public static <T extends Exception> TestOfExceptionalCall.Builder<T> testOfThrowableCallBuilder() {
        //noinspection unchecked
        return (TestOfExceptionalCall.Builder<T>) TEST_OF_THROWABLE_CALL_BUILDER_FACTORY.builder();
    }

    public static Context context(Object... records) {
        var cb = contextBuilder();
        for (var record : records) {
            if (record.getClass().isRecord()) {
                try {
                    var c = record.getClass().getRecordComponents();
                    for (var component : c) {
                        if (component.getName().startsWith("_")) {
                            continue;
                        }
                        var value = component.getAccessor().invoke(record);
                        if (value != null && value.getClass().isRecord()) {
                            cb.add(component.getName(), context(value));
                        } else {
                            cb.add(component.getName(), value);
                        }
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return cb.build();
    }
}
