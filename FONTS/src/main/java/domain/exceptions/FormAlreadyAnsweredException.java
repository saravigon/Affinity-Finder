package domain.exceptions;

public class FormAlreadyAnsweredException extends FormException {
    public FormAlreadyAnsweredException(int formID) {
        super("Form " + formID + " has already been answered by you. Please modify your existing answers instead.");
    }
}
