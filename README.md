# DISH
## Celebrity Catalog GUI

### Overview
This project is a Java Swing-based GUI application for managing a celebrity catalog. Administrators can add, update, or delete celebrities, while regular users can only view the list. The interface updates dynamically with changes.

### Features
- **Insert Celebrity**: Add a new celebrity with name, profession, biography, and image URL (admin can only do this).
- **Update Celebrity**: Modify details of an existing celebrity (admin can only do this).
- **Delete Celebrity**: Remove a celebrity from the catalog (admin can only do this).
- **Dynamic UI**: The celebrity list updates in real-time.
- **User Roles**: Admins can edit the catalog; regular users can just view it.
- **Authentication**: There is Separate sign-in for admins and users, with a sign-up option for new users.

### Requirements
- **Operating System**: Any system with a terminal (e.g., Windows with WSL, MinGW, Linux, macOS)
- **Git**: Required to clone the repository.
- **IntelliJ IDEA**: Used to open and run the project(can be any edition no premium edition needed).
- **Java**: JDK 8 or higher (e.g., OpenJDK 23).

### Setup Instructions
1. **Clone the Repository**:
   - Open a terminal, go to your desired location and run: git clone <repository-url>
   - Replace `<repository-url>` with the repository’s URL (e.g., from GitHub).

2. **Open in IntelliJ**:
- Launch IntelliJ IDEA.
- Go to `File > Open` and select the cloned folder.
- Allow IntelliJ to load the project.

3. **Configure Java**:
- If you are prompted, set the JDK to`java-23-openjdk`).
- Check `File > Project Structure > SDKs` to confirm the proper JDK is selected.

4. **main.java.Run the Application**:
- Locate `main.java.Run.java` in the `src` folder.
- Right-click it and select `main.java.Run` to start the app.

### Usage
- **The Initial Screen**: Displays "Sign In (Users)", "Sign Up (Users)", and "Admin Sign In".
- **Admin Login**: Use `Username: admin` / `Password: admin123` in "Admin Sign In" to access editing features.
- **User Login**: Sign up with a new username and password, then use "Sign In (Users)" to view the list.
- **Sign Out**: Click "Sign Out" to return to the initial screen.


