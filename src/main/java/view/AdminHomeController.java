package view;

import app.Photos;
import app.model.UserList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for the Admin Home view.
 * This class handles user management functionalities such as creating, deleting users,
 * and logging out from the admin interface.
 * @author Reuben Thomas, Ryan Zaken
 */
public class AdminHomeController {

    /**
     * TextField for entering the username of a new user.
     */
    @FXML
    private TextField usernameField;

    /**
     * ListView for displaying the list of existing users.
     */
    @FXML
    private ListView<String> userListView;

    /**
     * Reference to the main Photos application instance.
     */
    private Photos app;

    /**
     * Reference to the UserList model, which manages the list of users.
     */
    private UserList userList;

    /**
     * ObservableList for binding the user list to the ListView.
     */
    private ObservableList<String> observableUserList;

    /**
     * Displays a warning alert with the specified message.
     *
     * @param message The warning message to display.
     */
    public void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }

    /**
     * Initializes the controller with the application and user list.
     *
     * @param app      The main Photos application instance.
     * @param userList The UserList model containing the list of users.
     */
    public void init(Photos app, UserList userList) {
        this.app = app;
        this.userList = userList;

        // Initialize the ObservableList and bind it to the ListView
        observableUserList = FXCollections.observableArrayList(userList.getAllUsernames());
        userListView.setItems(observableUserList);
    }

    /**
     * Populates the ListView with the updated and sorted list of usernames.
     */
    public void populateUsers() {
        // Clear and repopulate the ObservableList with updated and sorted usernames
        observableUserList.setAll(userList.getAllUsernames());
        FXCollections.sort(observableUserList); // Sort the list alphabetically
    }

    /**
     * Checks if the specified username already exists in the user list.
     *
     * @param username The username to check.
     * @return True if the username exists, false otherwise.
     */
    private boolean isDuplicateUser(String username) {
        return userList.getAllUsernames().contains(username);
    }

    /**
     * Handles the creation of a new user.
     * Validates the username and adds it to the user list if valid.
     */
    @FXML
    public void handleCreateUser() {
        try {
            String username = usernameField.getText().trim();

            if (username.equals("admin")) {
                showWarning("Cannot create user with username 'admin'.");
                return;
            } else if (isDuplicateUser(username)) {
                showWarning("User already exists. Please choose a different username.");
                return;
            } else if (!username.isEmpty()) {
                userList.addUser(username); // Add user to the backend list
                populateUsers(); // Refresh the ObservableList
                usernameField.clear(); // Clear the input field
            }
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    /**
     * Handles the deletion of a selected user.
     * Validates the selection and removes the user from the user list if valid.
     */
    @FXML
    public void handleDeleteUser() {
        try {
            String selectedUsername = userListView.getSelectionModel().getSelectedItem();

            if (selectedUsername == null) {
                showWarning("Please select a user to delete.");
                return;
            }

            if (selectedUsername.equals("stock")) {
                showWarning("Cannot delete stock album!");
                return;
            } else if (selectedUsername != null) {
                userList.deleteUser(selectedUsername); // Remove user from the backend list
                populateUsers(); // Refresh the ObservableList
            }
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * Handles the logout action.
     * Switches the view back to the login screen.
     */
    @FXML
    public void handleLogout() {
        app.switchToLoginView((Stage) userListView.getScene().getWindow());
    }
}
