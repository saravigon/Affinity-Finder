package domain.exceptions;

public class InvalidMultipleChoiceAnswerException extends FormException {
    public InvalidMultipleChoiceAnswerException(int maxAllowed, int providedCount) {
        super("Too many choices selected. Maximum allowed: " + maxAllowed + ", but " + providedCount + " were selected.");
    }
}