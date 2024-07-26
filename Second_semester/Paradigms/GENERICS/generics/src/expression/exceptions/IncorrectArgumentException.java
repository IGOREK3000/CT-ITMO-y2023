package expression.exceptions;

public class IncorrectArgumentException extends EvaluateException {

    public IncorrectArgumentException(String message) {
        super(message);
    }

    public IncorrectArgumentException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Incorrect argument for: " + super.getMessage();
    }

}
