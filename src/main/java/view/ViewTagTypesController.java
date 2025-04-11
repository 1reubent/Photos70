package view;

import app.model.TagType;
import app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class ViewTagTypesController {
  @FXML
  private ListView<TagType> tagTypeList;
  @FXML
  private TextField newTagTypeField;
  @FXML
  private CheckBox multiValueCheckBox;
  @FXML
  private Button addTagTypeButton;
  @FXML
  private Button removeTagTypeButton;

  private User user;

  public void init(User user) {
    this.user = user;
    tagTypeList.getItems().addAll(user.getTagTypes());
    configureButtons();
  }

  private void configureButtons() {
    addTagTypeButton.setOnAction(e -> {
      String newTagTypeName = newTagTypeField.getText().trim();
      if (!newTagTypeName.isEmpty()) {
        if (user.addTagType(newTagTypeName, multiValueCheckBox.isSelected())) {
          tagTypeList.getItems().clear();
          tagTypeList.getItems().addAll(user.getTagTypes());
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

  private void removeTagsFromPhotos(TagType tagType) {
    user.getAlbums().forEach(album -> {
      album.getPhotos().forEach(photo -> {
        photo.getTags().removeIf(tag -> tag.getName().equalsIgnoreCase(tagType.getName()));
      });
    });
  }

  //TODO: do we need this?
  private void showInfo(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
    alert.setTitle("Information");
    alert.setHeaderText("Information");
    alert.showAndWait();
  }

  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.showAndWait();
  }

  //show confirmation dialog
  private Optional<ButtonType> showConfirmation(String message) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
    alert.setTitle("Confirmation");
    alert.setHeaderText("Confirmation");
    return alert.showAndWait();
  }
}