package expression;

public class L1Unary extends UnaryOperation {

    public L1Unary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        int count = 0;
        int mask = Integer.MIN_VALUE;
        while ((x & mask) != 0) {
            count++;
            mask >>>= 1;
        }
        return count;
    }

    @Override
    protected String getOperation() {
        return "l1";
    }
}
