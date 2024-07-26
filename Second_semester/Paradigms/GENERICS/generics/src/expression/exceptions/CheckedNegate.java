package expression.exceptions;

import expression.*;

public class CheckedNegate extends UnaryOperation {

    public CheckedNegate (MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Negate overflowed");
        }
        return -x;
    }

    @Override
    protected String getOperation() {
        return "-";
    }
}
