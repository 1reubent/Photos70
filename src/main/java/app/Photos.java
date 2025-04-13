package app;

import app.model.Album;
import app.model.Photo;
import app.model.User;
import app.model.UserList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.LoginController;
import view.UserHomeController;
import view.AdminHomeController;

import java.io.*;

/**
 * The main application class for the Photos application.
 * This class initializes the application, manages user data, and handles view switching.
 * @author Reuben Thomas, Ryan Zaken
 */
public class Photos extends Application {

  /**
   * The list of users in the application.
   */
  private UserList userList;

  /**
   * The login scene for the application.
   */
  private Scene loginScene;

  /**
   * The file path for storing user data.
   */
  private static String userListFilePath = null;

  @Override
  public void start(Stage stage) throws IOException {
    /* INITIALIZE USERS.DAT */

    // Get the resource URL for users.dat
    java.net.URL resourceUrl = getClass().getResource("/photos_app_data.dat");
    if (resourceUrl == null) {
      // File doesn't exist in resources, create it
      File resourcesDir = new File(getClass().getResource("/").getPath());
      File usersFile = new File(resourcesDir, "photos_app_data.dat");
      usersFile.createNewFile();
      userListFilePath = usersFile.getPath();
      userList = new UserList();
    } else {
      // File exists, set the path and load user list
      userListFilePath = resourceUrl.getPath();
      userList = loadAllUserData();
    }

    /* INITIALIZE STOCK USER */
    if (!userList.hasUser("stock")) {
      initializeStockUser();
      // print that stock user has been initialized
    }


    /*LOAD LOGIN VIEW*/
    FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("/view/login-view.fxml"));
    loginScene = new Scene(fxmlLoader.load(), 320, 240);
    LoginController loginController = fxmlLoader.getController();
    loginController.setApp(this); // Pass the Photos instance to the LoginController
    stage.setTitle("Photo Album");
    stage.setScene(loginScene);
    stage.show();
  }

  /**
   * Initializes the stock user and their default album with stock photos.
   */
  public void initializeStockUser() {
    User stock_user = userList.addUser("stock");
    Album stock_album = stock_user.addAlbum("stock");
    String[] stockPhotoPaths = {
            "data/stock1.jpg",
            "data/stock2.jpg",
            "data/stock3.jpg",
            "data/stock4.jpg",
            "data/stock5.jpg"
    };

    Album.setInitializingStock(true);
    for (String path : stockPhotoPaths) {
      stock_album.addPhoto(new Photo(path));
    }
    Album.setInitializingStock(false);

    saveAllUserData();
  }

  /**
   * Retrieves the user list.
   *
   * @return the user list
   */
  public UserList getUserList() {
    return userList;
  }

  /**
   * Switches to the user home view for the specified user.
   *
   * @param stage    the stage to display the view
   * @param username the username of the user
   * @throws IOException if the view cannot be loaded
   */
  public void switchToUserHomeView(Stage stage, String username) throws IOException {
    try {
        FXMLLoader userHomeLoader = new FXMLLoader(getClass().getResource("/view/user-home-view.fxml"));
        Scene userHomeScene = new Scene(userHomeLoader.load(), 640, 650);
        UserHomeController userHomeController = userHomeLoader.getController();
        userHomeController.init(this, userList.getUser(username));
        stage.setScene(userHomeScene);
        stage.setTitle("Photo Album " + "(User: " + username + ")"); // Set the title to the username
    } catch (IOException e) {
        showError("Failed to load user home view: " + e.getMessage());
    }
  }

  /**
   * Switches to the admin home view.
   *
   * @param stage the stage to display the view
   * @throws IOException if the view cannot be loaded
   */
  public void switchToAdminHomeView(Stage stage) throws IOException {
   try {
        FXMLLoader adminHomeLoader = new FXMLLoader(getClass().getResource("/view/admin-home-view.fxml"));
        Scene adminHomeScene = new Scene(adminHomeLoader.load());
        AdminHomeController adminHomeController = adminHomeLoader.getController();

        // pass the user list
        adminHomeController.init(this, userList);
        stage.setScene(adminHomeScene);
    } catch (IOException e) {
        showError("Failed to load admin home view: " + e.getMessage());
    }
  }

  /**
   * Switches to the login view.
   *
   * @param stage the stage to display the view
   */
  public void switchToLoginView(Stage stage) {
   try {
        stage.setScene(loginScene);
    } catch (Exception e) {
        showError("Failed to switch to login view: " + e.getMessage());
    }
  }

  @Override
  public void stop() throws IOException {
    saveAllUserData();
  }

  /**
   * Saves all user data to the file.
   */
  private void saveAllUserData() {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userListFilePath))) {
      out.writeObject(userList);
    }catch (Exception e) {
      // Handle the exception
      showError("Failed to save user data: " + e.getMessage());
    }
  }

  /**
   * Loads all user data from the file.
   *
   * @return the loaded user list, or null if an error occurs
   */
  private UserList loadAllUserData() {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userListFilePath))) {
      userList = (UserList) in.readObject();
      return userList;
    } catch (IOException | ClassNotFoundException e) {
      // Handle the exception
      showError("Failed to load user data: " + e.getMessage());
      return null;
    }
  }

  /**
   * Displays an error message in a dialog.
   *
   * @param message the error message to display
   */
  public void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * The main entry point for the application.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}