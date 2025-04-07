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

  private void populateAlbums() {
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
    // Save user data to disk


    app.switchToLoginView((Stage) albumList.getScene().getWindow());
  }

//  @FXML
////  TODO: importing should be done in the album view? otherwise we need to put the photo in an album
//  public void handleImport() {
//    FileChooser fileChooser = new FileChooser();
//    fileChooser.setTitle("Import Photo");
//    fileChooser.getExtensionFilters().add(
//            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
//    );
//    File selectedFile = fileChooser.showOpenDialog(null);
//    if (selectedFile != null) {
//      photoList.getItems().add(selectedFile.getAbsolutePath());
//      statusLabel.setText("Imported: " + selectedFile.getName());
//    }
//  }
//
//  @FXML
//  public void handleAddTag() {
//    // Placeholder for tag adding logic
//    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add Tag - Coming Soon!");
//    alert.showAndWait();
//  }
//
//  @FXML
//  public void handleRemoveTag() {
//    // Placeholder for tag removing logic
//    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Remove Tag - Coming Soon!");
//    alert.showAndWait();
//  }

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
      user.addAlbum(name);
      populateAlbums();
    });
  }

  @FXML
  public void handleDeleteAlbum() {
    String selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      String albumName = selectedAlbum.split(" ")[1];
      user.deleteAlbum(albumName);
      populateAlbums();
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