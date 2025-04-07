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
  @FXML private ListView<String> photoList;
  @FXML private Label statusLabel;
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
    for (Photo photo : album.getPhotos()) {
      photoList.getItems().add(photo.getPath());
    }
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
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      album.removePhoto(album.getPhoto(selectedPhoto));
      populatePhotos();
      statusLabel.setText("Photo removed: " + selectedPhoto);
    }else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCaptionPhoto() {
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Caption Photo");
      dialog.setHeaderText("Enter caption:");
      dialog.setContentText("Caption:");
      dialog.showAndWait().ifPresent(caption -> {
        Photo photo = new Photo(selectedPhoto);
        photo.setCaption(caption);
        populatePhotos();
        statusLabel.setText("Caption added: " + caption);
      });
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleDisplayPhoto() {
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      // Display photo in a separate area with its details
      // Display photo in a separate area with its details
      String photoPath = selectedPhoto;
      Image image = new Image(new File(photoPath).toURI().toString());
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
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleAddTag() {
    // Add tag to photo
    // Placeholder for tag adding logic
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add Tag - Coming Soon!");
      alert.showAndWait();
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleRemoveTag() {
    // Remove tag from photo
    // Placeholder for tag adding logic
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "remove Tag - Coming Soon!");
      alert.showAndWait();
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleCopyPhoto() {
    // Copy photo to another album
    // Placeholder for tag adding logic
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Copy Photo - Coming Soon!");
      alert.showAndWait();
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleMovePhoto() {
    // Move photo to another album
    // Placeholder for tag adding logic
    String selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      Alert alert = new Alert(Alert.AlertType.INFORMATION, "Move Photo - Coming Soon!");
      alert.showAndWait();
    }else{
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  @FXML
  public void handleSlideshow() {
    // Implement manual slideshow
    // Placeholder for tag adding logic
    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Slideshow - Coming Soon!");
    alert.showAndWait();
  }

  @FXML
  public void handleSearchPhotos() {
    // Implement search functionality
    // Placeholder for tag adding logic
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