package expression.generic;

import expression.executors.Executor;

public abstract class UnaryOperation<T> implements MainExpression<T> {

    protected final MainExpression<T> expression;

    protected final Executor<T> executor;

    public UnaryOperation(MainExpression<T> expression, Executor<T> executor) {
        this.expression = expression;
        this.executor = executor;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return executeOperation(expression.evaluate(x, y, z));
    }

    protected abstract T executeOperation(T x);

    protected abstract String getOperation();


    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return this.expression.equals(((UnaryOperation<?>) obj).expression);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getOperation().hashCode() + expression.hashCode();
    }

    @Override
    public String toString() {
        return getOperation() + "(" + expression.toString() + ")";
    }

}