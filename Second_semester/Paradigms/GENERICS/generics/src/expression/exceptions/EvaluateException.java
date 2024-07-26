package expression.exceptions;

public class EvaluateException extends RuntimeException {

    public EvaluateException (String message) {
        super(message);
    }

    public EvaluateException (String message, Exception e) {
        super(message, e);
    }


}
