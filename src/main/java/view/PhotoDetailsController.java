package view;

import app.model.Photo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 * Controller for displaying the details of a photo.
 * This class handles the logic for showing the photo's image, path, caption, date taken, and tags.
 * @author Reuben Thomas, Ryan Zaken
 */
public class PhotoDetailsController {

    /**
     * ImageView for displaying the photo.
     */
    @FXML
    private ImageView photoImageView;

    /**
     * Label for displaying the file path of the photo.
     */
    @FXML
    private Label pathLabel;

    /**
     * Label for displaying the caption of the photo.
     */
    @FXML
    private Label captionLabel;

    /**
     * Label for displaying the date the photo was taken.
     */
    @FXML
    private Label dateLabel;

    /**
     * VBox container for displaying the tags associated with the photo.
     */
    @FXML
    private VBox tagsContainer;

    /**
     * Initializes the controller with the details of the given photo.
     * Sets the photo's image, path, caption, date taken, and tags in the UI.
     *
     * @param photo The {@link Photo} object containing the details to display.
     */
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