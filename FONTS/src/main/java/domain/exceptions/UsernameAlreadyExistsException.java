package domain.exceptions;

public class UsernameAlreadyExistsException extends Exception {
    public UsernameAlreadyExistsException() {
        super("A user with this username already exists.");
    }

    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
