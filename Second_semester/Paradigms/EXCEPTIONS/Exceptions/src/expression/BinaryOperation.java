package expression;

import expression.exceptions.EvaluateException;

import java.util.List;

public abstract class BinaryOperation implements MainExpression {
    private final MainExpression firstExpression;
    private final MainExpression secondExpression;

    public BinaryOperation(MainExpression firstExpression, MainExpression secondExpression) {
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    @Override
    public String toString() {
        return "(" + firstExpression.toString() +
                " " + getOperation() + " "
                + secondExpression.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation operation) {
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

    protected abstract int executeOperation(int x, int y) throws EvaluateException;

    @Override
    public int evaluate(int x) throws EvaluateException {
        return executeOperation(firstExpression.evaluate(x), secondExpression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) throws EvaluateException {
        return executeOperation(firstExpression.evaluate(x, y, z), secondExpression.evaluate(x, y, z));
    }

    @Override
    public int evaluate(List<Integer> variables) {
        return executeOperation(firstExpression.evaluate(variables), secondExpression.evaluate(variables));
    }

    private boolean hasLeftBrackets() {
        if (firstExpression instanceof BinaryOperation) {
            return ((BinaryOperation) firstExpression).getPriority() > getPriority();
        } else {
            return false;
        }
    }

    private boolean hasRightBrackets() {
        if (secondExpression instanceof BinaryOperation binaryExpression) {
            int priority = binaryExpression.getPriority();
            return priority > getPriority() ||
                    (priority == getPriority() && (hasEqualPriorityBrackets() ||
                            (getOperation().equals("*") && binaryExpression.getOperation().equals("/"))));
        } else {
            return false;
        }
    }

    protected abstract boolean hasEqualPriorityBrackets();

    protected abstract int getPriority();

    @Override
    public String toMiniString() {
        String expression = "";
        if (hasLeftBrackets()) {
            expression += "(" + firstExpression.toMiniString() + ") " + getOperation() + " ";
        } else {
            expression += firstExpression.toMiniString() + " " + getOperation() + " ";
        }

        if (hasRightBrackets()) {
            expression += "(" + secondExpression.toMiniString() + ")";
        } else {
            expression += secondExpression.toMiniString();
        }
        return expression;
    }
}
