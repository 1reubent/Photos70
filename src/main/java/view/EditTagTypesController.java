package view;

import app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EditTagTypesController {
  @FXML
  private ListView<String> tagTypeList;
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
    tagTypeList.getItems().addAll(user.getTagTypeNames());
    configureButtons();
  }

  private void configureButtons() {
    addTagTypeButton.setOnAction(e -> {
      String newTagType = newTagTypeField.getText().trim();
      if (!newTagType.isEmpty()) {
        if (user.addTagType(newTagType, multiValueCheckBox.isSelected())) {
          tagTypeList.getItems().clear();
          tagTypeList.getItems().addAll(user.getTagTypeNames());
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
      String selectedTagType = tagTypeList.getSelectionModel().getSelectedItem();
      if (selectedTagType != null) {
        if (selectedTagType.equalsIgnoreCase("location") || selectedTagType.equalsIgnoreCase("people")) {
          showError("Cannot remove default tag types: location and people.");
          return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Tag Type");
        confirmationAlert.setHeaderText("Are you sure you want to remove this tag type?");
        confirmationAlert.setContentText("This will remove all tags of this type from all your photos.");
        confirmationAlert.showAndWait().ifPresent(response -> {
          if (response == ButtonType.OK) {
            if (user.removeTagType(selectedTagType)) {
              tagTypeList.getItems().remove(selectedTagType);
              removeTagsFromPhotos(selectedTagType);
              showInfo("Tag type and associated tags removed successfully.");
            } else {
              showError("Failed to remove tag type.");
            }
          }
        });
      } else {
        showError("No tag type selected!");
      }
    });
  }
  private void removeTagsFromPhotos(String tagType) {
    user.getAlbums().forEach(album -> {
      album.getPhotos().forEach(photo -> {
        photo.getTags().removeIf(tag -> tag.getName().equalsIgnoreCase(tagType));
      });
    });
  }
  private void showInfo(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
    alert.showAndWait();
  }
  private void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR, message);
    alert.showAndWait();
  }
}