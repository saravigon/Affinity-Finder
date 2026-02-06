package domain.exceptions;

public class OpenEndedAnswerTooLongException extends FormException {
    public OpenEndedAnswerTooLongException(String questionText, int actualLength) {
        super("Question '" + questionText + "' answer exceeds maximum length of 1000 characters (current: " + actualLength + ")");
    }
}
