package domain.exceptions;

public class InvalidFormFile extends FormException {
    public InvalidFormFile() {
        super("Invalid file format.");
    }

    public InvalidFormFile(String msg) {
        super(msg);
    }
    
}

