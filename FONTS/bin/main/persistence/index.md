# Persistence Layer

## Summary
The persistence layer handles all data storage and retrieval operations, acting as the bridge between the domain layer and the file system. It manages the serialization and deserialization of domain objects to/from JSON files.

## Architecture
The persistence layer follows a **Facade pattern** where `PersistanceManager` serves as the single entry point from the domain layer and delegates operations to specialized persistence classes.



## Classes

- **PersistanceManager.java** - Facade class that provides a unified interface for all persistence operations. Acts as the single point of entry from the domain layer and delegates to specialized persistence classes.

- **ProfilePersistence.java** - Handles persistence operations for Profile entities (save, load, delete, exists). Manages the `data/profiles/` directory and JSON serialization.

- **FormPersistence.java** - Handles persistence operations for Form entities. Manages the `data/forms/` directory and provides methods to load forms by ID or title.

- **AnswerPersistence.java** - Handles persistence operations for Answer entities. Manages the `data/answers/` directory and the relationship between forms and user responses.

## Usage

````java
// Initialize the persistence manager
PersistanceManager pm = new PersistanceManager();

// Profile operations
pm.saveProfile(profile);
Profile p = pm.loadProfile(uuid);

// Form operations
pm.saveForm(form);
Form f = pm.loadForm(ufid);

// Answer operations
pm.saveAnswer(answer);
Answer a = pm.loadAnswer(formUfid, userUuid);