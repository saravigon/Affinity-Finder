# FONTS - Source Code Directory

This directory contains all the source code, build configuration, and compiled artifacts for the Affinity Finder application.

## 📁 Directory Structure

### Build Configuration

- **`build.gradle`** - Gradle build configuration file
- **`settings.gradle`** - Gradle project settings
- **`gradlew`** / **`gradlew.bat`** - Gradle wrapper scripts for Linux/macOS and Windows

### Source Code - `src/`

#### `src/main/java/`
Production Java source code organized by layers:

- **`app/`** - Application entry point and main class
- **`domain/`** - Domain layer (business logic, models, controllers)
  - `classes/` - Domain entities and core classes
  - `controllers/` - Domain logic controllers
  - `exceptions/` - Custom exception definitions
- **`persistence/`** - Data persistence layer
- **`presentation/`** - User interface and presentation layer
  - `classes/` - UI components and views
  - `controllers/` - Presentation controllers

#### `src/main/resources/`
Application resources:
- OpenNLP model files (`.bin`, `.dict`)
- Application assets (logos, images)
- Configuration files

### Build Output - `build/`

Generated during compilation:
- **`classes/`** - Compiled Java classes
- **`libs/`** - Generated JAR files
- **`resources/`** - Processed resources
- **`tmp/`** - Temporary build files

### Gradle Dependencies - `gradle/`

Contains Gradle wrapper configuration and dependencies.

### Distribution Package - `GradlePOC-1.0-SNAPSHOT/`

Executable distribution with launcher scripts and dependencies.

---

## Quick Reference

- **Build the project**: `./gradlew build`
- **Run the application**: `./gradlew run`
- **Run tests**: `./gradlew test`

For detailed build instructions, see [BUILD.md](BUILD.md) in the project root.
