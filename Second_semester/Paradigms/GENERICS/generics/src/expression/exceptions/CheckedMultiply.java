package expression.exceptions;

import expression.*;

public class CheckedMultiply extends BinaryOperation {
    public CheckedMultiply(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "*";
    }

    @Override
    protected int executeOperation(int x, int y) throws OverflowException {
        if (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE) {
            if ((y != 0 && y != 1) && (x != 0 && x != 1)) {
                throw new OverflowException("multiply");
            }
        } else if (x != 0 && y != 0) {
            if (((x > 0 && y < 0) && (y < Integer.MIN_VALUE / x)) ||
                    ((x < 0 && y > 0) && (x < Integer.MIN_VALUE / y)) ||
                    ((x > 0 && y > 0) && (x > Integer.MAX_VALUE / y)) ||
                    ((x < 0 && y < 0) && (x < Integer.MAX_VALUE / y))) {
                throw new OverflowException("multiply");
            }
        }
        return x * y;
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return false;
    }

    @Override
    protected int getPriority() {
        return 1;
    }
}
