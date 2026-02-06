package domain.exceptions;

public class InvalidQuestionTextException extends FormException {
    public InvalidQuestionTextException() {
        super("Question text cannot be null or empty.");
    }
}