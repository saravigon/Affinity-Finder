package persistence;

import com.google.gson.*;
import domain.classes.Answer;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles persistence operations for Answer entities
 */
public class AnswerPersistence {

    /**
     * Directory to store Answer JSON files
     */
    private static final String ANSWERS_DIR = "data/answers/";
    /**
     * Gson instance for JSON serialization/deserialization
     */
    private final Gson gson;
    
    /**
     * Constructor initializes the AnswerPersistence with a Gson instance.
     * 
     * @param gson Gson instance for JSON serialization/deserialization
     */
    public AnswerPersistence(Gson gson) {
        this.gson = gson;
        createDirectoryIfNotExists();
    }

    /**
     * Creates the answers directory if it does not exist.
     */
    private void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(ANSWERS_DIR));
        } catch (IOException e) {
            System.err.println("Error creating answers directory: " + e.getMessage());
        }
    }

    /** 
     * Saves an Answer to a JSON file.
     * 
     * @param answer Answer to save
     * @throws IOException if an I/O error occurs
     */
    void save(Answer answer) throws IOException {
        String filename = ANSWERS_DIR + answer.getFormUFID() + "_" + answer.getResponderUUID() + ".json";
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(answer, writer);
        }
    }

    /** 
     * Loads an Answer from a JSON file by Form UFID and responder UUID.
     * 
     * @param formUFID UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return Loaded Answer, or null if not found
     */
    Answer load(int formUFID, int responderUUID) {
        String filename = ANSWERS_DIR + formUFID + "_" + responderUUID + ".json";
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Answer.class);
        } catch (IOException e) {
            System.err.println("Error loading answer: " + e.getMessage());
            return null;
        }
    }

    /** 
     * Loads all Answers associated with a specific Form UFID.
     * 
     * @param formUFID UFID of the Form
     * @return List of Answers associated with the Form
     */
    ArrayList<Answer> loadByForm(int formUFID) {
        ArrayList<Answer> answers = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(ANSWERS_DIR), formUFID + "_*.json")) {
            for (Path entry : stream) {
                try (FileReader reader = new FileReader(entry.toFile())) {
                    Answer answer = gson.fromJson(reader, Answer.class);
                    answers.add(answer);
                } catch (IOException e) {
                    System.err.println("Error loading answer from file " + entry + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error accessing answers directory: " + e.getMessage());
        }
        return answers;
    }

    /** 
     * Deletes an Answer JSON file by Form UFID and responder UUID.
     * 
     * @param formUFID UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int formUFID, int responderUUID) {
        String filename = ANSWERS_DIR + formUFID + "_" + responderUUID + ".json";
        try {
            return Files.deleteIfExists(Path.of(filename));
        } catch (IOException e) {
            return false;
        }
    }

    /** 
     * Checks if an Answer JSON file exists by Form UFID and responder UUID.
     * 
     * @param formUFID UFID of the Form associated with the Answer
     * @param responderUUID UUID of the Profile who submitted the Answer
     * @return true if the Answer exists, false otherwise
     */
    boolean exists(int formUFID, int responderUUID) {
        String filename = ANSWERS_DIR + formUFID + "_" + responderUUID + ".json";
        return Files.exists(Path.of(filename));
    }

    /**
     * Deletes all Answers from persistent storage.
     * 
     * @return true if all answers were deleted successfully
     */
    boolean deleteAll() {
        try {
            Path answersPath = Paths.get(ANSWERS_DIR);
            if (Files.exists(answersPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(answersPath, "*.json")) {
                    for (Path entry : stream) {
                        Files.delete(entry);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting all answers: " + e.getMessage());
            return false;
        }
    }
}
