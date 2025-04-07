package view;

import app.model.UserList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private Label errorLabel;

  private UserList userList;

  public void initialize() throws IOException {
    // Load user list from disk or initialize empty
    userList = UserList.load(getClass().getResource("/users.json").getPath());
    System.out.println("LoginController Users loaded: " + userList.getAllUsers().keySet());
    System.out.println("LoginController Stock albums: " + userList.getUser("stock").getAlbums().keySet() );
  }

  public void setUserList(UserList userList) {
    this.userList = userList;
  }

  @FXML
  private void handleLogin() {
    String username = usernameField.getText().trim();

    if (username.isEmpty()) {
      errorLabel.setText("Username cannot be empty.");
      return;
    }

    if (username.equals("admin")) {
      openAdminView();
      return;
    }

    if (!userList.hasUser(username)) {
      errorLabel.setText("User not found.");
      return;
    }

    openUserView(username);
  }

  private void openAdminView() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin-view.fxml"));
      Scene scene = new Scene(loader.load());
      Stage stage = (Stage) usernameField.getScene().getWindow();
      stage.setScene(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void openUserView(String username) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user-home-view.fxml"));
      Scene scene = new Scene(loader.load());

      // Pass username to the controller
      UserHomeController controller = loader.getController();
      System.out.println("stock user albums: " + userList.getUser(username).getAlbums().keySet());
      controller.init(userList.getUser(username));

      Stage stage = (Stage) usernameField.getScene().getWindow();
      stage.setScene(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}