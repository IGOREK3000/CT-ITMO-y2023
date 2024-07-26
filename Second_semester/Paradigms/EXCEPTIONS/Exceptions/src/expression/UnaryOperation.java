package expression;

import java.util.List;

public abstract class UnaryOperation implements MainExpression {

    protected final MainExpression expression;

    public UnaryOperation(MainExpression expression) {
        this.expression = expression;
    }

    protected abstract int executeOperation(int x);

    protected abstract String getOperation();

    @Override
    public int evaluate(int x) {
        return executeOperation(expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return executeOperation(expression.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return executeOperation(expression.evaluate(variables));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && this.getClass() == obj.getClass()) {
            return this.expression.equals(((UnaryOperation) obj).expression);
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

    @Override
    public String toMiniString() {
        if (expression.getClass() == Const.class || expression.getClass() == Variable.class
                || expression.getClass() == Negate.class || expression instanceof UnaryOperation) {
            return getOperation() + " " + expression.toMiniString();
        } else {
            return getOperation() + "(" + expression.toMiniString() + ")";
        }
    }
}