package expression;

public class T1Unary extends UnaryOperation {
    public T1Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        int count = 0;
        while ((x & 1) == 1) {
            x = x >>> 1;
            count++;
        }
        return count;
    }

    @Override
    protected String getOperation() {
        return "t1";
    }
}
