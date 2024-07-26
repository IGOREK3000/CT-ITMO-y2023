package expression.generic;

import expression.executors.Executor;

public class GenericSubtract<T> extends BinaryOperation<T> {
    public GenericSubtract(MainExpression<T> firstExpression, MainExpression<T> secondExpression, Executor<T> executor) {
        super(firstExpression, secondExpression, executor);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

    @Override
    protected T executeOperation(T x, T y)  {
        return executor.subtract(x, y);
    }

}
