package expression;

public class Pow2Unary extends UnaryOperation {
    public Pow2Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        return (int) (Math.pow(2, x));
    }

    @Override
    protected String getOperation() {
        return "pow2";
    }
}
