package org.tudalgo.algoutils.assertions;

/**
 * A wrapper class that wraps an object or an {@link Expression} returning a value or a {@link Statement}.
 * If a value or object-returning expression is wrapped, its value can be retrieved via {@link #getValue()}.
 * Otherwise, if a block of executable code is wrapped, it can be executed using {@link #call()}.
 * If the type of this wrapper is {@link Type#WRAPPED}, {@link #getValue()} can be used to execute the expression
 * and return its result, or, if no result is desired, it can be executed like a statement using {@link #call()}.
 *
 * @param <A> type of the wrapped value
 */
public interface ActualWrapper<A> {

    /**
     * Type of the wrapped value.
     */
    enum Type {
        OBJECT,
        WRAPPED,
        CALLABLE
    }

    /**
     * Returns the type of the value wrapped by this object.
     *
     * @return the type of the wrapped value
     */
    Type getType();

    /**
     * If this wrapper's type is {@link Type#OBJECT}, returns the value wrapped by this object.
     * If the type is {@link Type#WRAPPED} unwraps or executes the code to obtain the actual value
     * and returns it.
     *
     * @return the stored or evaluated value wrapped by this object
     * @throws UnsupportedOperationException if the type of this wrapper is not {@link Type#OBJECT} or {@link Type#WRAPPED}
     * @throws Throwable any exception or error thrown by the wrapped code
     */
    A getValue() throws Throwable;

    /**
     * Executes the code wrapped by this object.
     *
     * @throws UnsupportedOperationException if the type of this wrapper is not {@link Type#CALLABLE} or {@link Type#WRAPPED}
     * @throws Throwable any exception or error thrown by the wrapped code
     */
    void call() throws Throwable;

    /**
     * Wraps the given object.
     *
     * @param value the object to wrap
     * @return an {@link ActualWrapper} instance wrapping the given object
     * @param <A> type of the object to wrap
     */
    static <A> ActualWrapper<A> ofObject(A value) {
        return new ActualWrapperImpl<>(value);
    }

    /**
     * Wraps the given expression.
     *
     * @param expression the expression to wrap
     * @return an {@link ActualWrapper} instance wrapping the given expression
     * @param <A> type of the value returned by the given expression
     */
    static <A> ActualWrapper<A> ofExpression(Expression<A> expression) {
        return new ActualWrapperImpl<>(expression);
    }

    /**
     * Wraps the given statement.
     *
     * @param statement the statement to wrap
     * @return an {@link ActualWrapper} instance wrapping the given statement
     * @param <A> type of the value that would be returned by the given statement, just here to silence the compiler
     */
    static <A> ActualWrapper<A> ofStatement(Statement statement) {
        return new ActualWrapperImpl<>(statement);
    }

    /**
     * Functional interface for wrapping arbitrary code that returns a value.
     *
     * @param <A> type of the value returned by the functional method
     */
    @FunctionalInterface
    interface Expression<A> {

        /**
         * Executes the underlying code and returns its result.
         *
         * @return the result of invoking the underlying code
         * @throws Throwable any exception or error thrown by the code
         */
        A evaluate() throws Throwable;
    }

    /**
     * Functional interface for wrapping arbitrary code that does not return a value.
     */
    @FunctionalInterface
    interface Statement {

        /**
         * Executes the underlying code.
         *
         * @throws Throwable any exception or error thrown by the code
         */
        void evaluate() throws Throwable;
    }
}
