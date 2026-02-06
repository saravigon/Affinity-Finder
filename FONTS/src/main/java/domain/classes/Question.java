package domain.classes;
import java.util.ArrayList;

/**
 * Class representing a question in a form
 */
public class Question {
    // Data
    /**
     * Enum representing the type of question
     */
    public enum QuestionType {
        MULTIPLE_CHOICE, //escolliu una d'aquestes opcions o com a màxim n opcions
        OPEN_ENDED, //donar una resposta com un string en format lliure
        NUMERIC,   //donae una quantitat numerica
    }
    /**
     * Attribute
     * questionText: The text of the question
     */
    private String questionText;
    /**
     * Attribute
     * choices: List of choices for MULTIPLE_CHOICE questions
     */
    private ArrayList<String> choices; 
    /**
     * Attribute
     * questionType: The type of the question
     */
    private QuestionType questionType;
    /**
     * Attributes
     * maxChoices: Maximum choices for MULTIPLE_CHOICE question
     * minBound: Minimum bound for NUMERIC question
     * maxBound: Maximum bound for NUMERIC question
     */
    private Integer maxChoices,minBound,maxBound;
    /**
     * Attribute
     * order: Indicates if the question is qualitative ordered or unordered
     */
    private Boolean order;
    // Constructor
    /**
     * General constructor for the Question class, all unused parameters are null
     * @param questionText The text of the question
     * @param questionType The type of the question
     * @param maxChoices Maximum choices for MULTIPLE_CHOICE question
     * @param choices Available choices for MULTIPLE_CHOICE question
     * @param order If it's a qualitative ordered question, or a a qualitative unordered question
     * @param minBound The minimum bound for a NUMERICAL question
     * @param maxBound The maximum bound for a NUMERICAL question
     */
    public Question (String questionText, QuestionType questionType, Integer maxChoices, ArrayList<String> choices, Boolean order, Integer minBound, Integer maxBound){
        this.questionText = questionText;
        this.questionType = questionType;
        this.maxChoices = maxChoices;
        this.choices = choices;
        this.order = order;
        this.minBound = minBound;
        this.maxBound = maxBound;
    }    

    //Getters
    /**
     * Gets the text of the question
     * @return String question text
     */
    public String getQuestionText() {
        String s = "";
        switch (questionType) {
            case MULTIPLE_CHOICE:
                s = "Multiple Choice Question: "+  questionText + "\n";
                s = s + String.join(", ", choices) + "\n";
                s = s + "MaxChoices: " + maxChoices + "\n";
                s = s + "Enter your response separated by ', ' ";
                return s;
            case NUMERIC:
                s = "Numeric Question: " + questionText + "\n";
                if (minBound != null && maxBound != null) {
                    s += "  [Rango: " + minBound + " - " + maxBound + "]"+ "\n";
                }
                s+= "Enter your response (number): \n";
                return s;
            case OPEN_ENDED:
                s = "Open Ended Question: " + questionText + "\n" + "Enter your response (text): ";
                return s;
            default:
                return s;
        }
    }
    /**
     * Gets the type of the question
     * @return QuestionType question type
     */
    public QuestionType getQuestionType() {
        return questionType;
    }

    public String getQuestion() {
        return questionText;
    }

    /**
     * Retruns the information relevant for each type of question, useful for Kmeans
     * @return (double) range or (arraylist of String) choices depending of the question type
     */

    public Object getQuestionInfo(){
        switch (questionType) {
            case NUMERIC:
                return (double)(maxBound - minBound);
            case MULTIPLE_CHOICE:
                return choices;
            
            default:
                return null;
        }
    }
    /**
     * Returns the maximum number of choices for a MULTIPLE_CHOICE question
     * @return integer representing max number of choices
     */
    public Integer getMaxChoices(){
        return maxChoices;
    }

    /**
     * Returns list of all available choices for a MULTIPLE_CHOICE question
     * @return ArrayList of String choices
     */
    public ArrayList<String> getChoices(){
        return choices;
    }

    /**
     * Returns the maximum for NUMERICAL questions
     * @return Integer for maximum
     */
    public Integer getMaxBound() {
        return maxBound;
    }
    /**
     * Returns the minimum for NUMERICAL questions
     * @return Integer for minimum
     */
    public Integer getMinBound(){
        return minBound;
    }

    /**
     * Gets the numerical range of correct answers
     * @return (int) range
     */
    public Integer getRange(){
        return maxBound - minBound;
    }

    /**
     * Gets if question is of multiple_choice order or multiple choice unorder
     * @return A boolean that indicates if it's order
     */
    public Boolean isOrder(){
        return order;
    }

    //Setters
    /**
     * Sets the text of the question
     * @param text The new text for the question
     */
    public void setQuestionText(String text){
        this.questionText = text;
    } 
}
