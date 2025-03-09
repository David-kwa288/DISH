# DISH
## Celebrity Catalog GUI

### Overview
This project is a Java Swing-based GUI application for managing a celebrity catalog. Administrators can add, update, or delete celebrities, while regular users can only view the list. The interface updates dynamically with changes.

### Features
- **Insert Celebrity**: Add a new celebrity with name, profession, biography, and image URL (admin only).
- **Update Celebrity**: Modify details of an existing celebrity (admin only).
- **Delete Celebrity**: Remove a celebrity from the catalog (admin only).
- **Dynamic UI**: The celebrity list updates in real-time.
- **User Roles**: Admins can edit the catalog; regular users can view it.
- **Authentication**: Separate sign-in for admins and users, with a sign-up option for new users.

### Requirements
- **Ubuntu**: This guide assumes an Ubuntu environment.
- **Git**: Required to clone the repository.
- **IntelliJ IDEA**: Used to open and run the project.
- **Java**: JDK 8 or higher (e.g., OpenJDK 23).

### Setup Instructions
1. **Clone the Repository**:
   - Open a terminal in Ubuntu and run: git clone <repository-url>
   - Replace `<repository-url>` with the repository’s URL (e.g., from GitHub).

2. **Open in IntelliJ**:
- Launch IntelliJ IDEA.
- Go to `File > Open` and select the cloned folder.
- Allow IntelliJ to load the project.

3. **Configure Java**:
- If prompted, set the JDK (e.g., `/usr/lib/jvm/java-23-openjdk`).
- Check `File > Project Structure > SDKs` to confirm the JDK is selected.

4. **Run the Application**:
- Locate `Main.java` in the `src` folder.
- Right-click it and select `Run 'Main.main()'` to start the app.

### Usage
- **Initial Screen**: Displays "Sign In (Users)", "Sign Up (Users)", and "Admin Sign In".
- **Admin Login**: Use `admin` / `admin123` in "Admin Sign In" to access editing features.
- **User Login**: Sign up with a new username and password, then use "Sign In (Users)" to view the list.
- **Sign Out**: Click "Sign Out" to return to the initial screen.

### Project Structure
src/
├── model/
│   └── Celebrity.java         (Defines the celebrity data model)
├── view/
│   ├── CelebrityGUI.java     (Handles the main UI)
│   └── CelebrityPanel.java   (Renders individual celebrity entries)
├── controller/
│   └── CelebrityController.java (Manages application logic)
└── Main.java                 (Application entry point)
