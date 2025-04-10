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

  private User user;
  private boolean confirmed = false;
  private String selectedTagType;
  private String tagValue;

  public void init(User user) {
    this.user = user;
    tagTypeCombo.getItems().addAll(user.getTagTypeNamesWithMultiValue());
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public String getSelectedTagType() {
    return tagTypeCombo.getValue();
  }

  public String getTagValue() {
    return valueField.getText().trim();
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
}