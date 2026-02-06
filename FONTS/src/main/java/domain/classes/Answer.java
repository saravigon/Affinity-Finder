package domain.classes;

import java.util.ArrayList;

/**
 * Class representing an answer to a form
 */
public class Answer {
    //Data
    /**
     * Attribute
     * responderUUID: Unique identifier of the profile who answered
     */
    private int responderUUID;
    /**
     * Attribute
     * formUFID: Unique identifier of the form being answered
     */
    private int formUFID;
    /**
     * Attribute
     * answer: List of QuestionAnswer objects representing the answers given
     */
    private ArrayList<QuestionAnswer> answer;

    //Constructor
    /**
     * Constructor of the Answer class
     * @param responderUUID The unique identifier of the responder
     * @param formUFID The unique form ID being answered
     * @param answer The list of QuestionAnswer objects representing the answers given
     */
    public Answer(int responderUUID, int formUFID, ArrayList<QuestionAnswer> answer){// QuestionType questionType) {
        this.responderUUID = responderUUID;
        this.formUFID = formUFID;
        this.answer = answer;
        //this.questionType = questionType;
    }
    
    /**
     * Constructor of the Answer class without answers
     * @param responderUUID The unique identifier of the responder
     * @param formUFID The unique form ID being answered
     */
    public Answer(int responderUUID, int formUFID){
        this.responderUUID = responderUUID;
        this.formUFID = formUFID;
        this.answer = new ArrayList<QuestionAnswer>();
    }
    
    /**
     * Adds a QuestionAnswer to the answer list
     * @param qa The QuestionAnswer object to be added
     */
    public void addAnswer(QuestionAnswer qa){
        answer.add(qa);
    }

    //GETTERS
    /**
     * Gets the list of QuestionAnswer objects
     * @return ArrayList of QuestionAnswer containing all answers
     */
    public ArrayList<QuestionAnswer> getAnswer(){
        return answer;
    }

    /**
     * Gets a specific QuestionAnswer by index
     * @param i The index of the QuestionAnswer to retrieve
     * @return QuestionAnswer at the specified index
     */
    public QuestionAnswer getQuestionAnswer(int i){
        if(i > answer.size()) return null; //throw error
        return answer.get(i);
    }

    /**
     * Gets the responder's unique identifier
     * @return int responderUUID
     */
    public int getResponderUUID(){
        return responderUUID;
    }

    /**
     * Gets the unique form ID being answered
     * @return int formUFID
     */
    public int getFormUFID(){
        return formUFID;
    }

}
