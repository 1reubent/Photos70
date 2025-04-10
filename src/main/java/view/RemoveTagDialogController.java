package view;

import app.model.Photo;
import app.model.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class RemoveTagDialogController {

  @FXML
  private ListView<Tag> tagListView;

  private Photo photo;
  private Tag selectedTag;

  public void init(Photo photo) {
    this.photo = photo;
    tagListView.getItems().addAll(photo.getTags());
    tagListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> selectedTag = newVal);
  }

  public Tag getSelectedTag() {
    return selectedTag;
  }
}