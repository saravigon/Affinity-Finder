package persistence;

import com.google.gson.*;
import domain.classes.*;
import java.util.*;

import java.io.IOException;


/**
 * Controller class for handling persistence operations.
 * 
 * This class is basically a Facade that delegates persistence tasks
 * to specific persistence handlers for different entity types
 */
public class PersistenceManager {
    
    /**
     * Persistence handler for Profile entities
     */
    private final ProfilePersistence profilePersistence;
    /**
     * Persistence handler for Form entities
     */
    private final FormPersistence formPersistence;
    /**
     * Persistence handler for Answer entities
     */
    private final AnswerPersistence answerPersistence;
    
    /**
     * Constructor initializes the persistence handlers for Profile, Form, and Answer entities.
     */
    public PersistenceManager() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        this.profilePersistence = new ProfilePersistence(gson);
        this.formPersistence = new FormPersistence(gson);
        this.answerPersistence = new AnswerPersistence(gson);
    }

    // ---------------------------------------------------------
    // PROFILE METHODS - Delegate to ProfilePersistence
    // ---------------------------------------------------------
    
    /** 
     * Saves a Profile to persistent storage.
     * 
     * @param profile Profile to save
     * @throws IOException if an I/O error occurs
     */
    public void saveProfile(Profile profile) throws IOException {
        profilePersistence.save(profile);
    }
    
    /** 
     * Loads a Profile from persistent storage by UUID.
     * 
     * @param uuid UUID of the Profile to load
     * @return Loaded Profile, or null if not found
     */
    public Profile loadProfile(int uuid) {
        return profilePersistence.load(uuid);
    }
    
    /** 
     * Deletes a Profile from persistent storage by UUID.
     * 
     * @param uuid UUID of the Profile to delete
     * @return true if the Profile was deleted, false otherwise
     */
    public boolean deleteProfile(int uuid) {
        return profilePersistence.delete(uuid);
    }
    
    /** 
     * Checks if a Profile exists in persistent storage by UUID.
     * 
     * @param uuid UUID of the Profile to check
     * @return true if the Profile exists, false otherwise
     */
    public boolean existsProfile(int uuid) {
        return profilePersistence.exists(uuid);
    }

    /** 
     * Checks if a Profile exists in persistent storage by username.
     * 
     * @param username Username of the Profile to check
     * @return true if the Profile exists, false otherwise
     */
    public boolean existsProfileByName(String username) {
        return profilePersistence.loadByUsername(username) != null;
    }

    /** 
     * Loads a Profile from persistent storage by username.
     * 
     * @param username Username of the Profile to load
     * @return Loaded Profile, or null if not found
     */
    public Profile loadProfileByUsername(String username) {
        return profilePersistence.loadByUsername(username);
    }


    // ---------------------------------------------------------
    // FORM METHODS - Delegate to FormPersistence
    // ---------------------------------------------------------

    /** 
     * Saves a Form to persistent storage.
     * 
     * @param form Form to save
     * @throws IOException if an I/O error occurs
     */
    public void saveForm(Form form) throws IOException {
        formPersistence.save(form);
    }

    /** 
     * Loads a Form from persistent storage by UFID.
     * 
     * @param ufid UFID of the Form to load
     * @return Loaded Form, or null if not found
     */
    public Form loadForm(int ufid) {
        return formPersistence.load(ufid);
    }

    /** 
     * Loads all Forms from persistent storage.
     * 
     * @return Map of UFID to Form for all loaded Forms
     */
    public HashMap<Integer, Form> loadAllForms() {
        return formPersistence.loadAll();
    }

    /** 
     * Loads a Form from persistent storage by title.
     * 
     * @param title Title of the Form to load
     * @return Loaded Form, or null if not found
     */
    public Form loadFormByTitle(String title) {
        return formPersistence.loadByTitle(title);
    }

    /** 
     * Deletes a Form from persistent storage by UFID.
     * 
     * @param ufid UFID of the Form to delete
     * @return true if the Form was deleted, false otherwise
     */
    public boolean deleteForm(int ufid) {
        return formPersistence.delete(ufid);
    }

    /** 
     * Checks if a Form exists in persistent storage by UFID.
     * 
     * @param ufid UFID of the Form to check
     * @return true if the Form exists, false otherwise
     */
    public boolean existsForm(int ufid) {
        return formPersistence.exists(ufid);
    }

    /** 
     * Checks if a Form exists in persistent storage by title.
     * 
     * @param title Title of the Form to check
     * @return true if the Form exists, false otherwise
     */
    public boolean existsFormByTitle(String title) {
        return formPersistence.loadByTitle(title) != null;
    }

    /** 
     * Exports a Form to a specified file path in JSON format.
     * 
     * @param form Form to export
     * @param filePath Path where the form will be saved (including filename and extension)
     * @throws IOException if an I/O error occurs
     */
    public void exportForm(Form form, String filePath) throws IOException {
        formPersistence.exportForm(form, filePath);
    }

    /**
     * Exports affinity groups with usernames to a JSON file.
     * 
     * @param groups List of maps containing affinity group data with usernames
     * @param filePath Path where the results will be saved
     * @throws IOException if an I/O error occurs
     */
    public void exportAffinity(List<Map<String, Object>> groups, String filePath) throws IOException {
        formPersistence.exportAffinity(groups, filePath);
    }
    
    // ---------------------------------------------------------
    // ANSWER METHODS - Delegate to AnswerPersistence
    // ---------------------------------------------------------

    /** 
     * Saves an Answer to persistent storage.
     * 
     * @param answer Answer to save
     * @throws IOException if an I/O error occurs
     */
    public void saveAnswer(Answer answer) throws IOException {
        answerPersistence.save(answer);
    }

    /** 
     * Loads an Answer from persistent storage by UFID and responder UUID.
     * 
     * @param ufid UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return Loaded Answer, or null if not found
     */
    public Answer loadAnswer(int ufid, int responderUUID) {
        return answerPersistence.load(ufid, responderUUID);
    }

    /** 
     * Deletes an Answer from persistent storage by UFID and responder UUID.
     * 
     * @param ufid UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return true if the Answer was deleted, false otherwise
     */
    public boolean deleteAnswer(int ufid, int responderUUID) {
        return answerPersistence.delete(ufid, responderUUID);
    }

    /** 
     * Checks if an Answer exists in persistent storage by UFID and responder UUID.
     * 
     * @param ufid UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return true if the Answer exists, false otherwise
     */
    public boolean existsAnswer(int ufid, int responderUUID) {
        return answerPersistence.exists(ufid, responderUUID);
    }

    /** 
     * Loads all Answers associated with a specific Form UFID.
     * 
     * @param ufid UFID of the Form
     * @return List of Answers associated with the Form
     */
    public ArrayList<Answer> loadAnswersByForm(int ufid) {
        return answerPersistence.loadByForm(ufid);
    }


    // ---------------------------------------------------------
    // ADMIN METHODS - Additional operations for admin functionality
    // ---------------------------------------------------------

    /**
     * Deletes all persistent data (Profiles, Forms, and Answers).
     * Use with caution - this operation cannot be undone.
     * 
     * @return true if all data was successfully deleted, false otherwise
     */
    public boolean deleteAllData() {
        boolean success = true;
        
        // Delete all profiles
        success &= profilePersistence.deleteAll();
        
        // Delete all forms
        success &= formPersistence.deleteAll();
        
        // Delete all answers
        success &= answerPersistence.deleteAll();
        
        return success;
    }
}