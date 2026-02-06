package domain.controllers;

import domain.classes.Profile;
import domain.exceptions.*;

/**
 * Controller class for managing the creation of users.
 */
public class UserController {
    /**
     * Attribute
     * DataManager: Instance of DataManager to handle data persistence
     */
    private DataManager dataManager;

    /**
     * UserController Constructor
     */
    public UserController(){
        dataManager = DataManager.getInstance();
    }

    /**
     * UserController Constructor with dependency injection for testing
     * @param dataManager
     */
    public UserController(DataManager dataManager) {
        this.dataManager = dataManager;
    }

     /**
     * Creates a new user
     * @param usr The username for user
     * @param pass  The password chosen
     * @return Calls a function to add the new user, true if created
     */
    public Boolean createUser(String usr, String pass) throws UsernameAlreadyExistsException {
        if(dataManager.existsUsername(usr)){
            throw new UsernameAlreadyExistsException();
        }
        
        return dataManager.addNewPerson(usr, pass);
    }
    
    /**
     * Checks if the username and password match
     * @param usr The username given
     * @param pass  The password given
     * @return  Calls a funtion to check, true if correct
     */
    public Boolean logIn(String usr, String pass) throws ProfileNotFoundException {
        if (usr == null || pass == null) {
            throw new ProfileNotFoundException("\nIncorrect username or password");
        }

        if (dataManager.existsUsername(usr)) {
            
            // 1) Admin case (if you want admin to have a fixed password)
            if (usr.equals("admin")) {
                if ("admin".equals(pass)) return true;
                else throw new ProfileNotFoundException("\nIncorrect username or password");
            }

            // 2) Check the password directly after retrieving the profile once
            domain.classes.Profile profile = dataManager.getProfilebyUsername(usr);
            if (profile == null || !profile.getPassword().equals(pass)) {
                throw new ProfileNotFoundException("\nIncorrect username or password");
            }
        }
        else {
            throw new ProfileNotFoundException("\nIncorrect username or password");
        }

        // 3) Final login (if dataManager.login does something extra)
        return true;
    }


    /**
     * To know if a username exists
     * @param usr The username to consult
     * @return Calls a function to verify, true if exists
     */
    public Boolean existsUsername(String usr) throws ProfileNotFoundException {
        if (!dataManager.existsUsername(usr)) {
            throw new ProfileNotFoundException("Username " + usr + " not found.");
        }
        return true;
    }

    /**
     * To know if a identifier exists
     * @param id The identifier to consult
     * @return Calls a function to verify, true if exists
     */
    public Boolean existsUUID(int id) throws ProfileNotFoundException {
        if (!dataManager.existsUser(id)) {
            throw new ProfileNotFoundException("Profile with ID " + id + " not found.");
        }
        return true;
    }
    
    /**
     * Gets a profile by its UUID
     * @param UUID The unique identifier of the profile
     * @return Calls a function in DataManager to get the profile and returns it
     */
    public Profile getProfile(int UUID){
        return dataManager.getProfile(UUID);
    }

    /**
     * Gets the UUID of a profile by its username
     * @param username The username of the profile
     * @return Calls a function in DataManager to get the profile and returns its UUID
     */
    public int getUUIDbyUsername(String username){
        return dataManager.getProfilebyUsername(username).getUUID();
    }

    /**
     * Gets the username of a profile by its UUID
     * @param ID The unique identifier of the profile
     * @return Calls a function in DataManager to get the profile and returns its username
     */
    public String getUsername(int ID){
        return dataManager.getProfile(ID).getUsername();
    }

    /**
     * Gets a profile by its username
     * @param username The username of the profile
     * @return Calls a function in DataManager to get the profile and returns it
     */
    public Profile getProfilebyUsername(String username){
        return dataManager.getProfilebyUsername(username);
    }

    /**
     * Checks if the given credentials correspond to an admin
     * @param usr The username to check
     * @param pass The password to check
     * @return true if username is "Admin" and password is "Admin", false otherwise
     */
    public Boolean isAdmin(String usr, String pass) {
        return (usr=="Admin" && pass=="Admin");
    }

    // ## MODIFICATORS ###

    /**
     * Change a user's password
     * @param id The unique identifier of the user
     * @param pass The new password
     */
    public void changePassword(int id, String pass){
        dataManager.getProfile(id).changePassword(pass);
    }

}
