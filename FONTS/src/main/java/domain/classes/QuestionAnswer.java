
package domain.classes;
import java.util.ArrayList;

/**
 * Class representing an answer to a question, encapsulating the question type and the answer value
 */
public class QuestionAnswer{
    /**
     * The type of the question
     */
    private Question.QuestionType qType;
    /**
     * The answer value, can be Integer, String, or ArrayList of String based on question type
     */
    private Object answer;

    // ----------------------------------------
    // SETTERS
    // ----------------------------------------

    /**
     * Explicit creator for Question answer
     * @param qType The type of the question
     * @param answer The answer value
     */
    public QuestionAnswer(Question.QuestionType qType, Object answer){
        this.qType = qType;
        this.answer = answer;
        if(answer == null) return; //null is always allowed
        switch (qType) {
            case NUMERIC:
                if(!(answer instanceof Integer))
                    throw new IllegalArgumentException("Answer must be of type Integer for NUMERIC questions.");
                break;
            case OPEN_ENDED:
                if(!(answer instanceof String))
                    throw new IllegalArgumentException("Answer must be of type String for OPEN_ENDED questions.");
                if(answer.equals("")) this.answer = null; //if string is empty change it for null for consistency
                break;
            case MULTIPLE_CHOICE:
                if(!(answer instanceof ArrayList))
                    throw new IllegalArgumentException("Answer must be of type ArrayList<String> for MULTIPLE_CHOICE questions.");
                break;
            default:
                break;
        }
    }

    /**
     * Implicit creator for NUMERIC
     * @param answer The answer value
     */
    public QuestionAnswer(Integer answer){
        this.qType = Question.QuestionType.NUMERIC;
        this.answer = answer;
    }
    /**
     * Implicit creator for OPEN_ENDED
     * @param answer The answer value
     */

    public QuestionAnswer(String answer){
        this.qType = Question.QuestionType.OPEN_ENDED;
        this.answer = answer;
    }
    /**
     * Implicit creator for MULTIPLE_CHOICE
     * @param answer The answer value
     */

    public QuestionAnswer(ArrayList<String> answer){
        this.qType = Question.QuestionType.MULTIPLE_CHOICE;
        this.answer = answer;
    }

    // ----------------------------------------
    // GETTERS
    // ----------------------------------------
  
    /**
     * Getter for answer NUMERIC, returns the object casted into an Integer
     * Throws error if answer is not of type NUMERIC
     * @return Integer, can be null
     */
    public Integer getAnswerInteger() {
        if (qType != Question.QuestionType.NUMERIC) {
            throw new IllegalStateException("Answer is not of type NUMERIC.");
        }
        if (answer == null) {
            return null;
        }
        // Manage the case where answer might be a Double due to JSON deserialization
        if (answer instanceof Double) {
            return ((Double) answer).intValue();
        }
        return (Integer) answer;
    }

    /**
     * Getter for answer OPEN_ENDED, returns the object casted into a String
     * @return String, can be null
     */
    public String getAnswerString() {
        if (qType != Question.QuestionType.OPEN_ENDED) {
            throw new IllegalStateException("Answer is not of type OPEN_ENDED.");
        }
        return (String) answer;
    }

    /**
     * Getter for answer MULTIPLE_CHOICE, returns the object casted into an ArrayList of String
     * @return ArrayList of String, can be null
     */
    @SuppressWarnings("unchecked") //its safe because we check the type before casting, and being multiple choice means it has to be ArrayList<String>
    public ArrayList<String> getAnswerMultiple() {
        if (qType != Question.QuestionType.MULTIPLE_CHOICE) {
            throw new IllegalStateException("Answer is not of type MULTIPLE_CHOICE.");
        }
        return (ArrayList<String>) answer;
    }

    /**
     * General getter
     * @return the object as is
     */
    public Object getAnswerObject(){
        return answer;
    }
    /**
     * Getter for the questionType
     * @return A Question.QuestionType
     */
    public Question.QuestionType getQuestionType(){
        return qType;
    }

    /**
     * True if answer is null, else false
     * @return A boolean
     */
    public Boolean isUnAnswered(){
        return answer == null;
    }

}