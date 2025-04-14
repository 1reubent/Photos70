package view;

import app.Photos;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controller for the login view.
 * Handles user login functionality, including admin and regular user login.
 * @author Reuben Thomas, Ryan Zaken
 */
public class LoginController {

  /**
   * Text field for entering the username.
   */
  @FXML
  private TextField usernameField;

  /**
   * Label for displaying error messages.
   */
  @FXML
  private Label errorLabel;

  /**
   * Reference to the main {@link Photos} application instance.
   */
  private Photos app;

  /**
   * Sets the main application instance for this controller.
   *
   * @param app the main application instance
   */
  public void setApp(Photos app) {
    this.app = app;
    System.out.println("LoginController Stock albums: " + app.getUserList().getUser("stock").getAlbumNames());
  }

  /**
   * Handles the Enter key press event in the username field.
   * If the Enter key is pressed, it triggers the login process.
   *
   * @param event the key event
   */
  @FXML
  private void handleEnterKey(javafx.scene.input.KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      handleLogin();
    }
  }

  /**
   * Handles the login action.
   * Validates the username and navigates to the appropriate view (admin or user).
   */
  @FXML
  private void handleLogin() {
    String username = usernameField.getText().trim();
    usernameField.clear();

    if (username.isEmpty()) {
      errorLabel.setText("Username cannot be empty.");
      return;
    }

    if (username.equals("admin")) {
      // Clear error label
      errorLabel.setText("");
      openAdminView();
      return;
    }

    if (!app.getUserList().hasUser(username)) {
      errorLabel.setText("User not found.");
      return;
    }

    errorLabel.setText("");
    openUserView(username);
  }

  /**
   * Opens the admin view.
   * Switches the scene to the admin home view.
   */
  private void openAdminView() {
    try {
      app.switchToAdminHomeView((Stage) usernameField.getScene().getWindow());
    } catch (Exception e) {
      // Handle exception
      showErrorDialog("Error loading Admin View: " + e.getMessage());
    }
  }

  /**
   * Opens the user view.
   * Switches the scene to the user home view for the specified username.
   *
   * @param username the username of the user
   */
  private void openUserView(String username) {
    try {
      app.switchToUserHomeView((Stage) usernameField.getScene().getWindow(), username);
    } catch (Exception e) {
      showErrorDialog("Error loading User view: " + e.getMessage());
    }
  }

  /**
   * Displays an error dialog with the specified message.
   *
   * @param message the error message to display
   */
  private void showErrorDialog(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.setContentText(message);
    alert.showAndWait();
  }
}