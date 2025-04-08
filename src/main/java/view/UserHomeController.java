package view;

import app.Photos;
import app.model.Album;
import app.model.Tag;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;

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
          setText(String.format("Name: %s | %d photos | Date Range: %s",
                  album.getName(),
                  album.getPhotoCount(),
                  album.getDateRange()));
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
  public void handleEditTagTypes() {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Edit My Tag Types");
    dialog.setHeaderText("Manage your tag types");

    // Create the form content
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    ListView<String> tagTypeList = new ListView<>();
    tagTypeList.getItems().addAll(user.getTagTypes().keySet());
    tagTypeList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    TextField newTagTypeField = new TextField();
    CheckBox multiValueCheckBox = new CheckBox("Allow multiple values");
    Button addTagTypeButton = new Button("Add Tag Type");
    Button removeTagTypeButton = new Button("Remove Selected Tag Type");

    grid.add(new Label("Existing Tag Types:"), 0, 0);
    grid.add(tagTypeList, 0, 1, 2, 1);
    grid.add(new Label("New Tag Type:"), 0, 2);
    grid.add(newTagTypeField, 1, 2);
    grid.add(multiValueCheckBox, 1, 3);
    grid.add(addTagTypeButton, 0, 4);
    grid.add(removeTagTypeButton, 1, 4);

    // Add functionality to buttons
    addTagTypeButton.setOnAction(e -> {
      String newTagType = newTagTypeField.getText().trim();
      if (!newTagType.isEmpty()) {
        if (user.addTagType(newTagType, multiValueCheckBox.isSelected())) {
          tagTypeList.getItems().clear();
          tagTypeList.getItems().addAll(user.getTagTypes().keySet());
          newTagTypeField.clear();
          multiValueCheckBox.setSelected(false);
        } else {
          Alert alert = new Alert(Alert.AlertType.ERROR, "Tag type already exists!");
          alert.showAndWait();
        }
      } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Tag type name cannot be empty!");
        alert.showAndWait();
      }
    });

    removeTagTypeButton.setOnAction(e -> {
      String selectedTagType = tagTypeList.getSelectionModel().getSelectedItem();
      if (selectedTagType != null) {
        //cannot remove location or people tags
        if (selectedTagType.equalsIgnoreCase("location") || selectedTagType.equalsIgnoreCase("people")) {
          Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot remove default tag types: location and people.");
          alert.showAndWait();
          return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Tag Type");
        confirmationAlert.setHeaderText("Are you sure you want to remove this tag type?");
        confirmationAlert.setContentText("This will remove all tags of this type from all your photos.");
        confirmationAlert.showAndWait().ifPresent(response -> {
          if (response == ButtonType.OK) {
            // Remove the tag type
            if(user.removeTagType(selectedTagType)) {
              tagTypeList.getItems().remove(selectedTagType);

              // Remove all tags of this type from all photos
              user.getAlbums().forEach(album -> {
                album.getPhotos().forEach(photo -> {
                  photo.getTags().removeIf(tag -> tag.getName().equalsIgnoreCase(selectedTagType));
                });
              });

              Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Tag type and associated tags removed successfully.");
              successAlert.showAndWait();
            }else{
              Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to remove tag type.");
              errorAlert.showAndWait();
            }
          }
        });
      } else {
        Alert alert = new Alert(Alert.AlertType.WARNING, "No tag type selected!");
        alert.showAndWait();
      }
    });

    dialog.getDialogPane().setContent(grid);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
    dialog.showAndWait();
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
      if (!user.addAlbum(name)) {
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