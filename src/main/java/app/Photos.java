package app;

import app.model.Photo;
import app.model.User;
import app.model.UserList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import view.LoginController;
import view.UserHomeController;
import view.AdminHomeController;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Photos extends Application {
  private UserList userList;
  private User admin;
  private Scene loginScene;
  private Scene adminHomeScene;
  private Map<String, Pair<Scene, UserHomeController>> userHomeScenes = new HashMap<>();
  private static String userListFilePath = null; // initialized in start method
  private Map<String, Object> userData = new HashMap<>();

  @Override
  public void start(Stage stage) throws IOException {
    /* INITIALIZE USERS.DAT */

    // Get the resource URL for users.dat
    java.net.URL resourceUrl = getClass().getResource("/users.dat");
    if (resourceUrl == null) {
      // File doesn't exist in resources, create it
      File resourcesDir = new File(getClass().getResource("/").getPath());
      File usersFile = new File(resourcesDir, "users.dat");
      usersFile.createNewFile();
      userListFilePath = usersFile.getPath();
      userList = new UserList();
    } else {
      // File exists, set the path and load user list
      userListFilePath = resourceUrl.getPath();
      userList = loadUserList();
    }

    /* INITIALIZE STOCK USER */
    if (!userList.hasUser("stock")) {
      initializeStockPhotos();
      // print that stock user has been initialized
      System.out.println("Stock user initialized");
    }

    System.out.println("Users loaded: " + userList.getAllUsers().keySet());
    System.out.println("Stock user and album initialized: " + userList.getUser("stock").getAlbumNames());

    FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("/view/login-view.fxml"));
    loginScene = new Scene(fxmlLoader.load(), 320, 240);
    LoginController loginController = fxmlLoader.getController();
    loginController.setApp(this); // Pass the Photos instance to the LoginController
    stage.setTitle("Photo Album");
    stage.setScene(loginScene);
    stage.show();
  }

  public void initializeStockPhotos() {
    User stock_user = userList.addUser("stock");
    stock_user.addAlbum("stock");
    String[] stockPhotoPaths = {
        "data/stock1.jpg",
        "data/stock2.jpg",
        "data/stock3.jpg",
        "data/stock4.jpg",
        "data/stock5.jpg"
    };

    for (String path : stockPhotoPaths) {
      stock_user.getAlbum("stock").addPhoto(new Photo(path));
    }
    try {
      saveUserList();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
      Scene userHomeScene = new Scene(userHomeLoader.load(), 640, 650);
      UserHomeController userHomeController = userHomeLoader.getController();
      userHomeController.init(this, userList.getUser(username));
      userHomeScenes.put(username, new Pair<>(userHomeScene, userHomeController));
    } else {
      // Run populate albums before switching to the scene
      userHomeScenes.get(username).getValue().populateAlbums();
    }
    stage.setScene(userHomeScenes.get(username).getKey());
  }

  // TODO: Implement the admin home view
  // its init method must take the app as a parameter, and the admin user
  // need to save admin user to disk
  // need to save admin controller and scene.
  public void switchToAdminHomeView(Stage stage) throws IOException {
    if (adminHomeScene == null) {
      FXMLLoader adminHomeLoader = new FXMLLoader(getClass().getResource("/view/admin-home-view.fxml"));
      adminHomeScene = new Scene(adminHomeLoader.load());
      AdminHomeController adminHomeController = adminHomeLoader.getController();

      // pass the user list
      adminHomeController.init(this, userList);

    }
    stage.setScene(adminHomeScene);
  }

  public void switchToLoginView(Stage stage) {
    stage.setScene(loginScene);
  }

  @Override
  public void stop() throws IOException {
    saveUserList();
  }

  /* PUBLIC METHODS TO MANAGE USER DATA */
  public void addUserData(String key, Object value) {
    userData.put(key, value);
  }

  public Object getUserData(String key) {
    return userData.get(key);
  }

  public List<String> getUserDataKeys() {
    return new ArrayList<>(userData.keySet());
  }

  public void clearUserData() {
    userData.clear();
  }
  /* PERSISTENCE METHODS */
  private void saveUserList() throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userListFilePath))) {
      out.writeObject(userList);
    }
  }

  private UserList loadUserList() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userListFilePath))) {
      return (UserList) in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}