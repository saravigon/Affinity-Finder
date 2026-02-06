package domain.exceptions;

public class InvalidAnswerFile extends FormException {
    public InvalidAnswerFile() {
        super("Invalid Answer format.");
    }

    public InvalidAnswerFile(String msg) {
        super(msg);
    }
    
}

