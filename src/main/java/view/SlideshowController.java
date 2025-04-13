package view;

import app.model.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.List;

/**
 * Controller for the Slideshow feature.
 * This class handles the logic for displaying a slideshow of photos,
 * including navigating between photos and displaying their captions.
 * @author Reuben Thomas, Ryan Zaken
 */
public class SlideshowController {

    /**
     * The ImageView for displaying the current photo.
     */
    @FXML
    private ImageView imageView;

    /**
     * The Label for displaying the caption of the current photo.
     */
    @FXML
    private Label captionLabel;

    /**
     * The list of {@link Photo}s to be displayed in the slideshow.
     */
    private List<Photo> photos;

    /**
     * The index of the currently displayed photo in the slideshow.
     */
    private int currentIndex = 0;

    /**
     * Initializes the slideshow with a list of photos.
     * If the list is not empty, the first photo is displayed.
     *
     * @param photos The list of {@link Photo} objects to display in the slideshow.
     */
    public void init(List<Photo> photos) {
        this.photos = photos;
        if (!photos.isEmpty()) {
            displayPhoto(0);
        }
    }

    /**
     * Handles the action for navigating to the next photo in the slideshow.
     * If the end of the list is reached, it wraps around to the first photo.
     */
    @FXML
    private void handleNext() {
        if (photos != null && !photos.isEmpty()) {
            currentIndex = (currentIndex + 1) % photos.size();
            displayPhoto(currentIndex);
        }
    }

    /**
     * Handles the action for navigating to the previous photo in the slideshow.
     * If the beginning of the list is reached, it wraps around to the last photo.
     */
    @FXML
    private void handlePrevious() {
        if (photos != null && !photos.isEmpty()) {
            currentIndex = (currentIndex - 1 + photos.size()) % photos.size();
            displayPhoto(currentIndex);
        }
    }

    /**
     * Displays the photo at the specified index in the slideshow.
     * Updates the ImageView with the photo and sets the caption.
     *
     * @param index The index of the photo to display.
     */
    private void displayPhoto(int index) {
        Photo photo = photos.get(index);
        File file = new File(photo.getPath());
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
        captionLabel.setText(photo.getCaption() != null ? photo.getCaption() : "No Caption");
    }
}