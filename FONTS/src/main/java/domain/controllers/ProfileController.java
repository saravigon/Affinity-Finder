package domain.controllers;

import domain.classes.Profile;
import domain.exceptions.*;

/**
 * Controller class for managing user profiles.
 */
public class ProfileController {
    
    /**
     * Attribute
     * DataManager: Instance of DataManager to handle data persistence
     */
    private DataManager dataManager;

    /**
     * Attribute
     * The profile currently being managed
     */
    private Profile activeProfile;

    /**
     * Constructor of ProfileController
     * @param ID The unique identifier of the profile to be managed
     */
    public ProfileController(int ID){
        dataManager = DataManager.getInstance();
        activeProfile = dataManager.getProfile(ID);
    }

    /**
     * Default Constructor of ProfileController, loads a default profile
    */
    public ProfileController(){
        activeProfile = new Profile("createdUser", "pass"); // per exemple, canviar
        dataManager = DataManager.getInstance();
        dataManager.addNewPerson(getUsername(), getPassword());
    }

    /**
     * Deletes the active user
     * @return Calls a function to delete the user, true if deleted
     */
    public Boolean deleteUser(){
        int UUID = activeProfile.getUUID();
        return dataManager.deleteProfile(UUID);
    }

    /**
     * Changes the description of the active profile
     * @param desc  The new description
     */
    public void changeDescription(String desc) throws InvalidFormat {
        if (desc == null || desc.trim().isEmpty()) {
            throw new InvalidFormat("Description cannot be empty.");
        }
        activeProfile.setDescription(desc);
        dataManager.updateProfile(activeProfile);
    }

    /**
     * Change the active user's password
     * @param pass The new password
     */
    public void changePassword(String pass) throws InvalidFormat{
        if (pass.length() < 4) {
            throw new InvalidFormat("Password must be at least 4 characters long.");
        }      
        activeProfile.changePassword(pass);
        dataManager.updateProfile(activeProfile);
    }

    /**
     * Change the active user's username
     * @param usr The new username
     */
    public void changeUsername(String usr) throws UsernameAlreadyExistsException, InvalidFormat {
        if (dataManager.existsUsername(usr)) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        if (usr == null || usr.trim().isEmpty()) {
            throw new InvalidFormat("Username cannot be empty.");
        }
        activeProfile.changeUsername(usr);
        dataManager.updateProfile(activeProfile);
    }

    // -------------------------------------------------------------
    // ADDERS
    // -------------------------------------------------------------

    /**
     * Adds a created form to the active profile
     * @param formID The ID of the created form
     */
    public void addCreatedForm(int formID) {
        activeProfile.addCreatedForm(formID);
    }

    /**
     * Adds a created form to the active profile
     * @param formID The ID of the created form
     */
    public void addAnsweredForm(int formID) {
        activeProfile.addAnsweredForm(formID);
        dataManager.updateProfile(activeProfile);
    }


    // -------------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------------

    /**
     * Gets a profile by its UUID
     * @return Calls a function in DataManager to get the profile and returns it
     */
    public Profile getProfile(){
        return activeProfile;
    }
    
    /**
     * Gets the password of the active profile
     * @return The password of the active profile
     */
    public String getPassword(){
        return activeProfile.getPassword();
    }

    /**
     * Gets the username of the active profile
     * @return The username of the active profile
     */
    public String getUsername(){
        return activeProfile.getUsername();
    }

    /**
     * Gets the description of the active profile
     * @return The description of the active profile
     */
    public String getDescription(){
        return activeProfile.getDescription();
    }

    /**
     * Gets the ID of the active profile
     * @return The ID of the active profile
     */
    public int getID(){
        return activeProfile.getUUID();
    }
}
