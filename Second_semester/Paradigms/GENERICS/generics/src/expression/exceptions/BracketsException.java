package expression.exceptions;

public class BracketsException extends ParsingException {
    public BracketsException(String message) {
        super(message);
    }

    public BracketsException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
