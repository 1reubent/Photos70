package app;

import app.model.User;
import app.model.UserList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginController;
import view.UserHomeController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Photos extends Application {
  private UserList userList;
  private User admin;
  private Scene loginScene;
  private Scene adminHomeScene;
  private Map<String, Scene> userHomeScenes = new HashMap<>();

  @Override
  public void start(Stage stage) throws IOException {
    stage.setUserData(this); // used in UserHomeController to access the app instance
    // Load user list from disk or initialize empty
    try {
      userList = UserList.load(getClass().getResource("/users.json").getPath());
    } catch (IOException e) {
      userList = new UserList();
    }
    userList.initializeStockPhotos();
    System.out.println("Users loaded: " + userList.getAllUsers().keySet());

    FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("/view/login-view.fxml"));
    loginScene = new Scene(fxmlLoader.load(), 320, 240);
    LoginController loginController = fxmlLoader.getController();
    loginController.setApp(this); // Pass the Photos instance to the LoginController
    stage.setTitle("Photo Album");
    stage.setScene(loginScene);
    stage.show();
  }

  public UserList getUserList() {
    return userList;
  }

  public User getAdmin() {
    return admin;
  }

  public void switchToUserHomeView(Stage stage, String username) throws IOException {
    if (!userHomeScenes.containsKey(username)) {
      FXMLLoader userHomeLoader = new FXMLLoader(getClass().getResource("/view/user-home-view.fxml"));
      Scene userHomeScene = new Scene(userHomeLoader.load());
      UserHomeController userHomeController = userHomeLoader.getController();
      userHomeController.init(this, userList.getUser(username));
      userHomeScenes.put(username, userHomeScene);
    }
    stage.setScene(userHomeScenes.get(username));
  }

  //    TODO: Implement the admin home view
  //      its init method must take the app as a parameter, and the admin user
  public void switchToAdminHomeView(Stage stage) throws IOException {
    if (adminHomeScene == null) {
      FXMLLoader adminHomeLoader = new FXMLLoader(getClass().getResource("/view/admin-home-view.fxml"));
      adminHomeScene = new Scene(adminHomeLoader.load());
    }
    stage.setScene(adminHomeScene);
  }

  public void switchToLoginView(Stage stage) {
    stage.setScene(loginScene);
  }

  @Override
  public void stop() throws IOException {
    UserList.save(userList, getClass().getResource("/users.json").getPath());
  }

  public static void main(String[] args) {
    launch(args);
  }
}