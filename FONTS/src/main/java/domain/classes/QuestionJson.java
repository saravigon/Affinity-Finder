package domain.classes;

import java.util.ArrayList;
/**
 * Class representing the JSON structure of a question
 */
public class QuestionJson {
    /**
     * Attribute
     * type: Type of the question
     */
    public String type;
    /**
     * Attribute
     * text: Text of the question
     */
    public String text;
    /**
     * Attribute
     * min: Minimum bound for NUMERIC questions
     */
    public Integer min;
    /**
     * Attribute
     * max: Maximum bound for NUMERIC questions
     */
    public Integer max;
    /**
     * Attribute
     * options: ArrayList of options for MULTIPLE_CHOICE questions
     */
    public ArrayList<String> options; 
    /**
     * Attribute
     * ordered: Indicates if the MULTIPLE_CHOICE question is ordered
     */
    public Boolean ordered;
    /**
     * Attribute
     * maxChoices: Maximum choices for MULTIPLE_CHOICE questions
     */
    public Integer maxChoices;
    /**
     * Empty constructor for JSON deserialization
     */
    public QuestionJson() {}
}