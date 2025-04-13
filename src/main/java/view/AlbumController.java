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
/**
 * Controller for managing the album view.
 * This class handles the logic for displaying, adding, removing, and managing photos within an album.
 * @author Reuben Thomas, Ryan Zaken
 */
public class AlbumController {
  /**
   * ListView for displaying the {@link Photo}s in the album.
   */
  @FXML
  private ListView<Photo> photoList;

  /**
   * Label for displaying the status of operations.
   */
  @FXML
  private Label statusLabel;

  /**
   * Label for displaying the name of the album.
   */
  @FXML
  private Label albumNameLabel;

  /**
   * The {@link Album} being managed by this controller.
   */
  private Album album;

  /**
   * The {@link User} who owns the album.
   */
  private User user;

  /**
   * The main {@link Photos} application instance.
   */
  private Photos app;

  /**
   * Initializes the controller with the application, user, and album.
   *
   * @param app   The main application instance.
   * @param user  The user who owns the album.
   * @param album The album being managed.
   */
  public void init(Photos app, User user, Album album) {
    this.app = app;
    this.user = user;
    this.album = album;
    albumNameLabel.setText("Photos in album '" + album.getName() + "'");
    populatePhotos();
  }

  /**
   * Populates the ListView with photos from the album.
   * Sets a custom cell factory to display photo thumbnails and captions.
   */
  private void populatePhotos() {
    photoList.getItems().clear();
    photoList.getItems().addAll(album.getPhotos());

    photoList.setCellFactory(listView -> new ListCell<>() {
      private final ImageView imageView = new ImageView();

      @Override
      protected void updateItem(Photo photo, boolean empty) {
        super.updateItem(photo, empty);
        if (empty || photo == null) {
          setText(null);
          setGraphic(null);
        } else {
          File file = new File(photo.getPath());
          Image image = new Image(file.toURI().toString(), 50, 50, true, true);
          imageView.setImage(image);
          setText(photo.toString());
          setGraphic(imageView);
        }
      }
    });
  }

  /**
   * Displays a warning message in a dialog.
   *
   * @param message The warning message to display.
   */
  private void showWarning(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING, message);
    alert.setTitle("Warning");
    alert.setHeaderText("Warning");
    alert.showAndWait();
  }

  /**
   * Displays an error message in a dialog.
   *
   * @param message The error message to display.
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.showAndWait();
  }

  /**
   * Displays a confirmation dialog with a message.
   *
   * @param message The confirmation message to display.
   * @return An {@link Optional} containing the user's response.
   */
  private Optional<ButtonType> showConfirmation(String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Confirmation");
    return alert.showAndWait();
  }

  /**
   * Handles the action of adding a photo to the album.
   * Opens a file chooser to select an image file and adds it to the album.
   */
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
        showWarning(e.getMessage());
      }
    }
  }

  /**
   * Handles the action of removing a photo from the album.
   * Prompts the user for confirmation before removing the selected photo.
   */
  @FXML
  public void handleRemovePhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      // are you sure you want to remove this photo from the album?
      Optional<ButtonType> result = showConfirmation("Are you sure you want to remove this photo from the album?");
      if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
          album.removePhoto(selectedPhoto);
        } catch (IllegalArgumentException e) {
          showError(e.getMessage());
        }
        populatePhotos();
        statusLabel.setText("Photo removed: " + selectedPhoto.getName());
      }

    } else {
      // show warning
      showWarning("No photo selected!");
    }
  }

  /**
   * Handles the action of adding or updating a caption for a selected photo.
   * Opens a dialog to input the caption and updates the photo's caption.
   */
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
      // show warning
      showWarning("No photo selected!");
    }
  }

  /**
   * Handles the action of displaying the details of a selected photo.
   * Opens a new window to show the photo's details.
   */
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
        // show error
        showError("Error loading photo details: " + e.getMessage());
      }
    } else {
      showWarning("No photo selected!");
    }
  }



  /**
   * Handles the action of adding a tag to a selected photo.
   * Opens a dialog to select a tag type and value, and adds the tag to the photo.
   */
  @FXML
  public void handleAddTag() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/add-tag-dialog.fxml"));
        GridPane root = loader.load();

        AddTagDialogController controller = loader.getController();
        controller.init(user.getAllTagTypes());

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Add a tag to the photo");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        //check if the user clicked OK and if they chose a tag type and entered a value
        if (result.isPresent() && result.get() == ButtonType.OK && controller.isConfirmed()) {
          TagType tagType = controller.getSelectedTagType();
          String tagValue = controller.getTagValue();
          System.out.println(user.getAllTagTypes());
          System.out.println(tagType);
          if (!tagType.isMultiValue() && selectedPhoto.hasTagType(tagType)) {
            // show error
            showError("'" + tagType + "' tag type does not allow multiple values. Please remove the existing tag from '"
                    + selectedPhoto.getName() + "' before adding a new one.");
            return;
          }

          try {
            Tag tag = new Tag(tagType, tagValue);
            selectedPhoto.addTag(tag);
            statusLabel.setText("Tag added: " + tag);
          } catch (IllegalArgumentException | IllegalStateException e) {
            // show error
            showError(e.getMessage());
          }
        }
      } catch (IOException e) {
        //Handle IOException
        showError("Error loading add tag dialog: " + e.getMessage());
      }
    } else {
      Alert alert = new Alert(Alert.AlertType.WARNING, "No photo selected!");
      alert.showAndWait();
    }
  }

  /**
   * Handles the action of removing a tag from a selected photo.
   * Opens a dialog to select a tag to remove and removes it from the photo.
   */
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
            // show warning
            showWarning("No tag selected!");
          }
        }
      } catch (IOException e) {
        // Handle IOException
        showError("Error loading remove tag dialog: " + e.getMessage());
      }
    } else {
      // show warning
      showWarning("No photo selected!");
    }
  }

  /**
   * Handles the action of copying a photo to another album.
   * Opens a dialog to select a target album and copies the selected photo to it.
   */
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
              //catch add photo exception.
              showError(e.getMessage());
            }
          } else {
            // show warning
            showWarning("No album selected!");
          }
        }
      } catch (IOException e) {
        // Handle IOException
        showError("Error loading select album dialog: " + e.getMessage());
      }
    } else {
      // show warning
      showWarning("No photo selected!");
    }
  }

  /**
   * Handles the action of moving a photo to another album.
   * Opens a dialog to select a target album and moves the selected photo to it.
   */
  @FXML
  public void handleMovePhoto() {
    Photo selectedPhoto = photoList.getSelectionModel().getSelectedItem();
    if (selectedPhoto != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select-album-dialog.fxml"));
        GridPane root = loader.load();

        SelectAlbumDialogController controller = loader.getController();
        controller.init(user);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Move Photo");
        dialog.setHeaderText("Select an album to move the photo to:");
        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
          Album targetAlbum = controller.getSelectedAlbum();
          if (targetAlbum != null) {
            try {
              album.removePhoto(selectedPhoto);
              try {
                targetAlbum.addPhoto(selectedPhoto);
                populatePhotos();
                statusLabel.setText("Photo moved to album: " + targetAlbum.getName());
              } catch (IllegalArgumentException | IllegalStateException e) {
                //catch add photo exception.
                album.addPhoto(selectedPhoto); // Re-add the photo to the original album
                throw e; // Re-throw the exception to handle it in the outer catch block
              }
            } catch (IllegalArgumentException | IllegalStateException e) {
              //catch remove photo OR re-thrown add photo exception
              showError(e.getMessage());
            }
          } else {
            showWarning("No album selected!");
          }
        }
      } catch (IOException e) {
        // Handle IOException
        showError("Error loading select album dialog: " + e.getMessage());
      }
    } else {
      showWarning("No photo selected!");
    }
  }

  /**
   * Handles the action of starting a slideshow of the photos in the album.
   * Opens a new window to display the slideshow.
   */
  @FXML
  public void handleSlideshow() {
    if (album.getPhotos().isEmpty()) {
      // show warning
      showWarning("No photos available in the album for slideshow!");
      return;
    }

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/slideshow-view.fxml"));
      VBox root = loader.load();

      SlideshowController controller = loader.getController();
      controller.init(album.getPhotos());

      Stage slideshowStage = new Stage();
      Scene scene = new Scene(root, 600, 400);
      slideshowStage.setScene(scene);
      slideshowStage.setTitle("Slideshow");
      slideshowStage.show();
    } catch (IOException e) {
      // handle IOException
      showError("Error loading slideshow view: " + e.getMessage());
    }
  }

  /**
   * Handles the action of navigating back to the user home view.
   * Switches the scene to the user home view.
   */
  @FXML
  public void handleBack() {
    try {
      app.switchToUserHomeView((Stage) photoList.getScene().getWindow(), user.getUsername());
    } catch (IOException e) {
      // handle IOException
      showError("Error loading user home view: " + e.getMessage());
    }
  }
}