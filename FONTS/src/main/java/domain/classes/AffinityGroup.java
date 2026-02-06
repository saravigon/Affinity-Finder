package domain.classes;
import java.util.ArrayList;

/**
 * Class representing an Affinity Group
 */
public class AffinityGroup {

    /**
     * ID of the representative profile of the group
     */
    private Integer representativeUUID;
    /**
     * List of member profile IDs of the group
     */
    private ArrayList<Integer> membersUUID;


    /**
     *  Constructor for AffinityGroup
     *
     * @param repID Representative profile of the group
     * @param memIDs List of member profiles of the group
     */
    public AffinityGroup(Integer repID, ArrayList<Integer> memIDs) {
        this.representativeUUID = repID;
        this.membersUUID = new ArrayList<>();
        if (memIDs != null) {
            this.membersUUID.addAll(memIDs); 
        }
        
        if (repID != null && !this.membersUUID.contains(repID)) {
            this.membersUUID.add(repID);
        }
    }

    /**
     *  Constructor for AffinityGroup without members
     *
     */
    public AffinityGroup() {
        this.membersUUID = new ArrayList<>();
        this.representativeUUID = null;
    }
    
    // ---------------------------------------------------------
    // GETTERS
    // ---------------------------------------------------------

    /**
     * Get the representative of the group
     * 
     * @return representative profile ID of the group
     */
    public Integer getRepresentativeID() {
        return representativeUUID;
    }

    /** 
     * Get the members of the group
     * 
     * @return list of member profiles IDs of the group
     */
    public ArrayList<Integer> getMemberIDs() {
        return new ArrayList<>(membersUUID);
    }


    // ---------------------------------------------------------
    // SETTERS
    // ---------------------------------------------------------

    /**
     * Set the representative of the group
     * 
     * If the representative is not already a member, it will be added to the group.
     * 
     * @param repID New representativeID of the group
     */
    public void setRepresentative(Integer repID) {
        this.representativeUUID = repID;
        if (repID != null && !membersUUID.contains(repID)) {
            membersUUID.add(repID);
        }
    }

    // ---------------------------------------------------------
    // GROUP MANAGEMENT
    // ---------------------------------------------------------

    /**
     * Add a member to the group
     * 
     * Returns true if the member was added, false if they were already a member or if the profile is null.
     * 
     * @param id ID to be added as a member
     */
    public boolean addMember(Integer id) {
        if (id == null) return false;
        if (!membersUUID.contains(id)) {
            membersUUID.add(id);
            return true;
        }
        return false;
    }

    /**
     * Remove a member from the group
     * 
     * Returns true if the member was removed, false if they were not a member or if the profile is null.
     * The representative cannot be removed from the group.
     * 
     * @param id ID to be removed from the group
     */
    public boolean removeMember(Integer id) {
        if (id == null) return false;
        // Do not allow removing the representative!!
        if (id.equals(representativeUUID)) return false;
        return membersUUID.remove(id);
    }

    /**
     * Check if a profile is a member of the group
     * 
     * Returns true if the profile is a member, false otherwise.
     * 
     * @param id ID to check membership
     */
    public boolean isMember(Integer id) {
        return id != null && membersUUID.contains(id);
    }
}

