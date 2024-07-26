package expression.exceptions;

import expression.MainExpression;
import expression.UnaryOperation;

public class CheckedLog2Unary extends UnaryOperation  {
    public CheckedLog2Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        if (x <= 0) {
            throw new IncorrectArgumentException("log2 (must be more than 0)");
        }
        return (int) (Math.log(x)/Math.log(2));
    }

    @Override
    protected String getOperation() {
        return "log2";
    }
}
