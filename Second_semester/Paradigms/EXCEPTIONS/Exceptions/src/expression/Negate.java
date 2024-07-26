package expression;

public class Negate extends UnaryOperation {

    public Negate(MainExpression expression) {
        super(expression);
    }

    @Override
    protected int executeOperation(int x) {
        return -x;
    }

    @Override
    protected String getOperation() {
        return "-";
    }


}
