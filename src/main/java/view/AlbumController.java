package view;

import app.Photos;
import app.model.Album;
import app.model.Photo;
import app.model.Tag;
import app.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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

  //  TODO: move to a new fxml
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
      Dialog<ButtonType> dialog = new Dialog<>();
      dialog.setTitle("Add Tag");
      dialog.setHeaderText("Add a tag to the photo");

      // Create the form content
      GridPane grid = new GridPane();
      grid.setHgap(10);
      grid.setVgap(10);
      grid.setPadding(new Insets(20, 150, 10, 10));

      ComboBox<String> tagTypeCombo = new ComboBox<>();
      tagTypeCombo.getItems().addAll(user.getTagTypes().keySet());
      TextField valueField = new TextField();
      Button createTagButton = new Button("Create New Tag Type");

      grid.add(new Label("Tag Type:"), 0, 0);
      grid.add(tagTypeCombo, 1, 0);
      grid.add(createTagButton, 2, 0);
      grid.add(new Label("Value:"), 0, 1);
      grid.add(valueField, 1, 1);

      createTagButton.setOnAction(e -> {
        Dialog<ButtonType> newTagDialog = new Dialog<>();
        newTagDialog.setTitle("Create New Tag Type");
        newTagDialog.setHeaderText("Create a new tag type");

        GridPane newTagGrid = new GridPane();
        newTagGrid.setHgap(10);
        newTagGrid.setVgap(10);
        newTagGrid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        CheckBox multiValueCheckBox = new CheckBox("Allow multiple values");

        newTagGrid.add(new Label("Tag Name:"), 0, 0);
        newTagGrid.add(nameField, 1, 0);
        newTagGrid.add(multiValueCheckBox, 1, 1);

        newTagDialog.getDialogPane().setContent(newTagGrid);
        newTagDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> newTagResult = newTagDialog.showAndWait();
        if (newTagResult.isPresent() && newTagResult.get() == ButtonType.OK) {
          String newTagName = nameField.getText().trim();
          if (!newTagName.isEmpty()) {
            //TODO: does this check if the tag type already exists?
            if (user.addTagType(newTagName, multiValueCheckBox.isSelected())) {
              tagTypeCombo.getItems().clear();
              tagTypeCombo.getItems().addAll(user.getTagTypes().keySet());
              tagTypeCombo.setValue(newTagName);
            } else {
              Alert alert = new Alert(Alert.AlertType.ERROR, "Tag type already exists!");
              alert.showAndWait();
            }
          } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Tag type name cannot be empty!");
            alert.showAndWait();
          }
        }
      });

      dialog.getDialogPane().setContent(grid);
      dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

      // Enable/Disable OK button depending on whether fields are filled
      Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
      okButton.setDisable(true);

      tagTypeCombo.valueProperty().addListener((obs, oldVal, newVal) ->
              okButton.setDisable(newVal == null || valueField.getText().trim().isEmpty()));
      valueField.textProperty().addListener((obs, oldVal, newVal) ->
              okButton.setDisable(tagTypeCombo.getValue() == null || newVal.trim().isEmpty()));

      Optional<ButtonType> result = dialog.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK) {
        String tagType = tagTypeCombo.getValue();
        String tagValue = valueField.getText().trim();

        // Check if tag type allows multiple values
        //  user.getTagTypes().get(tagType) returns allowsMultipleValues
        if (!user.getTagTypes().get(tagType) && selectedPhoto.hasTagType(tagType)) {
          Alert alert = new Alert(Alert.AlertType.ERROR,
                  "This tag type does not allow multiple values and the photo already has this tag type.");
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