package expression.exceptions;

public class DBZException extends EvaluateException {

    public DBZException(String message) {
        super(message);
    }

    public DBZException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Division by zero exception";
    }

}
