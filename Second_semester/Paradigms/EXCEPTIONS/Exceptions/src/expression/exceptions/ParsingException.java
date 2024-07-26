package expression.exceptions;

// :NOTE: проверяемые исключения
public class ParsingException extends Exception {
    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable e) {
        super(message, e);
    }

}
