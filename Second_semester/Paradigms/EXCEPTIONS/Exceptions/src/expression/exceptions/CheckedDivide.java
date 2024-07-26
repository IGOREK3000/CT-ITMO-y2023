package expression.exceptions;

import expression.BinaryOperation;
import expression.MainExpression;

public class CheckedDivide extends BinaryOperation {
    public CheckedDivide(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "/";
    }

    @Override
    protected int executeOperation(int x, int y) throws EvaluateException {
        if (y == 0) {
            throw new DBZException("Exception for devision by zero");
        }
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("divide");
        }

        return x / y;
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return true;
    }

    @Override
    protected int getPriority() {
        return 1;
    }
}
