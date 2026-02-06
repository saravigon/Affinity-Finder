package domain.exceptions;

public class InvalidMaxChoicesException extends FormException {
    public InvalidMaxChoicesException() {
        super("The maxChoices field of the question  is incorrect");
    }
}