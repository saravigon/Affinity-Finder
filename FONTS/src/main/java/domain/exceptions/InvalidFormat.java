package domain.exceptions;

public class InvalidFormat extends Exception {
    public InvalidFormat() {
        super("This format is not accepted.");
    }

    public InvalidFormat(String msg) {
        super(msg);
    }
}
