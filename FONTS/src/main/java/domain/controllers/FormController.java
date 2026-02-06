
package domain.controllers;

import domain.classes.*;
import domain.classes.Question.*;
import domain.exceptions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*; 
import com.google.gson.*;

/**
 * Controller class for managing forms, questions, and answers
 */
public class FormController {

    
    /**
     * Attribute Profile activeProfile: The currently active profile using the controller
     */
    private Profile activeProfile;
    /** 
     * Attribute DataManager dataManager: The persistence controller used for data management
     */
    private DataManager dataManager;
    
    /**
     * FormController constructor (needed on tests with mocks)
     * @param profile The active profile
     * @param p The DataManager to be used
     */
    public FormController(Profile profile, DataManager p) {
        this.activeProfile = profile;
        this.dataManager = p;
    }   
    /**
     * FormController constructor
     * @param ID The unique identifier of the active profile
     */
    public FormController(int ID){
        dataManager = DataManager.getInstance();
        activeProfile = dataManager.getProfile(ID);
    }
    /**
     * Default constructor of FormController
     */
    public FormController(){
        activeProfile = new Profile("1", "1"); // per exemple, canviar
        dataManager = DataManager.getInstance();
    }

    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    /**
     * Gets the active profile
     * @return Profile active profile
     */ 
    public Profile getActiveProfile(){
        return activeProfile;
    }


    /**
     * Gets a hash map containing the existing forms
     * @return Calls a function in DataManager to get the forms and returns it, as a HashMap with UFID as key and Form as value
     */
    public HashMap <Integer,Form> getExistingForms(){
        return dataManager.getForms();
    }

    /**
     * Gets a form by its UFID
     * @param UFID The unique identifier of the form
     * @return Calls a function in DataManager to get the form and returns it
     * @throws IdNotFoundException if the form does not exist
     */
    public Form getForm(int UFID) throws IdNotFoundException {
        Form form = dataManager.getForm(UFID);
        if(form == null) throw new IdNotFoundException(UFID, "Form");
        return form;
    }

    /**
     * Gets a form by its title
     * @param title The title of the form
     * @return Calls a function in DataManager to get the form and returns it
     */
    public Form getFormByTitle(String title){
        return dataManager.getFormbyTitle(title);
    }

    /**
     * Devuelve una lista de preguntas con metadatos (tipo, opciones, bounds, maxChoices) en formato String para mostrar en el driver
     * @param formID El identificador único del formulario
     * @return Un ArrayList de Strings, cada uno describe la pregunta y sus metadatos
     * @throws FormException si hay un error al recuperar las preguntas (IdNotFoundException)
     */
    public ArrayList<String> getQuestionsWithMetadataFromForm(int formID) throws FormException {
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");
        ArrayList<Question> questions = form.getQuestions();
        ArrayList<String> result = new ArrayList<>();
        for (Question q : questions) {
            StringBuilder sb = new StringBuilder();
            sb.append(q.getQuestionText());
            switch (q.getQuestionType()) {
                case MULTIPLE_CHOICE:
                    sb.append("\n  [Opciones: ");
                    sb.append(String.join(", ", q.getChoices()));
                    sb.append("]");
                    sb.append("  [Máx opciones: ").append(q.getMaxChoices()).append("]");
                    if (q.isOrder() != null) sb.append("  [Ordenadas: ").append(q.isOrder() ? "sí" : "no").append("]");
                    break;
                case NUMERIC:
                    sb.append("\n  [Rango: ").append(q.getMinBound()).append(" - ").append(q.getMaxBound()).append("]");
                    break;
                case OPEN_ENDED:
                    sb.append("\n  [Respuesta abierta]");
                    break;
            }
            result.add(sb.toString());
        }
        return result;
    }


    // Get user's existing answers for a form
    public Answer getUserAnswers(int formId, int userId) throws Exception {
        // Return list of answer strings for this user and form
        return dataManager.getUserAnswers(formId, userId);
    }

    // ---------------------------------------------------------
    // EXISTS
    // ---------------------------------------------------------

    /**
     * Checks if a form exists by its UFID
     * @param UFID The unique identifier of the form
     * @return Calls a function in DataManager to check existence and returns the result
     */
    public Boolean existsForm(int UFID){
        return dataManager.existsForm(UFID);
    }

    /**
     * Checks if a form exists by its title
     * @param title The title of the form
     * @return Calls a function in DataManager to check existence and returns the result
     */
    public Boolean existsFormByTitle(String title){
        return dataManager.existsForm(title);
    }

    // ---------------------------------------------------------
    // SETTERS AND MODIFIERS
    // ---------------------------------------------------------

    /**
     * Creates a new form
     * @param title The title of the form
     * @param description The description of the form
     * @throws FormTitleInvalid if there was an error creating the form (FormTitleInvalid())
     */
    public void createNewForm(String title, String description) throws FormTitleInvalid {
        Profile creator = getActiveProfile();
        // Validate title
        if (dataManager.existsForm(title) || title == null || title.isEmpty()) {
            throw new FormTitleInvalid();
        }

        // Create form because the title does NOT exist
        dataManager.addNewForm(title, description, creator);
    }

   
    /**
     * Creates a new question
     * @param qt The text of the question
     * @param type The type of the question
     * @return The created Question
     */
    public Question newQuest(String qt, Question.QuestionType type, Integer maxChoices, ArrayList<String> choices, Boolean order, Integer minBound, Integer maxBound) {
        Question q = new Question(qt,type,maxChoices, choices, order, minBound, maxBound);
        return q;
    }



    // ---------------------------------------------------------
    // FORM MODIFIERS
    // ---------------------------------------------------------

    /**
     * Deletes a question from a form
     * @param UFID The unique identifier of the form
     * @param questionIndex The index of the question to be deleted in the form's question list
     * @throws FormException if there was an error deleting the question (IdNotFoundException)
     */
    public void deleteQuestion(int UFID, int questionIndex) throws FormException {
        Form form = dataManager.getForm(UFID);
        if (form == null) throw new IdNotFoundException(UFID, "Form");
        form.deleteQuestion(questionIndex);
        //form.clearResponders();
        dataManager.updateForm(form);
        dataManager.deleteAnswersOfForm(UFID);
    }

    /**
     * Modifies a question's text in a form
     * @param UFID The unique identifier of the form
     * @param questionIndex The index of the question to be modified in the form's question list
     * @param newText The new text for the question
     * @throws FormException if there was an error modifying the question (IdNotFoundException)
     */
    public void modfyQuestion(int UFID, int questionIndex, String newText) throws FormException { 
        Form form = dataManager.getForm(UFID);
        if (form==null) throw new IdNotFoundException(UFID, "Form");
        
        ArrayList<Question> questions = form.getQuestions();
        if (questionIndex < 0 || questionIndex >= questions.size()) {
            throw new IdNotFoundException(questionIndex, "Question index");
        }
        
        Question q = questions.get(questionIndex);
        q.setQuestionText(newText);
        //form.clearResponders();
        dataManager.updateForm(form);
        dataManager.deleteAnswersOfForm(UFID);
    }

    /**
     * Changes the title of a form
     * @param UFID The unique identifier of the form
     * @param newTitle The new title for the form
     * @throws FormException if there was an error changing the title (IdNotFoundException, FormTitleInvalid)
     */
    public void changeTitle(int UFID, String newTitle) throws FormException {
        Form form = dataManager.getForm(UFID);
        if (form == null) throw new IdNotFoundException(UFID, "Form");
        
        // Validate that the new title is not empty
        if (newTitle == null || newTitle.isEmpty()) {
            throw new FormTitleInvalid("Title cannot be empty");
        }
        
        // Validate that the new title is not already in use by ANOTHER form
        if (dataManager.existsForm(newTitle)) {
            Form existingForm = dataManager.getFormbyTitle(newTitle);
            // If a form with that title exists and it is NOT the same one we are modifying
            if (existingForm != null && existingForm.getUFID() != UFID) {
                throw new FormTitleInvalid("Title already exists");
            }
        }
        
        form.setTitle(newTitle);
        dataManager.updateForm(form);
    }

    /**
     * Changes the description of a form
     * @param UFID The unique identifier of the form
     * @param newDescription The new description for the form
     * @throws FormException if there was an error changing the description (IdNotFoundException)
     */
    public void changeDescription(int UFID, String newDescription) throws FormException {
        Form form = dataManager.getForm(UFID);
        if (form == null) throw new IdNotFoundException(UFID, "Form");
        form.setDescription(newDescription);
        dataManager.updateForm(form);

    }

    /**
     * Deletes a form by its UFID
     * @param UFID The unique identifier of the form
     * @return Calls a function in DataManager to delete the form and returns the result. True if deleted successfully, false otherwise.
     * @throws FormException if there was an error deleting the form (IdNotFoundException)
     */
    public boolean deleteForm(int UFID) throws FormException {
        if (dataManager.deleteForm(UFID)==false) throw new IdNotFoundException(UFID, "Form");
        else return true;
    }


    /**
     * Registers that the active profile has answered a form
     * @param formID The unique identifier of the form
     * @throws FormException if there was an error registering the answered form (IdNotFoundException)
     */
    public void newAnsweredForm(int formID) throws FormException {
        Profile p = getActiveProfile();
        Form f = dataManager.getForm(formID);
        if (f==null) throw new IdNotFoundException(formID, "Form");
        // Prevent adding duplicate answered entries for the same form
        ArrayList<Integer> answeredForms = p.getAnsweredForms();
        for (Integer answered : answeredForms) {
            if (answered == formID) {
                throw new FormAlreadyAnsweredException(formID);
            }
        }
        p.addAnsweredForm(f.getUFID());
    }

    /**
     * Answers a form
     * @param formID The unique identifier of the form
     * @param usrID The unique identifier of the user answering the form
     * @param allAnswers An ArrayList of Strings containing all answers to the form's questions
     * @throws FormException if there was an error answering the form (IdNotFoundException, InvalidAnswerCountException, InvalidMultipleChoiceAnswerException, InvalidAnswerChoiceException, InvalidAnswerFormatException, InvalidAnswerRangeException)
     */
    public void answerForm(int formID, int usrID, ArrayList<String> allAnswers) throws FormException {
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");
        if(!dataManager.existsUser(usrID)) throw new IdNotFoundException(usrID, "User");
        Profile p = dataManager.getProfile(usrID);
        if (hasUserAnsweredForm(formID, p) && dataManager.hasAnswer(formID, usrID)) {
            throw new FormAlreadyAnsweredException(formID);
        }

        ArrayList<Question> questions = form.getQuestions();
        
        if (questions.size() != allAnswers.size()) {
            throw new InvalidAnswerCountException(questions.size(), allAnswers.size());
        }

        ArrayList <QuestionAnswer> qa = new ArrayList<QuestionAnswer>();
        for(int i = 0; i < questions.size(); i++){

            // Normalize the raw answer string; treat null as empty
            String raw = allAnswers.get(i) == null ? "" : allAnswers.get(i).trim();

            switch(questions.get(i).getQuestionType()){
                case MULTIPLE_CHOICE :
                    ArrayList<String> a;
                    if (raw.isEmpty()) {
                        // empty means "no selection" -> keep as empty list
                        a = new ArrayList<>();
                    } else {
                        // Split and convert to ArrayList<String>
                        String[] splitAnswers = raw.split(", ");
                        a = new ArrayList<>(Arrays.asList(splitAnswers));
                    }

                    Integer maxVal = questions.get(i).getMaxChoices();
                    int max = (maxVal == null ? 0 : maxVal);
                    if (a.size() > max) {
                        throw new InvalidMultipleChoiceAnswerException(max, a.size());
                    }

                    // Validate each provided choice exists in the question (skip if user left it empty)
                    ArrayList<String> availableChoices = questions.get(i).getChoices();
                    for (String choice : a) {
                        if (!availableChoices.contains(choice)) {
                            throw new InvalidAnswerChoiceException(questions.get(i).getQuestionText(), choice);
                        }
                    }

                    QuestionAnswer qmc = new QuestionAnswer(QuestionType.MULTIPLE_CHOICE, a);
                    qa.add(qmc);
                    break;

                case OPEN_ENDED :
                    // Empty open-ended answers are allowed (represent "no answer")
                    // Limit open-ended answers to 1000 characters
                    if (raw.length() > 1000) {
                        throw new OpenEndedAnswerTooLongException(questions.get(i).getQuestionText(), raw.length());
                    }
                    QuestionAnswer qope = new QuestionAnswer(QuestionType.OPEN_ENDED, raw);
                    qa.add(qope);
                    break;

                case  NUMERIC :
                    if (raw.isEmpty()) {
                        // Treat empty numeric answer as "no answer" and store null
                        QuestionAnswer qnumEmpty = new QuestionAnswer(QuestionType.NUMERIC, null);
                        qa.add(qnumEmpty);
                    } else {
                        if (!raw.matches("-?\\d+")) { //Check if the answer is a valid integer
                            throw new InvalidAnswerFormatException(questions.get(i).getQuestionText(), raw);
                        }
                        int num = Integer.parseInt(raw);
                        Integer minVal = questions.get(i).getMinBound();
                        Integer maxVal2 = questions.get(i).getMaxBound();
                        int minBound = (minVal == null ? 0 : minVal);
                        int maxBound = (maxVal2 == null ? 0 : maxVal2);
                        if (num < minBound || num > maxBound) {
                            throw new InvalidAnswerRangeException(questions.get(i).getQuestionText(), minBound, maxBound, num);
                        }
                        QuestionAnswer qnum = new QuestionAnswer(QuestionType.NUMERIC,num);
                        qa.add(qnum);
                    }
                    break;
            }
        }
        Answer answer = new Answer(usrID, formID, qa);
        dataManager.addNewAnswer(answer);
    }
    

    /**
     * Imports a form from a JSON file
     * Expected format:
     * {
     *     "title": "Encuesta de Satisfacción",
     *     "description": "Queremos conocer tu opinión sobre el servicio.",
     *     "questions": [
     *         {
     *             "type": "OPEN_ENDED",
     *             "text": "¿Qué opinas del producto?"
     *         },
     *        {
     *             "type": "NUMERIC",
     *              "text": "¿Qué nota le pondrías al producto?",
     *              "min": 1,
     *              "max": 5
     *        },
     *        {
     *            "type": "MULTIPLE_CHOICE",
     *              "text": "¿Cómo calificarías el servicio?",
     *              "options": ["Excelente", "Bueno", "Regular", "Malo"]
     *          }
     *   ]
     * }
     * @param path The path to the JSON file
     * @throws IOException if there was an error reading the file
     * @throws FormException if there was an error importing the form (InvalidFormStructureException, FormTitleInvalid, InvalidQuestionTypeException, InvalidQuestionTextException, InvalidChoicesException, InvalidQuestionBoundsException)
     */
    public void importFormFromJson(String path, Profile p) throws IOException, FormException, InvalidQuestionTypeException { //io exception used to read file
        String jsonContent = Files.readString(Paths.get(path));

        Gson gson = new Gson();
        FormJson formJson = gson.fromJson(jsonContent, FormJson.class);

        if (formJson == null || formJson.title == null || formJson.description == null || 
            formJson.questions == null || formJson.questions.isEmpty()) {
            throw new InvalidFormStructureException("Form must have title, description, and at least one question.");
        }

        if (dataManager.existsForm(formJson.title)) {
            throw new FormTitleInvalid();
        }

        // Create new form
        Form newForm = new Form(formJson.title, formJson.description, activeProfile.getUUID());
        if (newForm.getTitle().isEmpty()) {
            throw new FormTitleInvalid("Title cannot be empty");
        }
        // Add questions
        for (QuestionJson qj : formJson.questions) {
            Question.QuestionType type;
            type = Question.QuestionType.valueOf(qj.type.toUpperCase());
            
            Integer maxChoices = null,minBound = null,maxBound = null;
            Boolean order = null;
            ArrayList<String> choices = null;
            if (qj.text == null || qj.text.isEmpty()) {
                throw new InvalidQuestionTextException();
            }
            switch (type) {
                case MULTIPLE_CHOICE:
                    if(qj.options.isEmpty() || qj.options == null || qj.options.size() < 2 ) {
                        throw new InvalidChoicesException();
                    }
                    choices = qj.options;
                    order = qj.ordered != null ? qj.ordered : false; // if not specified, default to false
                    if(qj.maxChoices == null || qj.maxChoices < 1 || qj.maxChoices > choices.size()){
                        throw new InvalidMaxChoicesException();
                    }
                    maxChoices = qj.maxChoices;
                    if (order) maxChoices = 1;
                    break;

                case NUMERIC:
                    if (qj.min == null || qj.max == null || qj.min > qj.max ) {
                        throw new InvalidQuestionBoundsException(qj.min, qj.max);
                    }
                    minBound = qj.min;
                    maxBound = qj.max;
                    maxChoices = 1;
                    break;
                case OPEN_ENDED:
                    break;
                default:
                    throw new InvalidQuestionTypeException(qj.text);
            }
            Question q = new Question(qj.text,type,maxChoices,choices,order,minBound,maxBound);
            newForm.addQuestion(q);

            
        }

        dataManager.addForm(newForm);
        p.addCreatedForm(newForm.getUFID());
    }


    /**
     * Import answers CSV where the file is organized per-user and contains answers to a single (fixed) form.
     * Expected format:
     * Header line (ignored)
     * ResponderUUID, Answer1, Answer2, Answer3, ...
     * ResponderUUID, Answer1, Answer2, Answer3, ...
     *
     * @param formID The UFID of the form to which the answers correspond
     * @param path Path to CSV file
     * @return Map from responder UUID to the list of answers they provided
     * @throws IOException if there was an error reading the file
     * @throws FormException if there was an error importing the answers (InvalidFormFile, IdNotFoundException, FormAlreadyAnsweredException)
     */
    public HashMap<Integer, ArrayList<String>> importAnswersFromCsv(int formID, String path) throws IOException, FormException {
        /*
        ##########################################
        Expected CSV format:
        ResponderUsername, Answer1, Answer2, Answer3, ...
        "alex", "Answer to Q1", "Answer to Q2", "Answer to Q3", ...
        "maria", "Answer to Q1", "Answer to Q2", "Answer to Q3", ...
        ##########################################
        */         

        List<String> lines = Files.readAllLines(Paths.get(path));
        if (lines.isEmpty()) {
            throw new InvalidFormFile();
        }

        HashMap<Integer, ArrayList<String>> importedAnswers = new HashMap<>();
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] columns = line.split(",");

            // Read username and convert to UUID
            String username = columns[0].trim().replaceAll("^\"|\"$", "");
            
            // Get user ID from username
            Profile userProfile = dataManager.getProfilebyUsername(username);

            if (userProfile == null) {
                throw new IOException("User not found: " + username);
            }
            
            int responderID = userProfile.getUUID();
            
            // Check if the user has already answered this form
            ArrayList<Integer> answeredForms = userProfile.getAnsweredForms();
            for (Integer answered : answeredForms) {
                if (answered == formID) {
                    throw new FormAlreadyAnsweredException(formID);
                }
            }

            // Read answers
            ArrayList<String> answersList = new ArrayList<>();
            for (int j = 1; j < columns.length; j++) {
                answersList.add(columns[j].trim().replaceAll("^\"|\"$", ""));
            }

            importedAnswers.put(responderID, answersList);
        }

        return importedAnswers;
    }
    
    /**
     * Processes imported answers by registering each user as having answered the form and saving their responses
     * @param formID The UFID of the form
     * @param answersByUser Map from user UUID to their list of answers
     * @throws FormException if there was an error processing the answers
     */
    public void processImportedAnswers(int formID, HashMap<Integer, ArrayList<String>> answersByUser) throws FormException {
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");
        
        for (Integer userID : answersByUser.keySet()) {
            Profile userProfile = dataManager.getProfile(userID);
            if (userProfile == null) throw new IdNotFoundException(userID, "User");
            
            // Registrar que el usuario ha contestado el formulario
            userProfile.addAnsweredForm(form.getUFID());
            
            // Guardar las respuestas
            answerForm(formID, userID, answersByUser.get(userID));
        }
    }

    /**
     * Import answers CSV where the file is organized per-form and contains answers from a single (fixed) user.
     * Expected format:
     * Header line (ignored)
     * UFID, Answer1, Answer2, Answer3, ...
     * (Single line expected)
     *
     * @param path Path to CSV file
     * @return Map from form UFID to the list of answers provided by the fixed user
     * @throws IOException if there was an error reading the file
     * @throws FormException if there was an error importing the answers (InvalidAnswerFile, IdNotFoundException, FormAlreadyAnsweredException)
     */
    public HashMap<Integer, ArrayList<String>> importAnswersFromCsvUserFixed(String path) throws IOException, FormException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        if (lines.isEmpty()) {
            throw new InvalidAnswerFile();
        }

        // Count non-empty data lines after header
        int dataLines = 0;
        String dataLine = null;
        for (int i = 1; i < lines.size(); i++) {
            String l = lines.get(i).trim();
            if (!l.isEmpty()) {
                dataLines++;
                dataLine = l;
            }
        }

        if (dataLines == 0) {
            throw new InvalidAnswerFile("CSV must contain exactly one data line after the header.");
        }
        if (dataLines > 1) {
            throw new InvalidAnswerFile("CSV contains more than one data line; only one is allowed for a fixed-user import.");
        }

        // Convert the single data line
        @SuppressWarnings("null") //if it were null an exception would have rised (dataLines = 0)
        String[] columns = dataLine.split(",");

        // First column must be the form UFID
        int formUFID;
        try {
            formUFID = Integer.parseInt(columns[0].trim());
        } catch (NumberFormatException e) {
            throw new InvalidAnswerFile("Invalid UFID in CSV: " + columns[0]);
        }
        try{
            getForm(formUFID);
        } catch (IdNotFoundException e) {
            throw new InvalidAnswerFile("Form with UFID " + formUFID + " not found.");
        }
        
        // Check if the active user has already answered this form
        Profile activeProfile = getActiveProfile();
        if (hasUserAnsweredForm(formUFID, activeProfile)) {
            throw new FormAlreadyAnsweredException(formUFID);
        }

        ArrayList<String> answersList = new ArrayList<>();
        for (int j = 1; j < columns.length; j++) {
            answersList.add(columns[j].trim().replaceAll("^\"|\"$", ""));
        }
        HashMap<Integer, ArrayList<String>> result = new HashMap<>();
        result.put(formUFID, answersList);
        return result;
    }

    /**
     * Gets information about existing forms (concerning UFID, title, description, creator)
     * @return An ArrayList of Strings containing information about each existing form
     */
    public ArrayList<String> getExistingFormsInfo() {
        HashMap<Integer, Form> forms = getExistingForms();
        ArrayList<String> infoList = new ArrayList<>();

        for (Form f : forms.values()) {
            String info = "-----\n" +
                        "Form UFID: " + f.getUFID() + "\n" +
                        "Title: " + f.getTitle() + "\n" +
                        "Description: " + f.getDescription() + "\n" +
                        "Creator: " + f.getCreatorUUID();
            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Adds a question to a form
     * @param formIndex The unique identifier of the form
     * @param text The text of the question
     * @param typeStr The type of the question as a String
     * @param maxChoices The maximum number of choices for multiple choice questions
     * @param options The list of options for multiple choice questions
     * @param ordered Whether the multiple choice question is ordered
     * @param minBound The minimum bound for numeric questions
     * @param maxBound The maximum bound for numeric questions
     * @throws FormException if there was an error adding the question (IdNotFoundException, InvalidQuestionTextException, InvalidQuestionTypeException, InvalidChoicesException, InvalidQuestionBoundsException)
     */
    public void addQuestionToForm(int formIndex, String text, String typeStr, ArrayList<String> options, Boolean ordered, Integer maxChoices, Integer minBound, Integer maxBound) throws FormException {

        Form form = getForm(formIndex);
        if (form == null) {
            throw new IdNotFoundException(formIndex, "Form");
        }
        if (text == null || text.isEmpty()) {
            throw new InvalidQuestionTextException();
        }
        // Normalize nulls to 0 for numeric bounds
        if ("NUMERIC".equalsIgnoreCase(typeStr)) {
            if(minBound == null || maxBound == null) {
                throw new InvalidQuestionBoundsException(0, 0);
            }
            if (minBound > maxBound) {
                throw new InvalidQuestionBoundsException(minBound, maxBound);
            }
        }
        if ("MULTIPLE_CHOICE".equalsIgnoreCase(typeStr)) {
            if (options == null || options.size() < 2) {
                throw new InvalidChoicesException();
            }
            // Validate maxChoices <= number of options
            if (maxChoices != null && maxChoices > options.size()) {
                throw new InvalidMaxChoicesException();
            }
        }

        Question.QuestionType type;
        if ("NUMERIC".equalsIgnoreCase(typeStr)) {
            type = Question.QuestionType.NUMERIC;
        }
        else if ("OPEN_ENDED".equalsIgnoreCase(typeStr)) {
            type = Question.QuestionType.OPEN_ENDED;
        }
        else if ("MULTIPLE_CHOICE".equalsIgnoreCase(typeStr)) {
            type = Question.QuestionType.MULTIPLE_CHOICE;
        }
        else throw new InvalidQuestionTypeException(text);
        

        if (type == Question.QuestionType.MULTIPLE_CHOICE && Boolean.TRUE.equals(ordered)) {
            maxChoices = 1;
        }
        // Si es multiple choice y ordered, maxChoices debe ser 1 siempre
        
        Question q = newQuest(text, type, maxChoices, options, ordered, minBound, maxBound);
        form.addQuestion(q);
        dataManager.deleteAnswersOfForm(formIndex);

        // Update form in DataManager
        dataManager.updateForm(form);
    }

    /**
     * Gets information about the forms answered by the active profile
     * @return An ArrayList of Strings containing information about each answered form
     */
    public ArrayList<String> getAnsweredFormsInfo(Profile p){
        List<Form> answeredForms = new ArrayList<>();
        for (Integer formId : p.getAnsweredForms()) {
            answeredForms.add(dataManager.getForm(formId));
        }
        ArrayList<String> infoList = new ArrayList<>();

        for (Form f : answeredForms) {
            String info = "-----\n" +
                        "Form UFID: " + f.getUFID() + "\n" +
                        "Title: " + f.getTitle() + "\n" +
                        "Description: " + f.getDescription() + "\n" +
                        "Creator: " + f.getCreatorUUID();
            infoList.add(info);
        }

        return infoList;
    }

    /**
     * Deletes an answered form from the active profile
     * @param formID The unique identifier of the form
     * @throws FormException if there was an error deleting the answered form (IdNotFoundException)
     */
    public void deleteAnsweredForm(int formID, Profile p) throws FormException {
        Form f = dataManager.getForm(formID);
        if (f==null) throw new IdNotFoundException(formID, "Form");
        // First remove answer from persistence (if present), then remove from profile's answered list
        try {
            dataManager.deleteAnswer(formID, p.getUUID());
        } catch (FormException e) {
            // propagate if user/form not found
            throw e;
        }
        p.deleteAnsweredForm(f.getUFID());

    }

    /**
     * Checks if the active profile has answered a specific form
     * @param formID The unique identifier of the form
     * @return true if the active profile has answered the form, false otherwise
     */
    public boolean hasUserAnsweredForm(int formID, Profile p) {
        ArrayList<Integer> answeredForms = p.getAnsweredForms();
        for (Integer answeredForm : answeredForms) {
            if (answeredForm == formID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Modifies the answers to an answered form for the active profile
     * @param formID The unique identifier of the form
     * @param newAnswers An ArrayList of Strings containing the new answers to the form's questions
     * @throws FormException if there was an error modifying the answered form (IdNotFoundException, InvalidAnswerCountException, InvalidMultipleChoiceAnswerException, InvalidAnswerChoiceException, InvalidAnswerFormatException, InvalidAnswerRangeException)
     */
    public void modifyAnsweredForm(int formID, Profile p, ArrayList<String> newAnswers) throws FormException {
        Form f = dataManager.getForm(formID);
        if (f==null) throw new IdNotFoundException(formID, "Form");
        
        // Check that the user has already answered this form
        if (!hasUserAnsweredForm(formID, p)) {
            throw new IdNotFoundException(formID, "You have not answered this form");
        }
        
        // First delete existing persisted answer (if present), then add the new one
        try {
            try {
                dataManager.deleteAnswer(formID, p.getUUID());
            } catch (FormException ex) {
                // ignore if failing because answer not found
                if (!(ex instanceof AnswerNotFoundException)) throw ex;
            }
            // Re-answer (because persisted answer was removed)
            answerForm(formID, p.getUUID(), newAnswers);
        } catch (FormException e) {
            // propagate if user/form not found or validation errors
            throw e;
        }
    }


    // ---------------------------------------------------------
    // K-MEANS CLUSTERING AND EVALUATION
    // ---------------------------------------------------------

    /**
     * Executes K-means clustering on a form
     * @param formID
     * @return An ArrayList of ArrayLists of Strings, each inner list represents a cluster with the representative's username followed by the members' usernames
     * @throws FormException
     */
    public ArrayList<ArrayList<String>>executeKmeans(int formID, Integer k) throws Exception {
        Form form = dataManager.getForm(formID);
        ArrayList<Answer> answers = dataManager.getAllFormAnswers(formID);

        if (form == null) throw new IdNotFoundException(formID, "Form");
        try{
            if(k != null){
                form.executeKmeansSetK(answers, (int)k);
            } else {
                form.executeKmeansElbowMethod(answers);
            }
        } catch (Exception e){
            throw e;
        }
        ArrayList<AffinityGroup> clusters = form.getAffinityGroups();
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for(AffinityGroup ag : clusters){
            if(ag != null){
                ArrayList<String> clusterInfo = new ArrayList<>();
                Integer repID = ag.getRepresentativeID();
                Profile rep = dataManager.getProfile(repID);
                if(rep != null) clusterInfo.add(rep.getUsername());
                else clusterInfo.add("Representative not found");
            
            
                ArrayList<Integer> members = ag.getMemberIDs();
                
                for(Integer memberID : members){
                    Profile member = dataManager.getProfile(memberID);
                    if(member != null) clusterInfo.add(member.getUsername());
                    else clusterInfo.add("User not found in data");
                }
                result.add(clusterInfo);
            }
            
        }


        // Update form in DataManager
        dataManager.updateForm(form);

        return result;
    }

    /**
     * Evaluates the clustering of a form
     * @param formID
     * @return double representing the evaluation score of the clustering
     * @throws FormException if there was an error evaluating the clustering (IdNotFoundException, FormException)
     */
    public double evaluateCluster(int formID, Integer k) throws Exception {   
        
        Form form = dataManager.getForm(formID);
        ArrayList<Answer> answers = dataManager.getAllFormAnswers(formID);

        if (form == null) throw new IdNotFoundException(formID, "Form");
        try{
            if(k != null){
                form.executeKmeansSetK(answers, (int)k);
            } else {
                form.executeKmeansElbowMethod(answers);
            }
        } catch (Exception e){
            throw e;
        }
        ArrayList<AffinityGroup> clusters = form.getAffinityGroups();
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        for(AffinityGroup ag : clusters){
            if(ag != null){
                ArrayList<String> clusterInfo = new ArrayList<>();
                Integer repID = ag.getRepresentativeID();
                Profile rep = dataManager.getProfile(repID);
                if(rep != null) clusterInfo.add(rep.getUsername());
                else clusterInfo.add("Representative not found");
            
            
                ArrayList<Integer> members = ag.getMemberIDs();
                
                for(Integer memberID : members){
                    Profile member = dataManager.getProfile(memberID);
                    if(member != null) clusterInfo.add(member.getUsername());
                    else clusterInfo.add("User not found in data");
                }
                result.add(clusterInfo);
            }
            
        }

        try {
            double evaluate = form.evaluateCluster();
            dataManager.updateForm(form);
            return evaluate;
        } catch (FormException e) {
            throw e;
        }
        
    }

    /**
     * Shows the chart for the affinity groups of form, uses eigenvalues.
     * Expects affinity groups to be created beforehand.
     * @param formID
     * @throws FormException
     */
    public void chartAffinityGroups(int formID) throws Exception{
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");
        if (form.getAffinityGroups() == null) {
            throw new IllegalArgumentException("Form doesnt have affinity groups generated, first run kmeans");
        }
        ArrayList<Answer> answers = dataManager.getAllFormAnswers(formID);
        form.scatterChart(answers);
    }


    // ---------------------------------------------------------
    // IMPORT / EXPORT
    // ---------------------------------------------------------

    /**
     * Exports a form to a JSON file
     * @param formID The unique identifier of the form
     * @param exportPath The path where the JSON file will be saved
     * @throws FormException if there was an error exporting the form (IdNotFoundException)
     * @throws IOException if there was an I/O error during export
     */
    public void exportForm(int formID, String exportPath) throws FormException, IOException {
        Form form = dataManager.getForm(formID);
        if (form == null) throw new IdNotFoundException(formID, "Form");
        dataManager.exportForm(form, exportPath);
    }

    /** 
     * Exports the affinity group results of a form to a specified file path.
     * 
     * @param formUFID UFID of the Form
     * @param filePath Path where the results will be saved (including filename and extension)
     * @throws IOException if an I/O error occurs
     */
    public void exportFormResults(int formUFID, String filePath) throws IOException {
        dataManager.exportFormResults(formUFID, filePath);
    }

}
