package expression;

import java.util.List;

public class NotUnary extends UnaryOperation {
    public NotUnary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        return ~x;
    }

    @Override
    protected String getOperation() {
        return "~";
    }

}
