package view;

import app.model.Album;
import app.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Controller for the Select Album Dialog.
 * This class handles the logic for displaying a list of {@link Album}
 * and allowing the user to select one.
 * @author Reuben Thomas, Ryan Zaken
 */
public class SelectAlbumDialogController {

    /**
     * ListView for displaying the list of {@link Album}.
     */
    @FXML
    private ListView<Album> albumListView;

    /**
     * Initializes the dialog with the user's albums.
     *
     * @param user The {@link User} whose albums are to be displayed.
     */
    public void init(User user) {
        ObservableList<Album> albums = FXCollections.observableArrayList(user.getAlbums());
        albumListView.setItems(albums);
    }

    /**
     * Gets the album selected by the user.
     *
     * @return The selected {@link Album}, or {@code null} if no album is selected.
     */
    public Album getSelectedAlbum() {
        return albumListView.getSelectionModel().getSelectedItem();
    }
}