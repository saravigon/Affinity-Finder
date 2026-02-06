package domain.controllers;

import domain.exceptions.*;
import domain.classes.*;

import java.util.*;
import java.io.IOException;

/**
 * Domain controller that serves as a facade for the domain layer.
 * It manages user profiles and forms by delegating to UserController, ProfileController, and FormController.
 */
public class DomainController {

    /**
     * Attributes: FormController instance for managing forms
     */
    private final FormController formController;
    /**
     * Attributes: UserController instance for managing users
     */
    private final UserController userController;
    /**
     * Attributes: ProfileController instance for managing user profiles
     */
    private final ProfileController profileController;
    
    /**
     * Attributes: AffinityGroupController instance for managing affinity groups
     */
    private final AffinityGroupController affinityGroupController;
    
    /**
     * Attributes: AdminController instance for managing admin functionalities
     */
    private final AdminController adminController;


    /**
     * Constructor for DomainController.
     * Initializes the UserController, ProfileController, FormController, AffinityGroupController, and AdminController.
     * If a profileID is provided, it initializes ProfileController and FormController with that ID.
     * 
     * @param profileID Optional profile ID to initialize controllers with a specific user profile.
     */
    public DomainController(Integer profileID) {
        this.userController = new UserController();
        this.affinityGroupController = new AffinityGroupController();
        this.adminController = new AdminController();

        if (profileID != null) {
            this.profileController = new ProfileController(profileID);
            this.formController = new FormController(profileID);
        }
        else {
            this.profileController = new ProfileController();
            this.formController = new FormController(profileController.getID());
        }
    }

    // ---------------------------------------------------------
    // USER MANAGEMENT METHODS - UserController delegation
    // ---------------------------------------------------------
    /**
     * Call to userController, creates a new user with the given username and password.
     * 
     * @param username The desired username for the new user.
     * @param password The desired password for the new user.
     * @throws UsernameAlreadyExistsException If the username is already taken.
     */
    public void createUser(String username, String password) throws UsernameAlreadyExistsException {
        userController.createUser(username, password);
    }

    /**
     * Call to userController, logs in a user with the given username and password.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @throws ProfileNotFoundException If the profile is not found.
     */
    public void logIn(String username, String password) throws ProfileNotFoundException {
        userController.logIn(username, password);
    }

    /**
     * Call to userController, gets the UUID by username.
     * 
     * @param username The username to look up.
     * @return The UUID of the user.
     * @throws ProfileNotFoundException If the profile is not found.
     */
    public int getUUIDbyUsername(String username) throws ProfileNotFoundException {
        return userController.getUUIDbyUsername(username);
    }

    /**
     * Call to userController, checks if the user is an admin.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     * @return True if the user is an admin, false otherwise.
     */
    public Boolean isAdmin(String username, String password) {
        return userController.isAdmin(username, password);
    }

    /**
     * Call to userController, gets the profile by ID.
     * 
     * @param profileID The ID of the profile.
     * @return The Profile object.
     * @throws ProfileNotFoundException If the profile is not found.
     */
    public Profile getProfileByID(int profileID) throws ProfileNotFoundException {
        return userController.getProfile(profileID);
    }


    // ---------------------------------------------------------
    // PROFILE MANAGEMENT METHODS - ProfileController delegation
    // ---------------------------------------------------------

    /**
     * Call to profileController, gets the username of the current profile.
     * 
     * @return The username of the current profile.
     */
    public String getProfileUsername() {
        return profileController.getUsername();
    }

    /**
     * Call to profileController, gets the description of the current profile.
     * 
     * @return The description of the current profile.
     */
    public String getProfileDescription() {
        return profileController.getDescription();
    }

    /**
     * Call to profileController, changes the password of the current profile.
     * 
     * @param newPassword The new password to set.
     * @throws InvalidFormat If the new password format is invalid.
     */
    public void changeProfilePassword(String newPassword) throws InvalidFormat {
        profileController.changePassword(newPassword);
    }

    /**
     * Call to profileController, changes the username of the current profile.
     * 
     * @param newUsername The new username to set.
     * @throws UsernameAlreadyExistsException If the new username is already taken.
     * @throws InvalidFormat If the new username format is invalid.
     */
    public void changeProfileUsername(String newUsername) throws UsernameAlreadyExistsException, InvalidFormat {
        profileController.changeUsername(newUsername);
    }

    /**
     * Call to profileController, changes the description of the current profile.
     * 
     * @param newDescription The new description to set.
     * @throws InvalidFormat If the new description format is invalid.
     */
    public void changeProfileDescription(String newDescription) throws InvalidFormat {
        profileController.changeDescription(newDescription);
    }

    /**
     * Call to profileController, gets the ID of the current profile.
     * 
     * @return The ID of the current profile.
     */
    public int getProfileID() {
        return profileController.getID();
    }
    /**
     * Call to profileController, gets the current Profile object.
     * 
     * @return The current Profile object.
     */
    public Profile getCurrentProfile() {
        return profileController.getProfile();
    }

    /**
     * Call to profileController, deletes the current profile.
     * 
     * @throws ProfileNotFoundException If the profile is not found.
     */
    public void deleteCurrentProfile() throws ProfileNotFoundException {
        profileController.deleteUser();
    }


    // ---------------------------------------------------------
    // ADMIN METHODS - AdminController delegation
    // ---------------------------------------------------------

    /**
     * Call to adminController, deletes a user by their username.
     * 
     * @param username The username of the user to delete.
     * @return True if the user was successfully deleted, false otherwise.
     * @throws ProfileNotFoundException If the user profile is not found.
     */
    public Boolean deleteUserByUsername(String username) throws ProfileNotFoundException {
        Profile profile = userController.getProfilebyUsername(username);
        if (profile == null) {
            throw new ProfileNotFoundException("User " + username + " not found.");
        }
        return adminController.deleteUser(profile.getUUID());
    }


    // ---------------------------------------------------------
    // FORM MANAGEMENT METHODS - FormController delegation
    // ---------------------------------------------------------

    /**
     * Call to formController, gets information about existing forms.
     * 
     * @return A list of strings containing information about existing forms.
     */
    public ArrayList<String> getExistingFormsInfo() {
        return formController.getExistingFormsInfo();
    }

    /**
     * Call to formController, creates a new form with the given title and description.
     * 
     * @param title The title of the new form.
     * @param description The description of the new form.
     * @throws FormException If there is an error creating the form.
     */
    public void createNewForm(String title, String description) throws FormException {
        formController.createNewForm(title, description);
        Form f = getFormbyTitle(title);
        profileController.addCreatedForm(f.getUFID());
    }

    /**
     * Call to formController, deletes a form by its ID.
     * 
     * @param formId The ID of the form to delete.
     * @throws FormException If there is an error deleting the form.
     */
    public void deleteForm(int formId) throws FormException {
        formController.deleteForm(formId);
    }

    /**
     * Call to formController, deletes a question from a form.
     * 
     * @param formId The ID of the form.
     * @param questionIndex The index of the question to delete.
     * @throws FormException If there is an error deleting the question.
     */
    public void deleteQuestion(int formId, int questionIndex)  throws FormException {
        formController.deleteQuestion(formId, questionIndex);
    }

    /**
     * Call to formController, gets a form by its ID.
     * 
     * @param formId The ID of the form.
     * @return The Form object.
     * @throws IdNotFoundException If the form ID is not found.
     */
    public Form getForm(int formId) throws IdNotFoundException {
        return formController.getForm(formId);
    }

    /**
     * Call to formController, gets a form by its title.
     * 
     * @param title The title of the form.
     * @return The Form object.
     * @throws FormException If there is an error retrieving the form.
     */
    public Form getFormbyTitle(String title) throws FormException {
        return formController.getFormByTitle(title);
    }

    /**
     * Call to formController, changes the title of a form.
     * 
     * @param formId The ID of the form.
     * @param newTitle The new title for the form.
     * @throws FormException If there is an error changing the title.
     */
    public void changeFormTitle(int formId, String newTitle) throws FormException {
        formController.changeTitle(formId, newTitle);
    }
    
    /**
     * Call to formController, changes the description of a form.
     * 
     * @param formId The ID of the form.
     * @param newDescription The new description for the form.
     * @throws FormException If there is an error changing the description.
     */
    public void changeFormDescription(int formId, String newDescription) throws FormException {
        formController.changeDescription(formId, newDescription);
    }

    /**
     * Call to formController, gets questions with metadata from a form.
     * 
     * @param formId The ID of the form.
     * @return A list of strings containing questions with metadata.
     * @throws FormException If there is an error retrieving the questions.
     */
    public ArrayList<String> getQuestionsWithMetadataFromForm(int formId) throws FormException{
        return formController.getQuestionsWithMetadataFromForm(formId);
    }

    /**
     * Call to formController, checks if a form exists by its ID.
     * 
     * @param formId The ID of the form.
     * @return True if the form exists, false otherwise.
     */
    public boolean existsForm(int formId) {
        return formController.existsForm(formId);
    }
    
    /**
     * Call to formController, modifies a question in a form.
     * 
     * @param formId The ID of the form.
     * @param questionIndex The index of the question to modify.
     * @param newQuestionText The new text for the question.
     * @throws FormException If there is an error modifying the question.
     */
    public void modifyQuestion(int formId, int questionIndex, String newQuestionText) throws FormException {
        formController.modfyQuestion(formId, questionIndex, newQuestionText);
    }

    /**
     * Call to formController, adds a question to a form.
     * 
     * @param formIndex The index of the form.
     * @param text The text of the question.
     * @param typeStr The type of the question as a string.
     * @param options The options for the question (if applicable).
     * @param ordered Whether the options are ordered (if applicable).
     * @param maxChoices The maximum number of choices (if applicable).
     * @param minBound The minimum bound (if applicable).
     * @param maxBound The maximum bound (if applicable).
     * @throws FormException If there is an error adding the question.
     */
    public void addQuestionToForm(int formIndex, String text, String typeStr, ArrayList<String> options, Boolean ordered, Integer maxChoices, Integer minBound, Integer maxBound) throws FormException {
        formController.addQuestionToForm(formIndex, text, typeStr, options, ordered, maxChoices, minBound, maxBound);
    }
    /**
     * Call to formController, gets information about answered forms for the current profile.
     * 
     * @return A list of strings containing information about answered forms.
     */
    public ArrayList<String> getAnsweredFormsInfo() {
        Profile p = getCurrentProfile();
        return formController.getAnsweredFormsInfo(p);
    }

    /**
     * Call to formController, deletes an answered form for the current profile.
     * 
     * @param formId The ID of the form to delete.
     * @throws FormException If there is an error deleting the answered form.
     */
    public void deleteAnsweredForm(int formId) throws FormException {
        Profile p = getCurrentProfile();
        formController.deleteAnsweredForm(formId, p);
    }

    /**
     * Call to formController, imports answers from a CSV file for a specific form.
     * 
     * @param formID The ID of the form.
     * @param path The file path of the CSV file.
     * @return A HashMap mapping user IDs to their list of answers.
     * @throws IOException If there is an error reading the file.
     * @throws FormException If there is an error importing the answers.
     */
    public HashMap<Integer, ArrayList<String>> importAnswersFromCsv(int formID, String path) throws IOException, FormException {
        return formController.importAnswersFromCsv(formID, path);
    }
    /**
     * Call to formController, processes imported answers for a specific form.
     * 
     * @param formID The ID of the form.
     * @param answersByUser A HashMap mapping user IDs to their list of answers.
     * @throws FormException If there is an error processing the imported answers.
     */
    public void processImportedAnswers(int formID, HashMap<Integer, ArrayList<String>> answersByUser) throws FormException {
        formController.processImportedAnswers(formID, answersByUser);
    }
    /** 
     * Call to formController, gets the active profile.
     * 
     * @return The active Profile object.
     */
    public Profile getActiveProfile(){
        return formController.getActiveProfile();
    }

    /**
     * Call to formController, imports answers from a CSV file with fixed user IDs.
     * 
     * @param path The file path of the CSV file.
     * @return A HashMap mapping user IDs to their list of answers.
     * @throws IOException If there is an error reading the file.
     * @throws FormException If there is an error importing the answers.
     */
    public HashMap<Integer, ArrayList<String>> importAnswersFromCsvUserFixed(String path) throws IOException, FormException {
        return formController.importAnswersFromCsvUserFixed(path);
    }
    
    /**
     * Call to formController, submits answers for a form.
     * 
     * @param formID The ID of the form to answer.
     * @param userID The ID of the user submitting the answers.
     * @param answers A list of answers to submit.
     * @throws FormException If there is an error submitting the answers.
     */
    public void answerForm(int formID, int userID, ArrayList<String> answers) throws FormException {
        formController.answerForm(formID, userID, answers);
        profileController.addAnsweredForm(formID);
    }

    /**
     * Call to formController, retrieves the answers submitted by a user for a specific form.
     * 
     * @param formId The ID of the form.
     * @param userId The ID of the user.
     * @return An Answer object containing the user's answers for the form.
     * @throws Exception If there is an error retrieving the answers.
     */
    public Answer getUserAnswers(int formId, int userId) throws Exception {
        // Return list of answer strings for this user and form
        return formController.getUserAnswers(formId, userId);
    }

    /**
     * Call to formController, checks if a user has already answered a specific form.
     * 
     * @param formId The ID of the form.
     * @param userId The ID of the user.
     * @return True if the user has answered the form, false otherwise.
     * @throws Exception If there is an error checking the answers.
     */

    public boolean hasUserAnsweredForm(int formId, int userId) throws Exception {
        // Implementation depends on your data structure
        // Check if there are existing answers for this user+form combination
        Profile p = userController.getProfile(userId);
        return formController.hasUserAnsweredForm(formId, p);
    }

    // ---------------------------------------------------------
    // FORM EXPORT/IMPORT METHODS - FormController delegation
    // ---------------------------------------------------------


    /** 
     * Exports the affinity group results of a form to a specified file path.
     * 
     * @param formUFID UFID of the Form
     * @param filePath Path where the results will be saved (including filename and extension)
     * @throws IOException if an I/O error occurs
     */
    public void exportFormResults(int formUFID, String filePath) throws IOException {
        formController.exportFormResults(formUFID, filePath);
    }

    /** 
     * Exports a form to a specified file path in JSON format.
     * 
     * @param formID ID of the form to export
     * @param filePath Destination file path for the exported form
     * @throws IOException If an I/O error occurs during export
     * @throws FormException If the form cannot be found or exported
     */
    public void exportForm(int formID, String filePath) throws IOException, FormException {
        formController.exportForm(formID, filePath);
    }

    // ---------------------------------------------------------
    // KMEANS MANAGEMENT METHODS - FormController delegation
    // ---------------------------------------------------------

    /**
     * Call to formController, executes the K-means clustering algorithm on the answers of a specific form.
     * 
     * @param formID The ID of the form.
     * @return A list of clusters, each cluster being a list of answer strings.
     * @throws Exception If there is an error during the execution of K-means.
     */ 
    public ArrayList<ArrayList<String>> executeKmeans(int formID, Integer k) throws Exception {
        return formController.executeKmeans(formID, k);
        
    }

    /**
     * Call to formController, evaluates the clustering of a specific form.
     * 
     * @param formID The ID of the form.
     * @return A double value representing the evaluation metric of the clustering.
     * @throws Exception If there is an error during the evaluation.
     */
    public double evaluateCluster(int formID, Integer k) throws Exception {
        return formController.evaluateCluster(formID,k);
    }

    /**
     * Call to formController, modifies the answers of an answered form.
     * 
     * @param formID The ID of the form.
     * @param newAnswers A list of new answers to update.
     * @throws FormException If there is an error modifying the answered form.
     */
    public void modifyAnsweredForm(int formID, ArrayList<String> newAnswers) throws FormException {
        Profile p = getCurrentProfile();
        formController.modifyAnsweredForm(formID, p, newAnswers);
    }

    /**
     * Call to formController, imports a form from a JSON file.
     * @param path The file path of the JSON file.
     * @throws IOException If there is an I/O error during import
     * @throws FormException If there is an error importing the form.
     */
    public void importFormFromJson(String path) throws IOException, FormException {
        Profile p = getCurrentProfile();
        formController.importFormFromJson(path, p);
    }

    /**
     * Call to formController, generates a scatter chart using PCA Eigen decomposition for affinity groups of a form.
     * 
     * @param formID The ID of the form.
     * @throws Exception If there is an error generating the chart.
     */
    public void chartAffinityGroups(int formID) throws Exception{
        formController.chartAffinityGroups(formID);
    }

    // ---------------------------------------------------------
    // AFFINITY MANAGEMENT METHODS - AffinityController delegation
    // ---------------------------------------------------------
    /**
     * Call to affinityGroupController, searches for an affinity group by form ID and user ID.
     * 
     * @param formID The ID of the form.
     * @param userID The ID of the user.
     * @return True if the affinity group exists, false otherwise.
     */
    public boolean searchAffinityGroup(int formID, int userID) {
        return affinityGroupController.searchAffinityGroup(formID, userID);
    }

    /**
     * Call to affinityGroupController, retrieves the currently active affinity group.
     * 
     * @return The active AffinityGroup object.
     */
    public AffinityGroup getActiveAffinityGroup() {
        return affinityGroupController.getActiveAffinityGroup();
    }
}
