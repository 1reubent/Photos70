package view;

import app.model.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;

public class SlideshowController {
  @FXML
  private ImageView imageView;
  @FXML
  private Label captionLabel;

  private List<Photo> photos;
  private int currentIndex = 0;

  public void init(List<Photo> photos) {
    this.photos = photos;
    if (!photos.isEmpty()) {
      displayPhoto(0);
    }
  }

  @FXML
  private void handleNext() {
    if (photos != null && !photos.isEmpty()) {
      currentIndex = (currentIndex + 1) % photos.size();
      displayPhoto(currentIndex);
    }
  }

  @FXML
  private void handlePrevious() {
    if (photos != null && !photos.isEmpty()) {
      currentIndex = (currentIndex - 1 + photos.size()) % photos.size();
      displayPhoto(currentIndex);
    }
  }

  private void displayPhoto(int index) {
    Photo photo = photos.get(index);
    File file = new File(photo.getPath());
    Image image = new Image(file.toURI().toString());
    imageView.setImage(image);
    captionLabel.setText(photo.getCaption() != null ? photo.getCaption() : "No Caption");
  }
}