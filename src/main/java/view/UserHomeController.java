package view;

import app.Photos;
import app.model.Album;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;

import app.model.User;

import java.io.IOException;


public class UserHomeController {
  @FXML
  private ListView<String> albumList;
  @FXML
  private ListView<String> photoList;
  @FXML
  private Label statusLabel;
  private User user;
  private Photos app;

  public void init(Photos app, User user) {
    this.user = user;
    this.app = app;
    System.out.println("UserHomeController initialized with user: " + user.getUsername());
    System.out.println("Albums: " + user.getAlbums());

    populateAlbums();
    // Additional initialization code here
    System.out.println("User logged in: " + user.getUsername());
  }

  public void populateAlbums() {
    System.out.println(user.getUsername());
    System.out.println(user.getAlbums().keySet());
    albumList.getItems().clear();
    for (Album album : user.getAlbums().values()) {
      albumList.getItems().add(String.format("Name: %s | %d photos | Date Range: %s",
              album.getName(),
              album.getPhotoCount(),
              album.getDateRange()));
    }
    System.out.println("Albums populated: " + albumList.getItems());
  }

  @FXML
  public void handleLogout() {
    // data changes are saved to disk when the app is closed. until then, they are held in memory.
    app.clearUserData();
    app.switchToLoginView((Stage) albumList.getScene().getWindow());
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message);
    alert.showAndWait();
  }


  @FXML
  public void handleCreateAlbum() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Create Album");
    dialog.setHeaderText("Enter album name:");
    dialog.setContentText("Name:");
    dialog.showAndWait().ifPresent(name -> {
      if(!user.addAlbum(name)){
        showError("Album already exists!");
        return;
      };
      populateAlbums();
    });
  }

  @FXML
  public void handleDeleteAlbum() {
    String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      String albumName = selectedAlbum.split(" ")[1];
      // Check if the album is empty
      Album album = user.getAlbum(albumName);
      if (album != null && album.getPhotoCount() > 0) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Album is not empty! Cannot delete.");
        alert.showAndWait();
        return;
      }
      // Confirm deletion
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this album?");
      alert.setTitle("Delete Album");
      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          user.deleteAlbum(albumName);
          populateAlbums();
        }
      });
    } else {
      // No album selected
      Alert alert = new Alert(Alert.AlertType.WARNING, "No album selected!");
      alert.setTitle("Delete Album");
      alert.setHeaderText("No album selected.");
      alert.setContentText("Please select an album to delete.");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleRenameAlbum() {
    String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      String oldName = selectedAlbum.split(" ")[1];
      TextInputDialog dialog = new TextInputDialog(oldName);
      dialog.setTitle("Rename Album");
      dialog.setHeaderText("Enter new album name:");
      dialog.setContentText("Name:");
      dialog.showAndWait().ifPresent(newName -> {
        user.renameAlbum(oldName, newName);
        populateAlbums();
      });
    }
  }

  @FXML
  public void handleOpenAlbum() {
    String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      String albumName = selectedAlbum.split(" ")[1];
      Album album = user.getAlbums().get(albumName);
      if (album != null) {
        openAlbumView(album);
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No album selected!");
      alert.showAndWait();
    }
  }

  private void openAlbumView(Album album) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album-view.fxml"));
      Scene scene = new Scene(loader.load());
      AlbumController controller = loader.getController();
      controller.init(app, user, album);
      Stage stage = (Stage) albumList.getScene().getWindow();
      stage.setScene(scene);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}