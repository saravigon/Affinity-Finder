package domain.exceptions;

public class InvalidChoicesException extends FormException {
    public InvalidChoicesException() {
        super("Multiple choice questions must have at least 2 choices.");
    }
}