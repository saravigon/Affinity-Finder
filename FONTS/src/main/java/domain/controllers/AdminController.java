package domain.controllers;

/**
 * Controller class for admin-related operations.
 */
public class AdminController {
    
    /**
     * DataManager instance to handle data persistence
     */
    private DataManager dataManager;

    /**
     * Constructor of AdminController
     */
    public AdminController(){
        dataManager = DataManager.getInstance();
    }

    /**
     * Deletes a user profile based on the provided UUID.
     * 
     * @param UUID The unique identifier of the user profile to be deleted.
     * @return true if the profile was successfully deleted, false otherwise.
     */
    public Boolean deleteUser(int UUID){
        return dataManager.deleteProfile(UUID);
    }

    public Boolean deleteForm(int formID) {
        return dataManager.deleteForm(formID);
    }
}