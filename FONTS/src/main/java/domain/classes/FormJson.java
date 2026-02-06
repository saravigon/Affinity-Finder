package domain.classes;

import java.util.List;
/**
 * Class representing the JSON structure of a Form
 */
public class FormJson {
    /**
     * Attribute
     * title: the title of the form
     */
    public String title;
    /**
     * Attribute
     * description: the description of the form
     */
    public String description;
    /**
     * Attribute
     * questions: list of questions in the form
     */
    public List<QuestionJson> questions;
    /**
     * Default constructor
     */
    public FormJson() {}
}