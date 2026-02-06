package domain.exceptions;

public class IdNotFoundException extends FormException {
    public IdNotFoundException() {
        super("The specified ID was not found.");
    }

    public IdNotFoundException(int id, String type) {
        super("The " + type + " with ID " + id + " doesn't exist.");
    }
    
}
