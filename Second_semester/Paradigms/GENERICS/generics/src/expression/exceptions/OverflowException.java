package expression.exceptions;

public class OverflowException extends EvaluateException {

    public OverflowException(String message) {
        super(message);
    }

    public OverflowException (String message, Exception e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Overflow exception caused by: " + super.getMessage();
    }

}

