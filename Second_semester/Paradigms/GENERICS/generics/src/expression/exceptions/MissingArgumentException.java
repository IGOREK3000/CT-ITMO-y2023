package expression.exceptions;

public class MissingArgumentException extends ParsingException {

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(String message, Exception e) {
        super(message, e);
    }

    @Override
    public String getMessage() {
        return "Missing argument for: " + super.getMessage();
    }
}
