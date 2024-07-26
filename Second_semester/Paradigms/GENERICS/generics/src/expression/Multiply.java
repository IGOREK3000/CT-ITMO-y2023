package expression;

public class Multiply extends BinaryOperation {

    public Multiply(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "*";
    }

    @Override
    protected int executeOperation(int x, int y) {
        return x * y;
    }


    @Override
    protected boolean hasEqualPriorityBrackets() {
        return false;
    }

    @Override
    protected int getPriority() {
        return 1;
    }


}
