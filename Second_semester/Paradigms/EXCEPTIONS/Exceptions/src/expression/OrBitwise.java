package expression;

public class OrBitwise extends BinaryOperation {
    public OrBitwise(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "|";
    }

    @Override
    protected int executeOperation(int x, int y) {
        return x | y;
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return false;
    }

    @Override
    protected int getPriority() {
        return 5;
    }
}
