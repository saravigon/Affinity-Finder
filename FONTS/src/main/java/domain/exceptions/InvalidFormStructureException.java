package domain.exceptions;

public class InvalidFormStructureException extends FormException {
    public InvalidFormStructureException() {
        super("Form structure is incomplete: form must have at least one question.");
    }
    
    public InvalidFormStructureException(String detail) {
        super("Invalid form structure: " + detail);
    }
}