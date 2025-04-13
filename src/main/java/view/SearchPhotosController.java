package view;

import app.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for searching photos based on date range and tags.
 * This class handles the logic for filtering photos and displaying search results.
 * @author Reuben Thomas, Ryan Zaken
 */
public class SearchPhotosController {

  /** DatePicker for selecting the start date of the search range. */
  @FXML
  private DatePicker startDatePicker;

  /** DatePicker for selecting the end date of the search range. */
  @FXML
  private DatePicker endDatePicker;

  /** ToggleButton for selecting single tag search mode. */
  @FXML
  private ToggleButton singleTagToggle;

  /** ToggleButton for selecting conjunctive tag search mode. */
  @FXML
  private ToggleButton conjunctiveToggle;

  /** ToggleButton for selecting disjunctive tag search mode. */
  @FXML
  private ToggleButton disjunctiveToggle;

  /** VBox container for dynamically adding tag input fields. */
  @FXML
  private VBox tagInputContainer;

  /** VBox container for displaying search results. */
  @FXML
  private VBox searchResultsContainer;

  /** Label for displaying status messages. */
  @FXML
  private Label statusLabel;

  /** The current {@link User} performing the search. */
  private User user;

  /** Reference to the {@link UserHomeController} for accessing user-related operations. */
  private UserHomeController userHomeController;

  /** ToggleGroup for managing the search type toggle buttons. */
  private ToggleGroup searchTypeGroup;

  /**
   * Initializes the controller with the {@link User} and {@link UserHomeController}.
   *
   * @param userHomeController The {@link UserHomeController} instance.
   */
  public void init(UserHomeController userHomeController) {
    this.userHomeController = userHomeController;
    this.user = userHomeController.getUser();

    // Initialize the ToggleGroup and assign it to the ToggleButtons
    searchTypeGroup = new ToggleGroup();
    singleTagToggle.setToggleGroup(searchTypeGroup);
    conjunctiveToggle.setToggleGroup(searchTypeGroup);
    disjunctiveToggle.setToggleGroup(searchTypeGroup);

    // Add listener to update tag input fields based on selected toggle
    searchTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> updateTagInputs());
    updateTagInputs(); // Initialize the tag inputs
  }

  /**
   * Displays a warning dialog with the specified message.
   *
   * @param message The warning message to display.
   */
  private void showWarning(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Warning");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Displays an error dialog with the specified message.
   *
   * @param message The error message to display.
   */
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Updates the status message displayed in the UI.
   *
   * @param message The status message to display.
   */
  private void setStatusMessage(String message) {
    statusLabel.setText(message);
  }

  /**
   * Updates the tag input fields based on the selected search type.
   */
  private void updateTagInputs() {
    tagInputContainer.getChildren().clear(); // Clear existing inputs

    if (singleTagToggle.isSelected()) {
      tagInputContainer.getChildren().add(createTagInputRow("Tag Type:", "Tag Value:"));
    } else if (conjunctiveToggle.isSelected() || disjunctiveToggle.isSelected()) {
      tagInputContainer.getChildren().add(createTagInputRow("Tag Type 1:", "Tag Value 1:"));
      tagInputContainer.getChildren().add(createTagInputRow("Tag Type 2:", "Tag Value 2:"));
    }
  }

  /**
   * Creates a row of input fields for tag type and tag value.
   *
   * @param label1Text The text for the first label.
   * @param label2Text The text for the second label.
   * @return An HBox containing the input fields.
   */
  private HBox createTagInputRow(String label1Text, String label2Text) {
    HBox row = new HBox(10);
    Label label1 = new Label(label1Text);
    TextField field1 = new TextField();
    Label label2 = new Label(label2Text);
    TextField field2 = new TextField();
    row.getChildren().addAll(label1, field1, label2, field2);
    return row;
  }

  /**
   * Handles the search operation based on the selected criteria.
   * Filters {@link Photo}s by date range and {@link Tag}s, and displays the results.
   */
  @FXML
  public void handleSearch() {
    LocalDate startDate = startDatePicker.getValue();
    LocalDate endDate = endDatePicker.getValue();
    LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : LocalDate.of(1970, 1, 1).atStartOfDay();
    LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();

    // Apply date filtering
    List<Photo> searchResults = user.getPhotosInDateRange(startDateTime, endDateTime);

    // Collect tag inputs based on the selected search type
    if (singleTagToggle.isSelected()) {
      // Handle single tag search
      HBox tagRow = (HBox) tagInputContainer.getChildren().get(0);
      TagType tagType = user.getTagType(((TextField) tagRow.getChildren().get(1)).getText());
      String tagValue = ((TextField) tagRow.getChildren().get(3)).getText();
      searchResults = user.getPhotosWithSingleTag(new Tag(tagType, tagValue));
    } else if (conjunctiveToggle.isSelected()) {
      // Handle conjunctive tag search
      HBox tagRow1 = (HBox) tagInputContainer.getChildren().get(0);
      HBox tagRow2 = (HBox) tagInputContainer.getChildren().get(1);
      TagType tagType1 = user.getTagType(((TextField) tagRow1.getChildren().get(1)).getText());
      String tagValue1 = ((TextField) tagRow1.getChildren().get(3)).getText();
      TagType tagType2 = user.getTagType(((TextField) tagRow2.getChildren().get(1)).getText());
      String tagValue2 = ((TextField) tagRow2.getChildren().get(3)).getText();
      searchResults = user.getPhotosWithBothTags(new Tag(tagType1, tagValue1), new Tag(tagType2, tagValue2));
    } else if (disjunctiveToggle.isSelected()) {
      // Handle disjunctive tag search
      HBox tagRow1 = (HBox) tagInputContainer.getChildren().get(0);
      HBox tagRow2 = (HBox) tagInputContainer.getChildren().get(1);
      TagType tagType1 = user.getTagType(((TextField) tagRow1.getChildren().get(1)).getText());
      String tagValue1 = ((TextField) tagRow1.getChildren().get(3)).getText();
      TagType tagType2 = user.getTagType(((TextField) tagRow2.getChildren().get(1)).getText());
      String tagValue2 = ((TextField) tagRow2.getChildren().get(3)).getText();
      searchResults = user.getPhotosWithEitherTag(new Tag(tagType1, tagValue1), new Tag(tagType2, tagValue2));
    }

    // Display search results in the same window
    if (searchResults != null && !searchResults.isEmpty()) {
      ListView<Photo> resultsListView = new ListView<>();
      resultsListView.getItems().addAll(searchResults);
      resultsListView.setCellFactory(listView -> new ListCell<>() {
        @Override
        protected void updateItem(Photo photo, boolean empty) {
          super.updateItem(photo, empty);
          if (empty || photo == null) {
            setText(null);
            setGraphic(null);
          } else {
            // Create an ImageView for the photo thumbnail
            ImageView thumbnail = new ImageView(new Image("file:" + photo.getPath()));
            thumbnail.setFitWidth(50);
            thumbnail.setFitHeight(50);
            thumbnail.setPreserveRatio(true);

            // Get the album name the photo belongs to
            String albumNames = String.join(", ", user.getAlbumsContainingPhoto(photo).stream().map(Album::getName).toList());

            // Create a label for the photo details
            Label details = new Label(photo.toString() + " (Albums: " + albumNames + ")");

            // Create an HBox to hold the thumbnail and details
            HBox content = new HBox(10, thumbnail, details);
            setGraphic(content);
          }
        }
      });

      // Add a button to create an album from the search results
      Button createAlbumButton = new Button("Create Album from Results");
      List<Photo> finalSearchResults = searchResults; // needed for lambda
      createAlbumButton.setOnAction(event -> handleCreateAlbumFromResults(finalSearchResults));

      // Display results and button in the UI
      searchResultsContainer.getChildren().clear();
      searchResultsContainer.getChildren().addAll(new Label("Search Results:"), resultsListView, createAlbumButton);
    } else {
      showWarning("No photos found matching the search criteria.");
    }
  }

  /**
   * Handles the creation of a new {@link Album} from the search results.
   *
   * @param searchResults The list of {@link Photo}s to include in the new album.
   */
  private void handleCreateAlbumFromResults(List<Photo> searchResults) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Create Album");
    dialog.setHeaderText("Enter album name for search results:");
    dialog.setContentText("Name:");
    dialog.showAndWait().ifPresent(name -> {
      Album newAlbum = user.addAlbum(name);
      if (newAlbum == null) {
        showError("Album name already exists!");
        return;
      }
      // Add photos to the new album
      for (Photo photo : searchResults) {
        newAlbum.addPhoto(photo);
      }
      setStatusMessage("Album created: " + name);
      userHomeController.populateAlbums();
    });
  }
}