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

  //Write a populate_List method, to refresh the list, and call it in the Init Method
  //Make sure to call again when any updates are made (Add/Delete User)

  @FXML
  public void handleCreateUser (){
    try {
      userList.addUser(usernameField.getText().trim());
    } catch (Exception e) {
      System.out.println("Error creating user: " + e.getMessage());
    }

  }

}
