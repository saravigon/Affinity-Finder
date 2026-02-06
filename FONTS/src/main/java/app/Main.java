package app;

import presentation.controllers.PresentationController;

/**
 * Main class for the Affinity Finder Application
 * Entry point that initializes the presentation layer
 */
public class Main {
    
    public static void main(String[] args) {
        PresentationController presentationController = new PresentationController();

        // Si pasas un id de perfil por argumentos, inicializa el controller con ese id
        if (args != null && args.length > 0) {
            try {
                Integer profileId = Integer.parseInt(args[0]);
                presentationController.inicializeController(profileId);
            } catch (NumberFormatException e) {
                System.out.println("Argument is not a valid integer profile id, starting without profile.");
                presentationController.inicializeController(null);
            } catch (Exception e) {
                System.out.println("Error initializing controller with profile id: " + e.getMessage());
                presentationController.inicializeController(null);
            }
        } else {
            presentationController.inicializeController(null);
        }

        System.out.println("=== AFFINITY FINDER APPLICATION ===");
        System.out.println("Starting application...");

        // Llama al método que muestra el menú inicial en PresentationController.
        // Ajusta el nombre si tu controller expone otro (startFormMenu / startMenu / start).
        try {
            presentationController.startMenu();
        } catch (NoSuchMethodError | NullPointerException ex) {
            System.out.println("PresentationController no tiene startMenu inicializado o hay NPE. Revisa la inicialización de las vistas.");
            ex.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Error launching UI: " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Application finished.");
    }
}