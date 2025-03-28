# Celebrity Catalog GUI - Developer Documentation

## Build Instructions

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 (as specified in `pom.xml`).
- **Maven**: Version 3.6+ (tested with 3.9.6) for dependency management and build automation.
- **Git**: For cloning the repository.

### Steps to Build and Run
1. **Clone the Repository**:
   ```bash
   git clone <https://github.com/David-kwa288/DISH>
   cd celebrity-catalog-gui
   ```

2. **Install Dependencies**:
   The `pom.xml` specifies JUnit 5.10.0 for testing:
   ```bash
   mvn install
   ```
   This downloads dependencies to your local Maven repository (`~/.m2`).

3. **Compile the Project**:
   Uses JDK 17 as per `<maven.compiler.source>` and `<maven.compiler.target>`:
   ```bash
   mvn clean compile
   ```
   Output goes to `target/classes/`.

4. **Run the Application**:
   The entry point is `Run.java` in `src/main/java/view/`:
   ```bash
   mvn exec:java -Dexec.mainClass="main.java.view.Run"
   ```

5. **Run Unit Tests**:
   Uses Surefire Plugin 3.1.2 to execute JUnit 5 tests in `src/test/java/`:
   ```bash
   mvn test
   ```
   Results are in `target/surefire-reports/`.

### Build Script (`run.sh`):
```bash
#!/bin/bash
mvn clean install compile exec:java -Dexec.mainClass="main.java.view.Run"
```
Usage:
```bash
chmod +x run.sh && ./run.sh  # (Linux/Mac)
```
- If running in an IDE (e.g., IntelliJ), import as a Maven project and click the "Run" button on `Run.java`.

## Technical Structure
The project follows a **Model-View-Controller (MVC)** architecture:

### Model (`Celebrity.java`):
- **Location**: `src/main/java/model/`
- **Role**: Defines the data structure for celebrities.
- **Fields**: `name`, `profession`, `biography`, `achievements`, `images`, `videos`.
- **Acts as**: Passive data container, accessed by the controller.

### View (`CelebrityGUI.java`, `CelebrityPanel.java`):
- **Location**: `src/main/java/view/`
- **Role**: Implements the UI using Java Swing.
- **Main Components**:
  - `CelebrityGUI.java`: Manages the main window, sidebar, and content panels.
  - `CelebrityPanel.java`: Renders individual celebrity cards.

### Controller (`CelebrityController.java`):
- **Location**: `src/main/java/controller/`
- **Role**: Orchestrates logic and data flow between model and view.
- **Manages**:
  - Celebrities list (all entries).
  - Favorites list (user-selected entries).
  - Authentication, CRUD, favorites, and filtering.
- **Updates**: Refreshes `celebListPanel` in the UI.

### Class Interactions:
1. `Run` creates a `CelebrityController` and `CelebrityGUI`, starting the app.
2. `CelebrityGUI` passes itself to `CelebrityController` for UI updates.
3. User actions trigger controller methods, which modify `Celebrity` objects and update the view.

### Example Flow:
1. `Run` calls `CelebrityGUI.show()`.
2. User signs in via `CelebrityGUI`.
3. `CelebrityController.handleSignIn` validates credentials, loads celebrities, and updates the UI.

## Testing Structure:
- **Location**: `src/test/java/controller/`
- **Tests**:
  - `CelebrityControllerTest.java`: Uses JUnit 5 for unit testing.
- **Executed via**:
  ```bash
  mvn test
  ```

## Summary of Classes:

### `Celebrity.java` (Model)
- **Role**: Data model for celebrity entries.
- **Fields**: `name`, `profession`, `biography`, `achievements`, `images`, `videos`.
- **Methods**: Getters (`getName()`, `getProfession()`, etc.).

### `CelebrityGUI.java` (View)
- **Role**: Main UI frame and layout manager.
- **Methods**:
  - `show()`: Displays the GUI.
  - `showMainContent(String role)`: Switches UI based on role.
  - `updateSidebarButtons(String role)`: Updates sidebar options.

### `CelebrityPanel.java` (View)
- **Role**: Displays individual celebrity cards.
- **Methods**:
  - `createCelebrityPanel(Celebrity, CelebrityController)`: Builds a card with name, image, and favorite button.

### `CelebrityController.java` (Controller)
- **Role**: Manages application logic and state.
- **Methods**:
  - `handleSignIn(String username, String password, boolean isAdmin)`: Authenticates users.
  - `insertCelebrity()`: Adds a celebrity via dialog.
  - `filterCelebrities(String name, String profession, String award)`: Filters the list.

### `Run.java` (Entry Point)
- **Methods**:
  - `main(String[] args)`: Initializes controller and GUI.

### `CelebrityControllerTest.java` (Test)
- **Methods**:
  - `testHandleGuestSignInLoadsCelebrities()`: Tests guest sign-in.
  - `testAddToFavorites()`: Tests favorites functionality.
