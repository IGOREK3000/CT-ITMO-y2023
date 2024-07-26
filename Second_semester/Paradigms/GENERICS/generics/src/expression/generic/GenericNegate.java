package expression.generic;

import expression.executors.Executor;

public class GenericNegate<T> extends UnaryOperation<T> {

    public GenericNegate(MainExpression<T> expression, Executor<T> executor) {
        super(expression, executor);
    }

    @Override
    protected T executeOperation(T x) {
        return executor.negate(x);
    }

    @Override
    protected String getOperation() {
        return "-";
    }
}
