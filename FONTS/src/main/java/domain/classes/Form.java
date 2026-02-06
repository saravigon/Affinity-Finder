package domain.classes;
import java.util.ArrayList;

import domain.exceptions.FormException;
import domain.exceptions.FormNotExecutedClustering;

/**
 * Class representing a form with questions and answers
 */
public class Form {

    /**
     * Unique Form ID
     */
    private int UFID;     
    /**
     * Title of the form
     */ 
    private String title;
    /**
     * Description of the form
     */
    private String description;
    /**
     * Unique identifier of the profile who created the form
     */
    private int UUID_creator;
    /**
     * List of questions in the form
     */
    private ArrayList<Question> questions;
    /**
     * List of affinity groups associated with the form
     */
    private ArrayList<AffinityGroup> affinityGroups;  
    /**
     * Kmeans clustering instance for the form
     */  
    private Kmeans kmeans;

    /**
     * Generates a unique ID for the form based on its title
     * @param title The title of the form
     * @return An integer representing the unique ID
     */
    private int generateUID(String title, String description){
        return title.hashCode()- Long.valueOf(System.currentTimeMillis()).hashCode();
    }

    /**
     * Constructor for the Form class
     * @param title The title of the form
     * @param description The description of the form
     * @param UUID_creator The unique identifier of the creator profile
     */
    public Form(String title,String description,int UUID_creator){
        setTitle(title);
        this.description = description;
        this.UUID_creator = UUID_creator;
        this.questions = new ArrayList<>();
        //this.responders = new ArrayList<>();
        this.affinityGroups = new ArrayList<>();
        this.UFID = generateUID(title, description);
        this.kmeans = null;
    }

    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    /**
     * Gets the title of the actual form
     * @return String title of the actual form
     */
    public String getTitle() {
        return title;
    } 

    /**
     * Gets the UFID of the actual form
     * @return int UFID of the actual form
     */
    public int getUFID() {
        return UFID;
    }

    /**
     * Gets the description of the actual form
     * @return String description of the actual form
     */
    public String getDescription() {
        return description;
    } 

    /**
     * Gets the creator UUID of the actual form
     * @return int creator UUID of the actual form
     */
    public int getCreatorUUID() {
        return UUID_creator;
    }
    
    /**
     * Gets the list of Questions of the actual form
     * @return An ArrayList of Question containing all the Questions created in the actual form
     */
    public ArrayList<Question> getQuestions(){
        return questions;
    }

    /**
     * Gets the list of QuestionType for all questions
     * @return ArrayList of questionTypes
     */
    public ArrayList<Question.QuestionType> getQuestionTypes(){
        ArrayList<Question.QuestionType> l = new ArrayList<> ();
        for(Question q : questions){
            l.add(q.getQuestionType());
        }
        return l;
    }

    /**
     * Gets the list of Questions' text of the actual form
     * @return An ArrayList of String containing all the Questions' text created in the actual form
     */
    public ArrayList<String> getQuestionsText(){
        ArrayList<String> s = new ArrayList<String>();
        for(Question q : questions){
            s.add(q.getQuestionText());
        }
        return s;
    }

    /**
     * Gets the list of Questions' text of the actual form without type info
     * @return
     */
    public ArrayList<String> getQuestionsNoType(){
        ArrayList<String> s = new ArrayList<String>();
        for(Question q : questions){
            s.add(q.getQuestion());
        }
        return s;
    }

    /**
     * Gets the number of questions in the form
     * @return int number of questions
     */
    public int getQuestionsCount(){
        return questions.size();
    }

    /**
     * Gets the list of Affinity Groups of the actual form
     * @return An ArrayList of AffinityGroup containing all the Affinity Groups created in the actual form
     */
    public ArrayList<AffinityGroup> getAffinityGroups(){
        return affinityGroups;
    }

    /**
     * Gets information of all questions in the form
     * @return ArrayList of question information
     */
    public ArrayList<Object> getQuestionInfo(){
        ArrayList<Object> l = new ArrayList<>();
        for(Question q : questions){
            l.add(q.getQuestionInfo());
        }
        return l;
    }


    // ---------------------------------------------------------
    // SETTERS
    // ---------------------------------------------------------

    /**
     * Sets the title of the actual form
     * @param t New title for the actual form
     */
    public void setTitle(String t){
        if(t == null || t.isEmpty()) t= "";
        this.title = t;
    }

    /**
     * Sets the description of the actual form
     * @param d New description for the actual form
     */
    public void setDescription(String d){
        this.description = d;
    }


    // ---------------------------------------------------------
    // ADDERS / DELETERS
    // ---------------------------------------------------------

    /**
     * Adds a question to the actual form
     * @param q Question to be added to the form
     */
    public void addQuestion(Question q){
        if (q!=null) questions.add(q);
    }

    /**
     * Deletes a question from the actual form based on its index
     * @param index Index of the question to be deleted
     */
    public void deleteQuestion(int index){
        //only if index is valid
        if (index >= 0 && index < questions.size()) questions.remove(index);
    }

    /**
     * Clears the Kmeans instance from the form
     */
    public void clearKmeans(){
        this.kmeans = null;
    }


    // ---------------------------------------------------------
    // KMEANS METHODS
    // ---------------------------------------------------------

    /**
     * Executes the K-means clustering algorithm on the form's answers, uses elbow method to set the best K
     * Creates and stores the resulting affinity groups in the form
     * @param answers The list of answers to the form
     */
    public void executeKmeansElbowMethod(ArrayList<Answer> answers) throws Exception {
        if(kmeans == null) kmeans = new Kmeans(this);
        ArrayList<AffinityGroup> ag = kmeans.createClustersElbowMethod(this,answers);
        this.affinityGroups = ag;
    }

    /**
     * Executes the K-means clustering algorithm on the form's answers, setting the number of clusters to K
     * Creates and stores the resulting affinity groups in the form
     * @param answers The list of answers to the form
     * @param k The number of clusters
     */
    public void executeKmeansSetK(ArrayList<Answer> answers, int k) throws Exception {
        if(kmeans == null) kmeans =  new Kmeans(this);
        kmeans.setK(k);
        ArrayList<AffinityGroup> ag = kmeans.createClustersSetK(this,answers);
        this.affinityGroups = ag;
    }

    /**
     * Evaluates the clustering of the affinity groups using the Silhouette method
     * @return double silhouette score of the clustering
     * @throws FormNotExecutedClustering if clustering has not been executed yet
     */
    public double evaluateCluster() throws FormException{
        if(affinityGroups.isEmpty() || kmeans == null) throw new FormNotExecutedClustering(this.UFID);
        else {
            return kmeans.evaluateClusteringSilhouete();
        }
    }

    /**
     * Creates the scatter chart for the answers clusters
     * @param answers
     */
    public void scatterChart(ArrayList<Answer> answers)throws Exception {
        if(kmeans == null) kmeans = new Kmeans(this);
        ScatterChart.scatterChart(this.questions, kmeans.getCleanAnswers(answers), this.affinityGroups);
    }

}    