package domain.exceptions;

public class FormTitleInvalid extends FormException {
    public FormTitleInvalid() {
        super("The title is invalid.");
    }
    public FormTitleInvalid(String msg) {
        super(msg);
    }
}
