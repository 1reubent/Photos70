package view;

import app.model.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

public class PhotoDetailsController {

  @FXML
  private ImageView photoImageView;
  @FXML
  private Label pathLabel;
  @FXML
  private Label captionLabel;
  @FXML
  private Label dateLabel;
  @FXML
  private VBox tagsContainer;

  public void init(Photo photo) {
    // Set the photo image
    File file = new File(photo.getPath());
    Image image = new Image(file.toURI().toString());
    photoImageView.setImage(image);
    photoImageView.setFitWidth(600);
    photoImageView.setPreserveRatio(true);

    // Set photo details
    pathLabel.setText("Path: " + photo.getPath());
    captionLabel.setText("Caption: " + photo.getCaption());
    dateLabel.setText("Date Taken: " + photo.getDateTaken());

    // Populate tags
    tagsContainer.getChildren().clear();
    if (photo.getTags().isEmpty()) {
      tagsContainer.getChildren().add(new Label("No tags"));
    } else {
      photo.getTags().forEach(tag -> {
        Label tagLabel = new Label("• " + tag.toString());
        tagsContainer.getChildren().add(tagLabel);
      });
    }
  }
}