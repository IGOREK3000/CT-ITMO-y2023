package expression.exceptions;

import expression.*;

public class CheckedSubtract extends BinaryOperation {
    public CheckedSubtract(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

    @Override
    protected int executeOperation(int x, int y) throws OverflowException {
        if (y == Integer.MIN_VALUE) {
            if (x >= 0) {
                throw new OverflowException("subtract overflowed");
            }
            if (x == Integer.MIN_VALUE) {
                return 0;
            }
        }

        if ((y > 0 && x < Integer.MIN_VALUE + y) || (y < 0 && x > Integer.MAX_VALUE + y)) {
            throw new OverflowException("subtract overflowed");
        }
        return x - y;
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
