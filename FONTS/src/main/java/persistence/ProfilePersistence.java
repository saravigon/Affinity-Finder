package persistence;

import com.google.gson.*;
import domain.classes.Profile;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Handles persistence operations for Profile entities
 */
class ProfilePersistence {
    
    /**
     * Directory to store Profile JSON files
     */
    private static final String PROFILES_DIR = "data/profiles/";
    /**
     * Gson instance for JSON serialization/deserialization
     */
    private final Gson gson;
    
    /**
     * Constructor initializes the ProfilePersistence with a Gson instance.
     * 
     * @param gson Gson instance for JSON serialization/deserialization
     */
    ProfilePersistence(Gson gson) {
        this.gson = gson;
        createDirectoryIfNotExists();
    }
    
    /**
     * Creates the profiles directory if it does not exist.
     */
    private void createDirectoryIfNotExists() {
        try {
            Files.createDirectories(Paths.get(PROFILES_DIR));
        } catch (IOException e) {
            System.err.println("Error creating profiles directory: " + e.getMessage());
        }
    }
    
    /** 
     * Saves a Profile to a JSON file.
     * 
     * @param profile Profile to save
     * @throws IOException if an I/O error occurs
     */
    void save(Profile profile) throws IOException {
        String filename = PROFILES_DIR + profile.getUUID() + ".json";
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(profile, writer);
        }
    }
    
    /** 
     * Loads a Profile from a JSON file by UUID.
     * 
     * @param uuid UUID of the Profile to load
     * @return Loaded Profile, or null if not found
     */
    Profile load(int uuid) {
        String filename = PROFILES_DIR + uuid + ".json";
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Profile.class);
        } catch (IOException e) {
            return null;
        }
    }
    
    /** 
     * Deletes a Profile JSON file by UUID.
     * 
     * @param uuid UUID of the Profile to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean delete(int uuid) {
        String filename = PROFILES_DIR + uuid + ".json";
        try {
            return Files.deleteIfExists(Path.of(filename));
        } catch (IOException e) {
            return false;
        }
    }
    
    /** 
     * Checks if a Profile JSON file exists by UUID.
     * 
     * @param uuid UUID of the Profile to check
     * @return true if the Profile exists, false otherwise
     */
    boolean exists(int uuid) {
        String filename = PROFILES_DIR + uuid + ".json";
        return Files.exists(Path.of(filename));
    }
    
    /** 
     * Loads all Profiles from the profiles directory.
     * 
     * @return List of all loaded Profiles
     */
    List<Profile> loadAll() {
        List<Profile> profiles = new ArrayList<>();
        try {
            Files.list(Paths.get(PROFILES_DIR))
                .filter(path -> path.toString().endsWith(".json"))
                .forEach(path -> {
                    try (FileReader reader = new FileReader(path.toFile())) {
                        Profile profile = gson.fromJson(reader, Profile.class);
                        if (profile != null) {
                            profiles.add(profile);
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading profile: " + path);
                    }
                });
        } catch (IOException e) {
            System.err.println("Error listing profiles: " + e.getMessage());
        }
        return profiles;
    }

    /** 
     * Loads a Profile by username.
     * 
     * @param username Username of the Profile to load
     * @return Loaded Profile, or null if not found
     */
    public Profile loadByUsername(String username) {
        List<Profile> profiles = loadAll();
        return profiles.stream()
            .filter(p -> p.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    /**
     * Deletes all Profiles from persistent storage.
     * 
     * @return true if all profiles were deleted successfully
     */
    boolean deleteAll() {
        try {
            Path profilesPath = Paths.get(PROFILES_DIR);
            if (Files.exists(profilesPath)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(profilesPath, "*.json")) {
                    for (Path entry : stream) {
                        Files.delete(entry);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting all profiles: " + e.getMessage());
            return false;
        }
    }
}