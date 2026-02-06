package domain.exceptions;

public class InvalidAnswerChoiceException extends FormException {
    public InvalidAnswerChoiceException(String questionText, String invalidChoice) {
        super("Question '" + questionText + "': choice '" + invalidChoice);
    }
}