package domain.exceptions;

public class InvalidQuestionTypeException  extends FormException {
    public InvalidQuestionTypeException () {
        super("Question type invalid.");
    }
    public InvalidQuestionTypeException (String questionText) {
        super("Question type invalid for question: " + questionText);
    }
}
