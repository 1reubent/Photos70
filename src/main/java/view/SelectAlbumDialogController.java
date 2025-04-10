package view;

import app.model.Album;
import app.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class SelectAlbumDialogController {
  @FXML
  private ComboBox<Album> albumComboBox;

  private User user;

  public void init(User user) {
    this.user = user;
    ObservableList<Album> albums = FXCollections.observableArrayList(user.getAlbums());
    albumComboBox.setItems(albums);
  }

  public Album getSelectedAlbum() {
    return albumComboBox.getSelectionModel().getSelectedItem();
  }
}