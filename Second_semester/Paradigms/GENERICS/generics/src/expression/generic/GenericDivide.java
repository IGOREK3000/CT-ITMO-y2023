package expression.generic;

import expression.exceptions.EvaluateException;
import expression.executors.Executor;

public class GenericDivide<T> extends BinaryOperation<T> {
    public GenericDivide(MainExpression<T> firstExpression, MainExpression<T> secondExpression, Executor<T> executor) {
        super(firstExpression, secondExpression, executor);
    }

    @Override
    protected String getOperation() {
        return "/";
    }

    @Override
    protected T executeOperation(T x, T y) throws EvaluateException {
        return executor.divide(x, y);
    }

}
