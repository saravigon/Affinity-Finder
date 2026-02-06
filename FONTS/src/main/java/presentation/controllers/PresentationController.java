package presentation.controllers;

import domain.controllers.*;
import presentation.classes.*;

public class PresentationController {

    /**
     * Reference to DomainController
     */
    private DomainController domainController;
    
    /**
     * Constructor of PresentationController
     */
    public PresentationController() {
        this.domainController = new DomainController(null);
    }

    /**
     * Initializes the DomainController with a profile ID
     * It is called when a user logs in
     * @param profileID Profile ID of the logged-in user
     */
    public void inicializeController(Integer profileID){
        this.domainController = new DomainController(profileID);
    }

    /**
     * Gets the DomainController reference
     * @return DomainController reference
     */
    public DomainController getController(){
        return domainController;
    }

    /**
     * Starts the initial menu view
     */
    public void startMenu() {
        new InitialMenuView(this).setVisible(true);
    }

}
