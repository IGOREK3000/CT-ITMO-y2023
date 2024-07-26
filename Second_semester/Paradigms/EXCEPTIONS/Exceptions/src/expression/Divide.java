package expression;

public class Divide extends BinaryOperation {

    public Divide(MainExpression firstExpression, MainExpression secondExpression) {
        super(firstExpression, secondExpression);
    }

    @Override
    protected String getOperation() {
        return "/";
    }

    @Override
    protected int executeOperation(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("Division by zero is not allowed (class expression.Divide)");
        } else {
            return x / y;
        }
    }

    @Override
    protected boolean hasEqualPriorityBrackets() {
        return true;
    }

    @Override
    protected int getPriority() {
        return 1;
    }
}
