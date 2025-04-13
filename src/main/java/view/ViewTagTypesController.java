package view;

import app.model.TagType;
import app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * Controller for managing the view and operations related to tag types.
 * Allows the user to add, remove, and view tag types, as well as configure
 * whether a tag type supports multiple values.
 * @author Reuben Thomas, Ryan Zaken
 */
public class ViewTagTypesController {

    /** ListView displaying the user's {@link TagType}s. */
    @FXML
    private ListView<TagType> tagTypeList;

    /** TextField for entering the name of a new tag type. */
    @FXML
    private TextField newTagTypeField;

    /** CheckBox to indicate if the new tag type supports multiple values. */
    @FXML
    private CheckBox multiValueCheckBox;

    /** Button to add a new tag type. */
    @FXML
    private Button addTagTypeButton;

    /** Button to remove the selected tag type. */
    @FXML
    private Button removeTagTypeButton;

    /** The currently logged-in {@link User}. */
    private User user;

    /**
     * Initializes the controller with the given user.
     *
     * @param user The {@link User} object representing the currently logged-in user.
     */
    public void init(User user) {
        this.user = user;
        tagTypeList.getItems().addAll(user.getAllTagTypes());
        configureButtons();
    }

    /**
     * Configures the actions for the add and remove {@link TagType} buttons.
     */
    private void configureButtons() {
        addTagTypeButton.setOnAction(e -> {
            String newTagTypeName = newTagTypeField.getText().trim();
            if (!newTagTypeName.isEmpty()) {
                if (user.addTagType(newTagTypeName, multiValueCheckBox.isSelected())) {
                    tagTypeList.getItems().clear();
                    tagTypeList.getItems().addAll(user.getAllTagTypes());
                    newTagTypeField.clear();
                    multiValueCheckBox.setSelected(false);
                } else {
                    showError("Tag type already exists!");
                }
            } else {
                showError("Tag type name cannot be empty!");
            }
        });

        removeTagTypeButton.setOnAction(e -> {
            TagType selectedTagType = tagTypeList.getSelectionModel().getSelectedItem();
            if (selectedTagType != null) {
                if (selectedTagType.getName().equalsIgnoreCase("location") || selectedTagType.getName().equalsIgnoreCase("people")) {
                    showError("Cannot remove default tag types: location and people.");
                    return;
                }
                Optional<ButtonType> result = showConfirmation("Are you sure you want to remove this tag type?\n\nThis will remove all tags of this type from all your photos.");
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    if (user.removeTagType(selectedTagType.getName())) {
                        tagTypeList.getItems().remove(selectedTagType);
                        removeTagsFromPhotos(selectedTagType);
                        showInfo("Tag type and associated tags removed successfully.");
                    } else {
                        showError("Failed to remove tag type.");
                    }
                }
            } else {
                showError("No tag type selected!");
            }
        });
    }

    /**
     * Removes all tags of the specified tag type from all photos in the user's albums.
     *
     * @param tagType The {@link TagType} to remove from photos.
     */
    private void removeTagsFromPhotos(TagType tagType) {
        user.getAlbums().forEach(album -> {
            album.getPhotos().forEach(photo -> {
                photo.getTags().removeIf(tag -> tag.getName().equalsIgnoreCase(tagType.getName()));
            });
        });
    }

    /**
     * Displays an informational message in an alert dialog.
     *
     * @param message The informational message to display.
     */
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Information");
        alert.setHeaderText("Information");
        alert.showAndWait();
    }

    /**
     * Displays an error message in an alert dialog.
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
     * Displays a confirmation dialog with the given message.
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
}