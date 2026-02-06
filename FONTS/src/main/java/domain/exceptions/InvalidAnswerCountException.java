package domain.exceptions;

public class InvalidAnswerCountException extends FormException {
    public InvalidAnswerCountException(int expected, int given) {
        super("Number incorrect of answers, expected: " + expected + " given: " + given + ".");
    }
}

