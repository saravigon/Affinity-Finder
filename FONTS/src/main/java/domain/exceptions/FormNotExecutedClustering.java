package domain.exceptions;

public class FormNotExecutedClustering extends FormException {
    public FormNotExecutedClustering(int formID) {
        super("Form " + formID + " has not had clustering executed yet. Please execute clustering before accessing this feature.");
    }
}
