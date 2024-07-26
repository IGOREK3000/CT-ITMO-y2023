package expression.exceptions;

import expression.*;

public class CheckedAdd extends BinaryOperation {

    public CheckedAdd(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "+";
    }

    @Override
    protected int executeOperation(int x, int y) throws OverflowException {
        if (x > 0 && y > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException("add overflowed");
        }
        if (x < 0 && y < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException("add overflowed");
        }
        return x + y;
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return false;
    }

    @Override
    protected int getPriority() {
        return 2;
    }
}
