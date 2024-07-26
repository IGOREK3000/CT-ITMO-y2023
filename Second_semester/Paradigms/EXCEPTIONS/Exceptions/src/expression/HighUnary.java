package expression;

public class HighUnary extends UnaryOperation {
    public HighUnary(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        if (x == 0) {
            return 0;
        }

        int count = 0;
        int mask = Integer.MIN_VALUE;

        while ((x & mask) == 0) {
            mask >>>= 1;
            count++;
        }

        return 1 << (31 - count);

    }

    @Override
    protected String getOperation() {
        return "high";
    }
}
