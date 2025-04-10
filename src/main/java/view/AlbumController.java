package view;

import app.Photos;
import app.model.Album;
import app.model.Photo;
import app.model.Tag;
import app.model.User;
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
    albumNameLabel.setText("Photos in album: " + album.getName());

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

          // Set caption and filename
          String filename = file.getName();
          String caption = photo.getCaption();
          setText(String.format("%s (Caption: %s)", filename, !caption.isEmpty() ? caption : "No caption"));
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
      //check if the file is already in the album
      if (album.getPhoto(photoPath) != null) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Photo already exists in the album!");
        alert.showAndWait();
        return;
      }
      //if photo already exists in an album, add that same photo to this album
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

      // Add the photo to the album and update UI
      album.addPhoto(photoToAdd);
      populatePhotos();
      statusLabel.setText("Photo added: " + selectedFile.getName());
    }
  }

  @FXML
  public void handleRemovePhoto() {
    //TODO: are you sure you want to remove this photo from the album?
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      album.removePhoto(selectedPhoto);
      populatePhotos();
      statusLabel.setText("Photo removed: " + selectedPhoto.getName());
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

  //TODO: move to a new fxml
  @FXML
  public void handleAddTag() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add-tag-dialog.fxml"));
        GridPane root = loader.load();

        AddTagDialogController controller = loader.getController();
        controller.init(user);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Add a tag to the photo");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK && controller.isConfirmed()) {
          String tagType = controller.getSelectedTagType();
          String tagValue = controller.getTagValue();
          System.out.println(user.getTagTypes());
          System.out.println(tagType);
          if (!user.isMultiValueTagType(tagType) && selectedPhoto.hasTagType(tagType)) {
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
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Remove Tag - Coming Soon!");
      alert.showAndWait();
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCopyPhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Copy Photo - Coming Soon!");
      alert.showAndWait();
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