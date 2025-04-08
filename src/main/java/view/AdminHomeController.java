package view;

import app.Photos;
import app.model.User;
import app.model.UserList;

public class AdminHomeController {

  private Photos app;
  private UserList userList;

  public void init(Photos app, UserList userList) {
    this.app = app;
    this.userList = userList;
  }

}
