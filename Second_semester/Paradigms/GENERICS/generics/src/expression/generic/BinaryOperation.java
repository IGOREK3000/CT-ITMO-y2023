package expression.generic;

import expression.exceptions.EvaluateException;
import expression.executors.Executor;

public abstract class BinaryOperation<T> implements MainExpression<T> {
    private final MainExpression<T> firstExpression;
    private final MainExpression<T> secondExpression;
    protected final Executor<T> executor;

    public BinaryOperation(MainExpression<T> firstExpression, MainExpression<T> secondExpression, Executor<T> executor) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
        this.executor = executor;
    }

    @Override
    public String toString() {
        return "(" + firstExpression.toString() +
                " " + getOperation() + " "
                + secondExpression.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation<?> operation) {
            return firstExpression.equals(operation.firstExpression) &&
                    secondExpression.equals(operation.secondExpression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int PRIME_NUMBER = 2957;
        return getOperation().hashCode() + PRIME_NUMBER *
                (firstExpression.hashCode() + PRIME_NUMBER *
                        secondExpression.hashCode()) + 1;
    }

    protected abstract String getOperation();

    @Override
    public T evaluate(T x, T y, T z) throws EvaluateException {
        return executeOperation(firstExpression.evaluate(x, y, z), secondExpression.evaluate(x, y, z));
    }

    protected abstract T executeOperation(T x, T y) throws EvaluateException;

}
