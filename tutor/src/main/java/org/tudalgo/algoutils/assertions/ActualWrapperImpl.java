package org.tudalgo.algoutils.assertions;

/**
 * Basic implementation of {@link ActualWrapper}.
 *
 * @param <A> type of the wrapped value
 */
public class ActualWrapperImpl<A> implements ActualWrapper<A> {

    private final Type type;
    private final A actualObject;
    private final Expression<A> actualExpression;
    private final Statement actualStatement;

    /**
     * Constructs a new {@link ActualWrapper} for a plain Java object.
     *
     * @param actualObject the object to wrap
     */
    public ActualWrapperImpl(A actualObject) {
        this(Type.OBJECT, actualObject, null, null);
    }

    /**
     * Constructs a new {@link ActualWrapper} for an expression.
     *
     * @param actualExpression the expression to wrap
     */
    public ActualWrapperImpl(Expression<A> actualExpression) {
        this(Type.WRAPPED, null, actualExpression, null);
    }

    /**
     * Constructs a new {@link ActualWrapper} for a statement.
     *
     * @param actualStatement the statement to wrap
     */
    public ActualWrapperImpl(Statement actualStatement) {
        this(Type.CALLABLE, null, null, actualStatement);
    }

    private ActualWrapperImpl(Type type, A actualObject, Expression<A> actualExpression, Statement actualStatement) {
        this.type = type;
        this.actualObject = actualObject;
        this.actualExpression = actualExpression;
        this.actualStatement = actualStatement;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public A getValue() throws Throwable {
        return switch (this.type) {
            case OBJECT -> this.actualObject;
            case WRAPPED -> this.actualExpression.evaluate();
            default -> throw new UnsupportedOperationException("This operation is not supported on type " + this.type);
        };
    }

    @Override
    public void call() throws Throwable {
        switch (this.type) {
            case WRAPPED -> this.actualExpression.evaluate();
            case CALLABLE -> this.actualStatement.evaluate();
            default -> throw new UnsupportedOperationException("This operation is not supported on type " + this.type);
        }
    }
}
