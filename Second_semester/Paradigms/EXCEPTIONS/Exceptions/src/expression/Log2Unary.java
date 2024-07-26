package expression;

public class Log2Unary extends UnaryOperation {

    public Log2Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        return (int) (Math.log(x) / Math.log(2));
    }

    @Override
    protected String getOperation() {
        return "log2";
    }
}
