package persistence;

import com.google.gson.*;
import domain.classes.Form;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles persistence operations for Form entities
 */
class FormPersistence {
    
    /**
     * Directory to store Form JSON files
     */
    private static final String FORMS_DIR = "data/forms/";
    /**
     * Gson instance for JSON serialization/deserialization
     */
    private final Gson gson;
    

    /**
     * Constructor initializes the FormPersistence with a Gson instance.
     * 
     * @param gson Gson instance for JSON serialization/deserialization
     */
    FormPersistence(Gson gson) {
        this.gson = gson;
        createDirectoryIfNotExists();
    }
    
    /**
     * Creates the forms directory if it does not exist.
     */
    private void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(FORMS_DIR));
        } catch (IOException e) {
            System.err.println("Error creating forms directory: " + e.getMessage());
        }
    }
    
    /** 
     * Saves a Form to a JSON file.
     * 
     * @param form Form to save
     * @throws IOException if an I/O error occurs
     */
    void save(Form form) throws IOException {
        String filename = FORMS_DIR + form.getUFID() + ".json";
        form.clearKmeans();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(form, writer);
        }
    }
    
    /** 
     * Loads a Form from a JSON file by UFID.
     * 
     * @param ufid UFID of the Form to load
     * @return Loaded Form, or null if not found
     */
    Form load(int ufid) {
        String filename = FORMS_DIR + ufid + ".json";
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Form.class);
        } catch (IOException e) {
            return null;
        }
    }
    
    /** 
     * Loads all Forms from the forms directory.
     * 
     * @return Map of UFID to Form for all loaded Forms
     */
    HashMap<Integer, Form> loadAll() {
        HashMap<Integer, Form> forms = new HashMap<>();
        try {
            Files.list(Paths.get(FORMS_DIR))
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        String filename = path.getFileName().toString();
                        int ufid = Integer.parseInt(filename.replace(".json", ""));
                        
                        Form form = load(ufid);
                        if (form != null) {
                            forms.put(ufid, form);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid form filename: " + path);
                    }
                });
        } catch (IOException e) {
            System.err.println("Error listing forms: " + e.getMessage());
        }
        return forms;
    }
    
    /** 
     * Loads a Form by its title.
     * 
     * @param title Title of the Form to load
     * @return Loaded Form, or null if not found
     */
    Form loadByTitle(String title) {
        HashMap<Integer, Form> forms = loadAll();
        return forms.values().stream()
            .filter(f -> f.getTitle().equals(title))
            .findFirst()
            .orElse(null);
    }
    
    /** 
     * Deletes a Form JSON file by UFID.
     * 
     * @param ufid UFID of the Form to delete
     * @return true if the Form was deleted, false otherwise
     */
    boolean delete(int ufid) {
        String filename = FORMS_DIR + ufid + ".json";
        try {
            return Files.deleteIfExists(Path.of(filename));
        } catch (IOException e) {
            return false;
        }
    }
    
    /** 
     * Checks if a Form JSON file exists by UFID.
     * 
     * @param ufid UFID of the Form to check
     * @return true if the Form exists, false otherwise
     */
    boolean exists(int ufid) {
        String filename = FORMS_DIR + ufid + ".json";
        return Files.exists(Path.of(filename));
    }

    /**
     * Deletes all Forms from persistent storage.
     * 
     * @return true if all forms were deleted successfully
     */
    boolean deleteAll() {
        try {
            Path formsPath = Paths.get(FORMS_DIR);
            if (Files.exists(formsPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(formsPath, "*.json")) {
                    for (Path entry : stream) {
                        Files.delete(entry);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting all forms: " + e.getMessage());
            return false;
        }
    }

    /**
     * Exports a Form to a specified file path in JSON format.
     * 
     * @param form Form to export
     * @param filePath Path where the form will be saved (including filename and extension)
     * @throws IOException if an I/O error occurs
     */
    void exportForm(Form form, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(form, writer);
        }
    }

    /**
     * Exports the affinity groups of a Form to a specified file path in JSON format.
     * 
     * @param groups List of maps containing affinity group data with usernames
     * @param filePath Path where the results will be saved
     * @throws IOException if an I/O error occurs
     */
    void exportAffinity(List<Map<String, Object>> groups, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(groups, writer);
        }
    }
}