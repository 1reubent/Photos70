package view;

import app.model.Photo;
import app.model.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Controller for the Remove Tag Dialog.
 * This class handles the logic for removing a tag from a photo,
 * including displaying the list of tags and selecting a tag to remove.
 * @author Reuben Thomas, Ryan Zaken
 */
public class RemoveTagDialogController {

    /**
     * ListView for displaying the tags associated with a photo.
     */
    @FXML
    private ListView<Tag> tagListView;

    /**
     * The {@link Tag} selected by the user for removal.
     */
    private Tag selectedTag;

    /**
     * Initializes the dialog with the tags of the given photo.
     *
     * @param photo The {@link Photo} whose tags are to be displayed.
     */
    public void init(Photo photo) {
        tagListView.getItems().addAll(photo.getTags());
        tagListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectedTag = newVal);
    }

    /**
     * Gets the tag selected by the user for removal.
     *
     * @return The selected {@link Tag}, or {@code null} if no tag is selected.
     */
    public Tag getSelectedTag() {
        return selectedTag;
    }
}