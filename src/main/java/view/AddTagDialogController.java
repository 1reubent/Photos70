package view;

import app.model.TagType;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller for the Add Tag Dialog.
 * This class handles the logic for adding a tag to a photo, including
 * selecting a tag type and entering a tag value.
 * @author Reuben Thomas, Ryan Zaken
 */
public class AddTagDialogController {

    /**
     * ComboBox for selecting the tag type.
     */
    @FXML
    private ComboBox<TagType> tagTypeCombo;

    /**
     * TextField for entering the tag value.
     */
    @FXML
    private TextField valueField;

    /**
     * Indicates whether the dialog was confirmed with valid input.
     */
    private boolean confirmed = false;

    /**
     * Initializes the dialog with the available tag types.
     *
     * @param tagTypes A list of available {@link TagType} objects.
     */
    public void init(List<TagType> tagTypes) {
        tagTypeCombo.getItems().addAll(tagTypes);
    }

    /**
     * Checks if the dialog was confirmed with valid input.
     *
     * @return {@code true} if the dialog was confirmed, {@code false} otherwise.
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Gets the selected tag type.
     *
     * @return The selected {@link TagType}.
     */
    public TagType getSelectedTagType() {
        return tagTypeCombo.getValue();
    }

    /**
     * Gets the entered tag value.
     *
     * @return The tag value as a trimmed string.
     */
    public String getTagValue() {
        return valueField.getText().trim();
    }

    /**
     * Initializes the controller and sets up validation listeners.
     */
    @FXML
    private void initialize() {
        tagTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate());
        valueField.textProperty().addListener((obs, oldVal, newVal) -> validate());
    }

    /**
     * Validates the input fields and updates the confirmation status.
     * The input is valid if a tag type is selected and the value field is not empty.
     */
    private void validate() {
        boolean valid = tagTypeCombo.getValue() != null && !valueField.getText().trim().isEmpty();
        confirmed = valid;
    }
}