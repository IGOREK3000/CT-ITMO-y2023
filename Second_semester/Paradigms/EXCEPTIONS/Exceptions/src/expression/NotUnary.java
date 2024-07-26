package expression;

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
