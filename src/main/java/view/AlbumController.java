package view;

import app.Photos;
import app.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AlbumController {
  @FXML
  private ListView<Photo> photoList;
  @FXML
  private Label statusLabel;
  @FXML
  private Label albumNameLabel;
  private Album album;
  private User user;
  private Photos app;

  public void init(Photos app, User user, Album album) {
    this.app = app;
    this.user = user;
    this.album = album;
    albumNameLabel.setText("Photos in album '" + album.getName() + "'");

    populatePhotos();
  }

  private void populatePhotos() {
    photoList.getItems().clear();
    photoList.getItems().addAll(album.getPhotos());
    // Set custom cell factory to display photo thumbnails and captions

    //TODO: figure out what this does. what is a cell factory? what is a list cell?
    photoList.setCellFactory(listView -> new ListCell<>() {
      private final ImageView imageView = new ImageView();

      @Override
      protected void updateItem(Photo photo, boolean empty) {
        super.updateItem(photo, empty);
        if (empty || photo == null) {
          setText(null);
          setGraphic(null);
        } else {
          // Set thumbnail
          File file = new File(photo.getPath());
          Image image = new Image(file.toURI().toString(), 50, 50, true, true);
          imageView.setImage(image);
          // Set text
          setText(photo.toString());
          setGraphic(imageView);
        }
      }
    });
  }

  @FXML
  public void handleAddPhoto() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Add Photo");
    fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
    );
    File selectedFile = fileChooser.showOpenDialog(null);
    if (selectedFile != null) {
      String photoPath = selectedFile.getAbsolutePath();
      Photo photoToAdd = null;

      // Check if the photo exists in another album
      for (Album other_album : user.getAlbums()) {
        if (other_album.getPhoto(photoPath) != null) {
          System.out.println("Photo already exists in another album, adding to this album.");
          photoToAdd = other_album.getPhoto(photoPath);
          break;
        }
      }

      // If the photo does not exist in any album, create a new photo
      if (photoToAdd == null) {
        photoToAdd = new Photo(selectedFile.getAbsolutePath());
      }

      try {
        // Add the photo to the album and update UI
        album.addPhoto(photoToAdd);
        populatePhotos();
        statusLabel.setText("Photo added: " + selectedFile.getName());
      } catch (IllegalArgumentException | IllegalStateException e) {
        Alert alert = new Alert(Alert.AlertType.WARNING, e.getMessage());
        alert.showAndWait();
      }
    }
  }

  @FXML
  public void handleRemovePhoto() {
    //TODO: are you sure you want to remove this photo from the album?
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      // are you sure you want to remove this photo from the album?
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this photo from the album?");
      alert.setTitle("Remove Photo");
      alert.setHeaderText("Remove Photo");
      alert.setContentText("Are you sure you want to remove this photo from the album?");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            album.removePhoto(selectedPhoto);
        } catch (IllegalArgumentException e) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert2.showAndWait();
        }
        populatePhotos();
        statusLabel.setText("Photo removed: " + selectedPhoto.getName());
      }

    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCaptionPhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      TextInputDialog dialog = new TextInputDialog(selectedPhoto.getCaption());
      dialog.setTitle("Caption Photo");
      dialog.setHeaderText("Enter caption:");
      dialog.setContentText("Caption:");
      dialog.showAndWait().ifPresent(caption -> {
        selectedPhoto.setCaption(caption);
        populatePhotos();
        statusLabel.setText("Caption added: " + caption);
      });
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleDisplayPhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/photo-details-dialog.fxml"));
        ScrollPane root = loader.load();

        PhotoDetailsController controller = loader.getController();
        controller.init(selectedPhoto);

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Photo Details - " + new File(selectedPhoto.getPath()).getName());
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }


  @FXML
  public void handleAddTag() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add-tag-dialog.fxml"));
        GridPane root = loader.load();

        AddTagDialogController controller = loader.getController();
        controller.init(user.getTagTypes());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Add a tag to the photo");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && controller.isConfirmed()) {
          TagType tagType = controller.getSelectedTagType();
          String tagValue = controller.getTagValue();
          System.out.println(user.getTagTypes());
          System.out.println(tagType);
          if (!tagType.isMultiValue() && selectedPhoto.hasTagType(tagType)) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "'" + tagType + "' tag type does not allow multiple values. Please remove the existing tag from '" + selectedPhoto.getName() + "' before adding a new one.");
            alert.showAndWait();
            return;
          }

          try {
            Tag tag = new Tag(tagType, tagValue);
            selectedPhoto.addTag(tag);
            statusLabel.setText("Tag added: " + tag);
          } catch (IllegalArgumentException | IllegalStateException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleRemoveTag() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/remove-tag-dialog.fxml"));
        GridPane root = loader.load();

        RemoveTagDialogController controller = loader.getController();
        controller.init(selectedPhoto);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Remove Tag");
        dialog.setHeaderText("Select a tag to remove");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Tag tagToRemove = controller.getSelectedTag();
          if (tagToRemove != null) {
            selectedPhoto.removeTag(tagToRemove);
            statusLabel.setText("Tag removed: " + tagToRemove);
          } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No tag selected!");
            alert.showAndWait();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCopyPhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select-album-dialog.fxml"));
        GridPane root = loader.load();

        SelectAlbumDialogController controller = loader.getController();
        controller.init(user);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select an album to copy the photo to:");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Album targetAlbum = controller.getSelectedAlbum();
          if (targetAlbum != null) {
            try {
              targetAlbum.addPhoto(selectedPhoto);
              statusLabel.setText("Photo copied to album: " + targetAlbum.getName());
            } catch (IllegalArgumentException | IllegalStateException e) {
              Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
              alert.showAndWait();
            }
          } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No album selected!");
            alert.showAndWait();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleMovePhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Move Photo - Coming Soon!");
      alert.showAndWait();
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleSlideshow() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Slideshow - Coming Soon!");
    alert.showAndWait();
  }

  @FXML
  public void handleSearchPhotos() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Search Photos - Coming Soon!");
    alert.showAndWait();
  }

  @FXML
  public void handleBack() {
    try {
      app.switchToUserHomeView((Stage) photoList.getScene().getWindow(), user.getUsername());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}