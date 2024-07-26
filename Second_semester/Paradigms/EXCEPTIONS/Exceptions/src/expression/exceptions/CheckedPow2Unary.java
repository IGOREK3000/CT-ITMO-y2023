package expression.exceptions;

import expression.Const;
import expression.MainExpression;
import expression.UnaryOperation;

public class CheckedPow2Unary extends UnaryOperation {
    public CheckedPow2Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        // :NOTE: в статик
        if (x > (new CheckedLog2Unary(new Const(Integer.MAX_VALUE))).evaluate(1)) {
            throw new OverflowException("Pow2Unary");
        }
        if (x < 0) {
            throw new IncorrectArgumentException("pow2 (should be more than 0)");
        }
        return (int) (Math.pow(2, x));
    }

    @Override
    protected String getOperation() {
        return "pow2";
    }
}
