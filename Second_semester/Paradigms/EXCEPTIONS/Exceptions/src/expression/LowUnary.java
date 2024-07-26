package expression;

public class LowUnary extends UnaryOperation {
    public LowUnary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        return x & -x;
    }

    @Override
    protected String getOperation() {
        return "low";
    }
}
