package org.tudalgo.algoutils.tutor.general.assertions;

import org.junit.jupiter.api.function.ThrowingSupplier;
import org.tudalgo.algoutils.tutor.general.Environment;
import org.tudalgo.algoutils.tutor.general.assertions.actual.Actual;
import org.tudalgo.algoutils.tutor.general.assertions.basic.*;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Expected;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedExceptional;
import org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObject;
import org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing;
import org.tudalgo.algoutils.tutor.general.basic.BasicEnvironment;
import org.tudalgo.algoutils.tutor.general.callable.Callable;
import org.tudalgo.algoutils.tutor.general.callable.ObjectCallable;

import java.lang.reflect.InvocationTargetException;

import static org.tudalgo.algoutils.tutor.general.assertions.expected.ExpectedObjects.*;
import static org.tudalgo.algoutils.tutor.general.assertions.expected.Nothing.nothing;

/**
 * <p>The Assertions2 class roughly mimics the functionality of the org.junit.jupiter.api.Assertions class while providing
 * the option to pass along a {@link Context}.</p>
 *
 * <p><b>Note:</b> some methods do not provice a 1:1 mapping to the org.junit.jupiter.api.Assertions class. For example,
 *  * instead of using {@link org.junit.jupiter.api.Assertions#assertDoesNotThrow(ThrowingSupplier)} you should use
 *  * {@link Assertions2#call(Callable, Context, PreCommentSupplier)} or if you need the result of the call you should use
 *  * {@link Assertions2#callObject(ObjectCallable, Context, PreCommentSupplier)}.</p>
 *
 * <p>Here is an example of how to convert Junit5 assertions to Assertions2 assertions:
 *
 * <pre>{@code
 * // Junit5
 * Assertions.assertEquals(1 + 1, 2, "The result of 1 + 1 should be 2.");
 *
 * // Assertions2
 * Context context = Assertions2.contextBuilder()
 *     .add("summand1", 1)
 *     .add("summand2", 1)
 *     .build();
 * Assertions2.assertEquals(2, 1 + 1, context, r -> "The result of 1 + 1 should be 2.");
 * }</pre>
 * Of course in this example it doesn't make much sense to use Assertions2 instead of Junit5, since the context is obvoius.
 * Here is a more practical example:
 * <pre>{@code
 *  // Junit5, not using JSON parameter set Tests
 * @Test
 * public void testMoveToPosition() {
 *    World.setSize(10,10);
 *    var robot = new Robot(1,1, Direction.UP, 0);
 *    assertions2.assertDoesNotThrow(() -> MyTestClass.moveToPosition(robot, 5, 5), "The Method threw an exception.");
 *    assertions2.assertEquals(5, robot.getX(), "Wrong final x coordinate.");
 *    assertions2.assertEquals(5, robot.getY(), "The final y coordinate.");
 * }
 *
 * // assertions2, using JSON parameter set Tests
 *
 * public static final Map<String, Function<JsonNode, ?>> customConverters = Map.ofEntries(
 *         Map.entry("worldWidth", JsonNode::asInt),
 *         Map.entry("worldHeight", JsonNode::asInt),
 *         Map.entry("robot", JsonConverters::toRobot),
 *         Map.entry("expectedEndX", JsonNode::asInt),
 *         Map.entry("expectedEndY", JsonNode::asInt)
 *     );
 * @ParameterizedTest
 * @JsonParameterSetTest(value = "inputs.json", customConverters = "customConverters")
 * public void testMovementInvalidDirection(final JsonParameterSet params) {
 *    var worldWidth = params.getInt("worldWidth");
 *    var worldHeight = params.getInt("worldHeight");
 *    World.setSize(worldWidth, worldHeight);
 *    var robot = params.<Robot>get("robot");
 *    var expectedEndX = params.getInt("expectedEndX");
 *    var expectedEndY = params.getInt("expectedEndY");
 *    var context = params.toContext("expectedEndX", "expectedEndY");
 *    assertions2.call(() -> MyTestClass.moveToPosition(robot, worldWidth, worldHeight), context, r -> "The Method threw
 *    an exception.");
 *    assertions2.assertEquals(expectedEndX, robot.getX(), context, r -> "Wrong final x coordinate.");
 *    assertions2.assertEquals(expectedEndY, robot.getY(), context, r -> "The final y coordinate.");
 * }
 * }</pre>
 * <p>In this example the student gets way more information about the test failure than in the Junit5 example while also
 * providing a more readable test method.
 * </p>
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

    public static <T> T objectAssert(ExpectedObject<T> expected, T object, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(expected).build().run(object).check(context, preCommentSupplier).object();
    }

    public static <T> T objectAssert(ExpectedObject<T> expected, T object, Context context) {
        return objectAssert(expected, object, context, null);
    }

    public static <T> T objectAssert(ExpectedObject<T> expected, T object, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return objectAssert(expected, object, null, preCommentSupplier);
    }

    public static <T> T objectAssert(ExpectedObject<T> expected, ObjectCallable<T> callable, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return Assertions2.<T>testOfObjectBuilder().expected(expected).build().run(callable).check(context, preCommentSupplier).object();
    }

    public static <T> T objectAssert(ExpectedObject<T> expected, ObjectCallable<T> callable, Context context) {
        return objectAssert(expected, callable, context, null);
    }

    public static <T> T objectAssert(ExpectedObject<T> expected, ObjectCallable<T> callable, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return objectAssert(expected, callable, null, preCommentSupplier);
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
    public static <T> T fail(Throwable cause, Context context, PreCommentSupplier<? super ResultOfFail> preCommentSupplier) {
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
