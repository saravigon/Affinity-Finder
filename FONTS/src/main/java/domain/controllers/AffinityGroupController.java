package domain.controllers;

import java.util.*;

import domain.classes.AffinityGroup;
import domain.classes.Form;
import domain.classes.Profile;

/**
 * Controller class for managing Affinity Groups
 */
public class AffinityGroupController {

    /**
     * Active affinity group
     */
    private AffinityGroup activeAffinityGroup;
    /**
     * DataManager instance
     */
    private DataManager dataManager;

    /**
     * Constructor
     * Just initializes the DataManager instance
     */
    public AffinityGroupController(){
        dataManager = DataManager.getInstance();
    }

    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    /**
     * Gets the list of forms answered by a user
     * @param userUUID The unique identifier of the user profile
     */
    public List<Form> getAnsweredForms(int userUUID) {
        return dataManager.getAnsweredForms(userUUID);
    }

    /**
     * Gets the title of a form by its ID
     * @param formId The unique identifier of the form
     * @return String title of the form
     */
    public String getTitleForForm(int formId) {
        Form f = null;

        try { f = dataManager.getForm(formId);} 
        catch (Exception e) { return null; }

        if (f == null) return null;
        return f.getTitle();
    }

    /**
     * Gets the active affinity group
     * @return Active affinity group
     */
    public AffinityGroup getActiveAffinityGroup() {
        return activeAffinityGroup;
    }

    /**
     * Gets a profile by its ID
     * @param profileId The unique identifier of the profile
     * @return Profile object
     */
    public Profile getProfileById(int profileId) {
        return dataManager.getProfile(profileId);
    }

    /**
     * Gets the list of answered form IDs for a user
     * @param userID The unique identifier of the user profile
     * @return List of Integer form IDs
     */
    public List<Integer> getAnsweredFormsIDs(int userID) {
        List<Form> forms = getAnsweredForms(userID);
        List<Integer> formIDs = new ArrayList<>();
        if (forms == null) {
            return formIDs;
        }
        for (Form f : forms) {
            formIDs.add(f.getUFID());
        }
        return formIDs;
    }

    /**
     * Gets the list of answered form titles for a user
     * @param userID The unique identifier of the user profile
     * @return List of String form titles
     */
    public List<String> getAnsweredFormsTitles(int userID) {
        List<Form> forms = getAnsweredForms(userID);
        List<String> formTitles = new ArrayList<>();
        if (forms == null) {
            return formTitles;
        }
        for (Form f : forms) {
            formTitles.add(f.getTitle());
        }
        return formTitles;
    }

    /**
     * Gets the names of the members in the active affinity group
     * @return List of String member names
     */
    public List<String> getMembersName() {
        List<String> memberNames = new ArrayList<>();
        if (activeAffinityGroup == null) return memberNames;

        for (Integer memberId : activeAffinityGroup.getMemberIDs()) {
            Profile p = getProfileById(memberId);
            if (p != null) memberNames.add(p.getUsername());
        }
        return memberNames;
    }

    /**
     * Gets the number of members in the active affinity group
     * @return Integer number of members
     */
    public Integer getNumOfMembers() {
        if (activeAffinityGroup == null) return 0;
        return activeAffinityGroup.getMemberIDs().size();
    }

    /**
     * Gets the name of the representative of the active affinity group
     * @return String name of the representative
     */
    public String getRepreName() {

        if (activeAffinityGroup == null) return null;

        Profile p = getProfileById(activeAffinityGroup.getRepresentativeID());
        return p.getUsername();
    }

    
    // ---------------------------------------------------------
    // SETTERS
    // ---------------------------------------------------------
    
    /**
     * Sets the active affinity group
     * @param group The affinity group to set as active
     */
    public void setActiveAffinityGroup(AffinityGroup group) {
        this.activeAffinityGroup = group;
    }


    // ---------------------------------------------------------
    // OTHER METHODS
    // ---------------------------------------------------------

    /**
     * Finds the affinity group for a given form ID and user ID
     * @param formID ID of the form
     * @param userID ID of the user profile
     * @return True if found, false otherwise
     */
    public boolean searchAffinityGroup(int formID, int userID) {

        Form f = dataManager.getForm(formID);

        if (f == null) return false;

        for (AffinityGroup group : f.getAffinityGroups() ) {

            if ((group.isMember(userID))) {
                setActiveAffinityGroup(group);
                return true;
            }
        }
        return false;
    }

}