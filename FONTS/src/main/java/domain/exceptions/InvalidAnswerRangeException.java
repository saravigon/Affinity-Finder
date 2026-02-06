package domain.exceptions;

public class InvalidAnswerRangeException extends FormException {
    public InvalidAnswerRangeException(String questionText, int min, int max, int provided) {
        super("Question '" + questionText + "': expected answer between " + min + " and " + max + ", but received: " + provided);
    }
}