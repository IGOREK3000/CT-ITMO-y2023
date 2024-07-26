package expression.generic;

import expression.exceptions.IncorrectArgumentException;

public class ExecutorCreator {
    public static Executor create(String operation) {
        if (operation.equals("+")) {
            return new AddExecutor();
        } else if (operation.equals("-")) {
            return new SubtractExecutor();
        } else if (operation.equals("*")) {
            return new MultiplyExecutor();
        } else if (operation.equals("/")) {
            return new DivideExecutor();
        } else {
            throw new IncorrectArgumentException("Illegal operation: " + operation);
        }
    }
}
