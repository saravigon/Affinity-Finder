package domain.controllers;

import domain.classes.*;
import domain.exceptions.*;
import persistence.PersistenceManager;

import java.util.*;



/**
 * DataManager class
 * Singleton class that manages data operations between the domain and persistence layers
 */
public class DataManager {
    
    /**
     * Admin user, default user with elevated privileges
     */
    private Admin admin;

    /**
     * PersistenceManager instance for disk operations
     */
    private PersistenceManager persistenceManager = new PersistenceManager();

    /**
     * Singleton instance
     */
    private static DataManager instance = null; 

    /**
     * Constructor
     */
    private DataManager() {
        Person aux = new Person("admin","admin");
        admin = new Admin(aux);
    }

    /**
     * Gets the singleton instance of DataManager
     * @return DataManager instance
     */
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Resets the singleton instance of DataManager
     */
    public static void resetInstance() {
        instance = new DataManager();
    }


    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    /** 
     * Gets the existing forms
     * @return HashMap with UFID as key and Form as value
     */
    public HashMap <Integer,Form> getForms() {
        return persistenceManager.loadAllForms();
    }
    
    /**
     * Gets a form by its UFID
     * @param UFID unique form identifier
     * @return Form identified by UFID
     */
    public Form getForm(int UFID) {
        return persistenceManager.loadForm(UFID);
    }

    /**
     * Gets a form by its title
     * @param title title of the form
     * @return Form identified by title
     */
    public Form getFormbyTitle(String title){
        return persistenceManager.loadFormByTitle(title);
    }

    /**
     * Gets a profile by its UUID
     * @param UUID unique profile identifier
     * @return Profile identified by UUID
     */
    public Profile getProfile(int UUID){
        return persistenceManager.loadProfile(UUID);
    }

    /**
     * Gets a profile by its username
     * @param username username of the profile
     * @return Profile identified by username
     */
    public Profile getProfilebyUsername(String username){
        return persistenceManager.loadProfileByUsername(username);
    }

    /**
     * Gets the list of forms answered by a user
     * @param userUUID The unique identifier of the user profile
     */
    public List<Form> getAnsweredForms(Integer userUUID) {
        
        Profile p = persistenceManager.loadProfile(userUUID);
        List<Form> answeredForms = new ArrayList<>();
        if (p != null) {
            for (Integer ufid : p.getAnsweredForms()) {
                Form f = persistenceManager.loadForm(ufid);
                if (f != null) {
                    answeredForms.add(f);
                }
            }
        }

        return answeredForms;
    }

    /** 
     * Loads all Answers associated with a specific Form UFID.
     * @param UFID ufid of the Form
     * @return List of Answers associated with the Form
     */
    public ArrayList<Answer> getAllFormAnswers(int UFID){
        return persistenceManager.loadAnswersByForm(UFID);
    }

    /** 
     * Gets the answers of a specific user for a specific form
     * @param formId UFID of the Form
     * @param userId UUID of the Profile who submitted the Answer
     * @return Loaded Answer, or null if not found
     * @throws Exception if an error occurs during loading
     */
    public Answer getUserAnswers(int formId, int userId) throws Exception {
        return persistenceManager.loadAnswer(formId, userId);
    }

    // ---------------------------------------------------------
    // EXISTENCE CHECKS
    // ---------------------------------------------------------

    /**
     * Checks if a form exists by its UFID
     * @param UFID unique form identifier of the form checked
     * @return boolean indicating existence
     */
    public boolean existsForm(int UFID){
        return persistenceManager.existsForm(UFID);
    }

    /**
     * Checks if a user exists by its UUID
     * @param UUID unique profile identifier of the user checked
     * @return boolean indicating existence
     */
    public boolean existsUser(int UUID){
        if (admin.getUUID() == UUID) return true;
        return persistenceManager.existsProfile(UUID);
    }

    /**
     * Checks if a username exists
     * @param username username to be checked
     * @return boolean indicating existence
     */
    public boolean existsUsername(String username){
        if (username.equals("admin")) return true;
        return persistenceManager.existsProfileByName(username);
    }

    /**
     * Checks if a form exists by its title
     * @param title title of the form checked
     * @return boolean indicating existence
     */
    public boolean existsForm(String title){
        return persistenceManager.existsFormByTitle(title);
    }

    // ---------------------------------------------------------
    // SETTERS / ADDERS
    // ---------------------------------------------------------

    /**
     * Add a form 
     * @param f form to be added
     * @return Boolean indicating success
     */
    public Boolean addForm(Form f){
        if(f == null || existsForm(f.getUFID()) || existsForm(f.getTitle())) {
            return false; 
        }
        
        // Save form to persistence
        try { persistenceManager.saveForm(f); } 
        catch (Exception e) { return false; }


        // Add form to creator's createdForms list
        Profile creator = getProfile(f.getCreatorUUID());
        if (creator != null) {
            creator.addCreatedForm(f.getUFID());
        }

        // Rewrite profile with updated createdForms
        try { persistenceManager.saveProfile(creator); } 
        catch (Exception e) { return false; }

        return true;
    }

    /**
     * Add a new form
     * @param title title of the form
     * @param description description of the form
     * @param creator profile who created the form
     * @return Boolean indicating success
     */
    public Boolean addNewForm(String title, String description, Profile creator){
        if (existsForm(title)) return false;
        Form f = new Form(title, description, creator.getUUID());
        return addForm(f);
    }

    /**
     * Add a new person
     * @param usr username of the new person
     * @param pass password of the new person
     * @return Boolean indicating success
     */
    public Boolean addNewPerson(String usr, String pass){
        if (persistenceManager.existsProfileByName(usr)) return false;
        Profile p = new Profile(usr, pass);
        
        try { persistenceManager.saveProfile(p); } 
        catch (Exception e) { return false; }

        return true;
    }

    /**
     * Add a new answer
     * @param answer answer to be added
     * @return Boolean indicating success
     */
    public Boolean addNewAnswer(Answer answer){
        if(!existsUser(answer.getResponderUUID())) return false;
        if(!existsForm(answer.getFormUFID())) return false;

        try {
            // Guardar la respuesta en disco
            persistenceManager.saveAnswer(answer);
            
            // Actualizar el perfil del usuario
            Profile p = getProfile(answer.getResponderUUID());
            if (p != null) {
                // Solo agregar si no está ya presente (evitar duplicados al modificar respuestas)
                if (!p.getAnsweredForms().contains(answer.getFormUFID())) {
                    p.addAnsweredForm(answer.getFormUFID());
                    persistenceManager.saveProfile(p);
                }
            }
            
            // Actualizar el formulario
            Form f = getForm(answer.getFormUFID());
            if (f != null) {
                //f.addResponder(answer.getResponderUUID());
                persistenceManager.saveForm(f);
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error saving answer: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // UPDATES
    // ---------------------------------------------------------
    /**
     * Updates an existing form
     * @param f form to be updated
     * @return Boolean indicating success
     */
    public boolean updateForm(Form f) {
        if (f == null || !existsForm(f.getUFID())) {
            return false;
        }
        
        try {
            persistenceManager.saveForm(f);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating form: " + e.getMessage());
            return false;
        }
    }
    /**
     * Updates an existing profile
     * @param p profile to be updated
     * @return Boolean indicating success
     */
    public boolean updateProfile(Profile p) {
        if (p == null || !existsUser(p.getUUID())) {
            return false;
        }
        
        try {
            persistenceManager.saveProfile(p);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return false;
        }
    }

    /**
     * Checks if there is currently a stored answer for a given form and responder
     * Used to distinguish between modifying an existing answer (after deletion) and answering twice
     * @param formUFID Form identifier
     * @param responderUUID User identifier
     * @return true if an answer entry exists, false otherwise
     */
    public boolean hasAnswer(int formUFID, int responderUUID) {
        return persistenceManager.existsAnswer(formUFID, responderUUID);
    }


    // ---------------------------------------------------------
    // DELETION METHODS
    // ---------------------------------------------------------


    /**
     * Deletes an answer for a given form and responder
     * 
     * This method also updates the profile of the user who submitted the answer
     * and recalculates the k-means clustering for the form.
     * 
     * @param formUFID the form id
     * @param responderUUID the responder id
     * @return true if an answer was removed, false otherwise
     * @throws IdNotFoundException if the form or user do not exist
     */
    public Boolean deleteAnswer(int formUFID, int responderUUID) throws FormException {
        if (!existsForm(formUFID)) throw new IdNotFoundException(formUFID, "Form");
        if (!existsUser(responderUUID)) throw new IdNotFoundException(responderUUID, "User");

        if (!persistenceManager.existsAnswer(formUFID, responderUUID)) {
            throw new AnswerNotFoundException(formUFID, responderUUID);
        }
    
        try {
            // Eliminar la respuesta de disco
            boolean deleted = persistenceManager.deleteAnswer(formUFID, responderUUID);
            
            if (deleted) {
                // Actualizar el perfil del usuario
                Profile p = getProfile(responderUUID);
                if (p != null) {
                    p.deleteAnsweredForm(formUFID);
                    persistenceManager.saveProfile(p);
                }
                
                // Actualizar el formulario
                Form f = getForm(formUFID);
                if (f != null) {
                  
                    // Recalcular kmeans
                    FormController formController = new FormController();
                    formController.executeKmeans(formUFID,null);

                    persistenceManager.saveForm(f);
                }
            }
            
            return deleted;

        } catch (Exception e) {
            System.err.println("Error deleting answer: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a form by its UFID
     * 
     * This method also deletes all answers associated with the form and updates the profiles of users who answered the form.
     * 
     * @param UFID unique form identifier of the form to be deleted
     * @return Boolean indicating success
     */
    public Boolean deleteForm(int UFID) {
        Form f = getForm(UFID);
        if (f == null) return false;

        try {
            // Actualizar el perfil del creador
            Profile p = getProfile(f.getCreatorUUID());
            if (p != null) {
                p.deleteForm(f.getUFID());
                persistenceManager.saveProfile(p);
            }
            
            // Eliminar todas las respuestas asociadas
            deleteAnswersOfForm(UFID);
            
            // Eliminar el formulario
            return persistenceManager.deleteForm(UFID);
        } catch (Exception e) {
            System.err.println("Error deleting form: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes all answers associated with a form
     * 
     * This method also updates the profiles of users who answered the form.
     * 
     * @param UFID unique form identifier of the form whose answers are to be deleted
     */
    public void deleteAnswersOfForm(int UFID) {
    try {
        // Cargar todas las respuestas del formulario
        List<Answer> answers = persistenceManager.loadAnswersByForm(UFID);
        
        for (Answer a : answers) {
            // Actualizar el perfil de cada usuario que respondió
            Profile p = getProfile(a.getResponderUUID());
            if (p != null) {
                p.deleteAnsweredForm(UFID);
                persistenceManager.saveProfile(p);
            }
            
            // Eliminar la respuesta
            persistenceManager.deleteAnswer(UFID, a.getResponderUUID());
        }
    } catch (Exception e) {
        System.err.println("Error deleting answers of form: " + e.getMessage());
    }
}

    /**
     * Deletes a profile by its UUID
     * 
     * This method also deletes all forms created by the user and all answers provided by the user.
     * It recalculates affinity groups for affected forms after deletions.
     * 
     * @param UUID unique profile identifier of the user to be deleted
     * @return Boolean indicating success
     */
    public Boolean deleteProfile(int UUID){
        try {
            // Obtener todos los formularios creados por el usuario
            HashMap<Integer, Form> forms = getForms();

            // Listas para seguimiento de formularios a eliminar y a recalcular
            List<Integer> UFIDToDelete = new ArrayList<>();
            List<Integer> UFIDToRecalculate = new ArrayList<>();
            
            for (Form f : forms.values()) {
                if (f.getCreatorUUID() == UUID) {
                    UFIDToDelete.add(f.getUFID());
                }
            }
            
            // Eliminar cada formulario creado por el usuario
            for (Integer ufid : UFIDToDelete) {
                deleteForm(ufid);
            }
            
            // Eliminar todas las respuestas que el usuario ha dado y recolectar formularios afectados
            for (Form f : forms.values()) {
                List<Answer> answers = persistenceManager.loadAnswersByForm(f.getUFID());
                for (Answer a : answers) {
                    if (a.getResponderUUID() == UUID) {
                        persistenceManager.deleteAnswer(f.getUFID(), UUID);
                        // El formulario fue afectado, necesita recalcular kmeans
                        if (!UFIDToRecalculate.contains(f.getUFID())) {
                            UFIDToRecalculate.add(f.getUFID());
                        }
                    }
                }
            }
            
            // Recalcular kmeans para formularios afectados
            for (Integer ufid : UFIDToRecalculate) {
                Form f = getForm(ufid);
                if (f != null) {
                    FormController formController = new FormController();
                    formController.executeKmeans(ufid,null);
                    persistenceManager.saveForm(f);
                }
            }
            
            // Eliminar el perfil
            return persistenceManager.deleteProfile(UUID);
        } catch (Exception e) {
            System.err.println("Error deleting profile: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // EXPORT METHODS
    // ---------------------------------------------------------

    /**
     * Exports a form to a specified file path in JSON format.
     * 
     * @param form Form to export
     * @param filePath Destination file path
     * @return true if export was successful, false otherwise
     */
    public boolean exportForm(Form form, String filePath) {
        try {
            persistenceManager.exportForm(form, filePath);
            return true;
        } catch (Exception e) {
            System.err.println("Error exporting form: " + e.getMessage());
            return false;
        }
    }

    /**
    * Exports the affinity group results of a form to a specified file path.
    * Translates all user IDs to usernames - no IDs visible to user.
    * 
    * @param formUFID UFID of the Form
    * @param filePath Path where the results will be saved
    */
    public void exportFormResults(int formUFID, String filePath) {
        try {
            Form form = getForm(formUFID);
            if (form == null) {
                System.err.println("Form not found: " + formUFID);
                return;
            }
            
            List<Map<String, Object>> groups = new ArrayList<>();
            for (AffinityGroup group : form.getAffinityGroups()) {
                Map<String, Object> groupMap = new HashMap<>();
                
                // Representative
                Profile rep = getProfile(group.getRepresentativeID());
                groupMap.put("representative", rep != null ? rep.getUsername() : "Unknown");
                
                // Members
                List<String> members = new ArrayList<>();
                for (Integer memberId : group.getMemberIDs()) {
                    Profile p = getProfile(memberId);
                    members.add(p != null ? p.getUsername() : "Unknown");
                }
                groupMap.put("members", members);
                
                groups.add(groupMap);
            }
            
            // Export to JSON via persistence
            persistenceManager.exportAffinity(groups, filePath);
        } catch (Exception e) {
            System.err.println("Error exporting form results: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ADMIN FUNCTIONS
    // ---------------------------------------------------------


    /**
     * Deletes all persistent data (Profiles, Forms, and Answers).
     * Use with caution - this operation cannot be undone.
     * 
     * @return true if all data was successfully deleted, false otherwise
     */
    public Boolean deleteAllData() {
        return persistenceManager.deleteAllData();
    }
}