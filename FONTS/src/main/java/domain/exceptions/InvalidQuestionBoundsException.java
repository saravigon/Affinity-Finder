package domain.exceptions;

public class InvalidQuestionBoundsException extends FormException {
    public InvalidQuestionBoundsException(int min, int max) {
        super("Invalid numeric bounds: minimum (" + min + ") must be less than maximum (" + max + ").");
    }
    
    public InvalidQuestionBoundsException(String questionText, int min, int max) {
        super("Question '" + questionText + "' has invalid numeric bounds: min=" + min + ", max=" + max);
    }
}