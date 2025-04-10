package view;

import app.Photos;
import app.model.Album;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import app.model.User;

import java.io.IOException;


public class UserHomeController {
  @FXML
  private ListView<Album> albumList;
  @FXML
  private Label statusLabel;
  private User user;
  private Photos app;

  public void init(Photos app, User user) {
    this.user = user;
    this.app = app;
    System.out.println("UserHomeController initialized with user: " + user.getUsername());
    System.out.println("Albums: " + user.getAlbumNames());

    populateAlbums();
    // Additional initialization code here
    System.out.println("User logged in: " + user.getUsername());
  }

  public void populateAlbums() {
    System.out.println(user.getUsername());
    System.out.println(user.getAlbumNames());

    albumList.getItems().clear();
    albumList.getItems().addAll(user.getAlbums());
    System.out.println("Albums populated: " + albumList.getItems());

    // TODO: figure out what this does. what is a cell factory? what is a list cell?
    albumList.setCellFactory(listView -> new ListCell<>() {
      @Override
      protected void updateItem(Album album, boolean empty) {
        super.updateItem(album, empty);
        if (empty || album == null) {
          setText(null);
        } else {
          setText(album.toString());
        }
      }
    });
  }

  @FXML
  public void handleLogout() {
    // data changes are saved to disk when the app is closed. until then, they are held in memory.
    app.clearUserData();
    app.switchToLoginView((Stage) albumList.getScene().getWindow());
  }

  @FXML
  public void handleViewTagTypes() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view-tag-types-dialog.fxml"));
      DialogPane dialogPane = loader.load();

      ViewTagTypesController controller = loader.getController();
      controller.init(user);

      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.setDialogPane(dialogPane);
      dialog.setTitle("View My Tag Types");
      //add close button
      dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
      dialog.showAndWait();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
      if (user.addAlbum(name) == null) {
        showError("Album name already exists!");
        return;
      }
      populateAlbums();
    });
  }

  @FXML
  public void handleDeleteAlbum() {
    Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      if (selectedAlbum.getPhotoCount() > 0) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Album is not empty! Cannot delete.");
        alert.showAndWait();
        return;
      }
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this album?");
      alert.setTitle("Delete Album");
      alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
          user.deleteAlbum(selectedAlbum.getName());
          populateAlbums();
        }
      });
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No album selected!");
      alert.setTitle("Delete Album");
      alert.setHeaderText("No album selected.");
      alert.setContentText("Please select an album to delete.");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleRenameAlbum() {
    Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      TextInputDialog dialog = new TextInputDialog(selectedAlbum.getName());
      dialog.setTitle("Rename Album");
      dialog.setHeaderText("Enter new album name:");
      dialog.setContentText("Name:");
      dialog.showAndWait().ifPresent(newName -> {
        user.renameAlbum(selectedAlbum.getName(), newName);
        populateAlbums();
      });
    }
  }

  @FXML
  public void handleOpenAlbum() {
    Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
    if (selectedAlbum != null) {
      openAlbumView(selectedAlbum);
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No album selected!");
      alert.showAndWait();
    }
  }

  private void openAlbumView(Album album) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album-view.fxml"));
      Scene scene = new Scene(loader.load(), 640, 650);
      AlbumController controller = loader.getController();
      controller.init(app, user, album);
      Stage stage = (Stage) albumList.getScene().getWindow();
      stage.setScene(scene);
      stage.setTitle("Viewing Album: " + album.getName()); // Set the title to the album's nam
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}