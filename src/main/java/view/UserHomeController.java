package view;

import app.Photos;
import app.model.Album;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import app.model.User;

import java.io.IOException;
import java.util.Optional;


/**
 * Controller for the User Home view.
 * This class handles the logic for managing user albums, including creating, renaming, deleting, and opening albums.
 * It also provides functionality for logging out, searching photos, and viewing tag types.
 * The controller interacts with the {@link Photos} application and the {@link User} model.
 *
 * <p>Features include:
 * <ul>
 *   <li>Displaying a list of albums</li>
 *   <li>Managing albums (create, delete, rename)</li>
 *   <li>Opening an album view</li>
 *   <li>Logging out and switching views</li>
 * </ul>
 * </p>
 *
 * @author Reuben Thomas, Ryan Zaken
 */
public class UserHomeController {

    /**
     * The ListView for displaying the user's albums.
     */
    @FXML
    private ListView<Album> albumList;

    /**
     * The Label for displaying status messages to the user.
     */
    @FXML
    private Label statusLabel;

    /**
     * The currently logged-in {@link User}.
     */
    private User user;

    /**
     * The main {@link Photos} application instance.
     */
    private Photos app;

    /**
     * Initializes the controller with the application and user data.
     *
     * @param app  The main {@link Photos} application instance.
     * @param user The currently logged-in {@link User}.
     */
    public void init(Photos app, User user) {
        this.user = user;
        this.app = app;
        System.out.println("UserHomeController initialized with user: " + user.getUsername());
        System.out.println("Albums: " + user.getAlbumNames());

        populateAlbums();
        System.out.println("User logged in: " + user.getUsername());
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return The {@link User} object representing the logged-in user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Populates the ListView with the user's albums.
     * Sets up a custom cell factory to display album names.
     */
    public void populateAlbums() {
        System.out.println(user.getUsername());
        System.out.println(user.getAlbumNames());

        albumList.getItems().clear();
        albumList.getItems().addAll(user.getAlbums());
        System.out.println("Albums populated: " + albumList.getItems());

        albumList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null) {
                    setText(null);
                } else {
                    setText(album.toString());
                }
            }
        });
    }

    /**
     * Sets a status message to be displayed in the status label.
     *
     * @param message The status message to display.
     */
    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Displays an error message in an alert dialog.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Displays a warning message in an alert dialog.
     *
     * @param message The warning message to display.
     */
    public void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
    }

    /**
     * Displays a confirmation dialog with the given message.
     *
     * @param message The confirmation message to display.
     * @return An {@link Optional} containing the user's response.
     */
    public Optional<ButtonType> showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        return alert.showAndWait();
    }

    /**
     * Handles the logout action by switching to the login view.
     */
    @FXML
    public void handleLogout() {
        app.switchToLoginView((Stage) albumList.getScene().getWindow());
    }

    /**
     * Handles the action to view the user's tag types.
     * Loads and displays the tag types dialog.
     */
    @FXML
    public void handleViewTagTypes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/view-tag-types-dialog.fxml"));
            DialogPane dialogPane = loader.load();

            ViewTagTypesController controller = loader.getController();
            controller.init(user);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("View My Tag Types");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (IOException e) {
            showError("Failed to load tag types dialog: " + e.getMessage());
        }
    }

    /**
     * Handles the action to search photos.
     * Loads and displays the search photos view.
     */
    @FXML
    public void handleSearchPhotos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/search-photos-view.fxml"));
            Scene scene = new Scene(loader.load(), 640, 600);
            SearchPhotosController controller = loader.getController();
            controller.init(this);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Search Photos");
            stage.show();
        } catch (IOException e) {
            showError("Failed to load search photos view: " + e.getMessage());
        }
    }

    /**
     * Handles the action to create a new {@link Album}.
     * Prompts the user for an album name and adds it to the user's albums.
     */
    @FXML
    public void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter album name:");
        dialog.setContentText("Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (user.addAlbum(name) == null) {
                showError("Album name already exists!");
                return;
            }
            populateAlbums();
        });
    }

    /**
     * Handles the action to delete the selected {@link Album}.
     * Prompts the user for confirmation before deleting.
     */
    @FXML
    public void handleDeleteAlbum() {
        Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            if (selectedAlbum.getName().equalsIgnoreCase("stock")) {
                showWarning("Cannot delete stock album!");
                return;
            }
            if (selectedAlbum.getPhotoCount() > 0) {
                showWarning("Album is not empty! Cannot delete.");
                return;
            }
            Optional<ButtonType> result = showConfirmation("Are you sure you want to delete this album?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                user.deleteAlbum(selectedAlbum.getName());
                populateAlbums();
                setStatusMessage("Album deleted: " + selectedAlbum.getName());
            }
        } else {
            showWarning("No album selected!");
        }
    }

    /**
     * Handles the action to rename the selected {@link Album}.
     * Prompts the user for a new album name and updates the album.
     */
    @FXML
    public void handleRenameAlbum() {
        Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            if (selectedAlbum.getName().equalsIgnoreCase("stock")) {
                showWarning("Cannot rename stock album!");
                return;
            }
            TextInputDialog dialog = new TextInputDialog(selectedAlbum.getName());
            dialog.setTitle("Rename Album");
            dialog.setHeaderText("Enter new album name:");
            dialog.setContentText("Name:");
            dialog.showAndWait().ifPresent(newName -> {
                user.renameAlbum(selectedAlbum.getName(), newName);
                populateAlbums();
            });
        } else {
            showWarning("No album selected!");
        }
    }

    /**
     * Handles the action to open the selected {@link Album}.
     * Loads and displays the album view.
     */
    @FXML
    public void handleOpenAlbum() {
        Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            openAlbumView(selectedAlbum);
        } else {
            showWarning("No album selected!");
        }
    }

    /**
     * Opens the album view for the specified album.
     *
     * @param album The {@link Album} to open.
     */
    private void openAlbumView(Album album) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/album-view.fxml"));
            Scene scene = new Scene(loader.load(), 640, 650);
            AlbumController controller = loader.getController();
            controller.init(app, user, album);
            Stage stage = (Stage) albumList.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Viewing Album: " + album.getName());
        } catch (IOException e) {
            showError("Failed to load album view: " + e.getMessage());
        }
    }
}