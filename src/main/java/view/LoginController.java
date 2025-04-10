package view;

import app.Photos;
import app.model.UserList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private Label errorLabel;

  private Photos app;

  public void setApp(Photos app) {
    this.app = app;
    System.out.println("LoginController Stock albums: " + app.getUserList().getUser("stock").getAlbumNames());
  }

  @FXML
  private void handleEnterKey(javafx.scene.input.KeyEvent event) {
    if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
      handleLogin();
    }
  }

  @FXML
  private void handleLogin() {
    String username = usernameField.getText().trim();
    usernameField.clear();

    if (username.isEmpty()) {
      errorLabel.setText("Username cannot be empty.");
      return;
    }

    if (username.equals("admin")) {
      //clear error label
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

  private void openAdminView() {
    try {
      app.switchToAdminHomeView((Stage) usernameField.getScene().getWindow());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void openUserView(String username) {
    try {
      app.switchToUserHomeView((Stage) usernameField.getScene().getWindow(), username);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}