package view;

import app.Photos;
import app.model.UserList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdminHomeController {

  @FXML
  private TextField usernameField;

  private Photos app;
  private UserList userList;

  public void init(Photos app, UserList userList) {
    this.app = app;
    this.userList = userList;
  }

  @FXML
  public void handleCreateUser (){
    try {
      userList.addUser(usernameField.getText().trim());
    } catch (Exception e) {
      System.out.println("Error creating user: " + e.getMessage());
    }

  }

}
