package view;

import app.Photos;
import app.model.UserList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AdminHomeController {

    @FXML
    private TextField usernameField;

    @FXML
    private ListView<String> userListView; // Ensure this matches the fx:id in your FXML

    private Photos app;
    private UserList userList;
    private ObservableList<String> observableUserList;

    public void init(Photos app, UserList userList) {
        this.app = app;
        this.userList = userList;

        // Initialize the ObservableList and bind it to the ListView
        observableUserList = FXCollections.observableArrayList(userList.getAllUsernames());
        userListView.setItems(observableUserList);
    }

    public void populateUsers() {
        // Clear and repopulate the ObservableList with updated usernames
        observableUserList.setAll(userList.getAllUsernames());
    }

    @FXML
    public void handleCreateUser() {
        try {
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                userList.addUser(username); // Add user to the backend list
                populateUsers(); // Refresh the ObservableList
                usernameField.clear(); // Clear the input field
            }
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteUser() {
        try {
            String selectedUsername = userListView.getSelectionModel().getSelectedItem();
            if (selectedUsername != null) {
                userList.deleteUser(selectedUsername); // Remove user from the backend list
                populateUsers(); // Refresh the ObservableList
            }
        } catch (Exception e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

}
