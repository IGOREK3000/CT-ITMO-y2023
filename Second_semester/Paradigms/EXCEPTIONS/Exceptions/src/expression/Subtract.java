package expression;

public class Subtract extends BinaryOperation {

    public Subtract(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "-";
    }

    @Override
    protected int executeOperation(int x, int y) {
        return x - y;
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return true;
    }

    @Override
    protected int getPriority() {
        return 2;
    }


}
