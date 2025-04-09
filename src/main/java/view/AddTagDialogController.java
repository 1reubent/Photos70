package view;

import app.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class AddTagDialogController {

  @FXML
  private ComboBox<String> tagTypeCombo;
  @FXML
  private TextField valueField;
  @FXML
  private Button createTagButton;

  private User user;
  private boolean confirmed = false;
  private String selectedTagType;
  private String tagValue;

  public void init(User user) {
    this.user = user;
    tagTypeCombo.getItems().addAll(user.getTagTypes().keySet());

    createTagButton.setOnAction(e -> {
      Dialog<ButtonType> newTagDialog = new Dialog<>();
      newTagDialog.setTitle("Create New Tag Type");
      newTagDialog.setHeaderText("Create a new tag type");

      GridPane newTagGrid = new GridPane();
      newTagGrid.setHgap(10);
      newTagGrid.setVgap(10);
      newTagGrid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

      TextField nameField = new TextField();
      CheckBox multiValueCheckBox = new CheckBox("Allow multiple values");

      newTagGrid.add(new Label("Tag Name:"), 0, 0);
      newTagGrid.add(nameField, 1, 0);
      newTagGrid.add(multiValueCheckBox, 1, 1);

      newTagDialog.getDialogPane().setContent(newTagGrid);
      newTagDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

      Optional<ButtonType> newTagResult = newTagDialog.showAndWait();
      if (newTagResult.isPresent() && newTagResult.get() == ButtonType.OK) {
        String newTagName = nameField.getText().trim();
        if (!newTagName.isEmpty()) {
          if (user.addTagType(newTagName, multiValueCheckBox.isSelected())) {
            tagTypeCombo.getItems().clear();
            tagTypeCombo.getItems().addAll(user.getTagTypes().keySet());
            tagTypeCombo.setValue(newTagName);
          } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Tag type already exists!");
            alert.showAndWait();
          }
        } else {
          Alert alert = new Alert(Alert.AlertType.WARNING, "Tag type name cannot be empty!");
          alert.showAndWait();
        }
      }
    });
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public String getSelectedTagType() {
    return selectedTagType;
  }

  public String getTagValue() {
    return tagValue;
  }

  @FXML
  private void initialize() {
    tagTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> validate());
    valueField.textProperty().addListener((obs, oldVal, newVal) -> validate());
  }

  private void validate() {
    boolean valid = tagTypeCombo.getValue() != null && !valueField.getText().trim().isEmpty();
    confirmed = valid;
  }

  public void confirm() {
    selectedTagType = tagTypeCombo.getValue();
    tagValue = valueField.getText().trim();
  }
}