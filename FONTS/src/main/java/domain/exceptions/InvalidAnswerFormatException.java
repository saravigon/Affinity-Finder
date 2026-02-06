package domain.exceptions;

public class InvalidAnswerFormatException extends FormException {
    public InvalidAnswerFormatException(String questionText, String providedValue) {
        super("Question '" + questionText + "' expects a numeric answer, but received: '" + providedValue + "'");
    }
}