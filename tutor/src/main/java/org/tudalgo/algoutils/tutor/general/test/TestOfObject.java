package org.tudalgo.algoutils.tutor.general.test;

import java.util.function.Predicate;

/**
 * A test type testing an object.
 *
 * @param <T> the type of the object under test
 * @author Dustin Glaser
 */
public interface TestOfObject<T> extends Test {

    /**
     * Tests the given object and throws an error if the object is not as expected.
     *
     * @param object the object under test
     * @return the object under test (if the object is as expected)
     * @throws Error if the object is not as expected
     */
    default T assertSuccessful(T object, Context context, PreCommentSupplier<? super ResultOfObject<T>> preCommentSupplier) {
        return test(object).assertSuccessful(context, preCommentSupplier);
    }

    /**
     * Tests if the given object is as expected.
     *
     * @param object the object under test
     * @return the result of the test
     */
    ResultOfObject<T> test(T object);

    interface Builder<T> {

        TestOfObject<T> build();

        Builder<T> evaluator(Predicate<T> evaluator);

        Builder<T> expectation(Object expectation);

        interface Factory {

            <T> TestOfObject.Builder<T> builder();
        }
    }
}
