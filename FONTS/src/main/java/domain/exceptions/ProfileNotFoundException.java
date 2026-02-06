package domain.exceptions;

public class ProfileNotFoundException extends Exception {
    public ProfileNotFoundException() {
        super("Profile not found.");
    }

    public ProfileNotFoundException(String msg) {
        super(msg);
    }
}


