package view;

import app.Photos;
import app.model.Album;
import app.model.Photo;
import app.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;

public class AlbumController {
  @FXML
  private ListView<Photo> photoList;
  @FXML
  private Label statusLabel;
  private Album album;
  private User user;
  private Photos app;

  public void init(Photos app, User user, Album album) {
    this.user = user;
    this.album = album;
    this.app = app;
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
      album.addPhoto(new Photo(selectedFile.getAbsolutePath()));
      populatePhotos();
      statusLabel.setText("Photo added: " + selectedFile.getName());
    }
  }

  @FXML
  public void handleRemovePhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      album.removePhoto(selectedPhoto);
      populatePhotos();
      statusLabel.setText("Photo removed: " + selectedPhoto.getCaption());
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCaptionPhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      TextInputDialog dialog = new TextInputDialog();
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
      Image image = new Image(new File(selectedPhoto.getPath()).toURI().toString());
      ImageView imageView = new ImageView(image);
      imageView.setFitWidth(400);
      imageView.setPreserveRatio(true);

      Stage stage = new Stage();
      VBox vbox = new VBox(imageView);
      vbox.setPadding(new Insets(10));
      Scene scene = new Scene(vbox);
      stage.setScene(scene);
      stage.setTitle("Photo Viewer");
      stage.show();
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleAddTag() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add Tag - Coming Soon!");
      alert.showAndWait();
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