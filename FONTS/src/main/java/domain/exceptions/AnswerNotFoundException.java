package domain.exceptions;

public class AnswerNotFoundException extends FormException {
    public AnswerNotFoundException() {
        super("The requested answer was not found.");
    }

    public AnswerNotFoundException(int formId, int responderId) {
        super("No answer found for form " + formId + " by responder " + responderId + ".");
    }
}
