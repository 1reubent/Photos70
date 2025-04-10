package view;

import app.model.Album;
import app.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class SelectAlbumDialogController {
  @FXML
  private ListView<Album> albumListView;

  private User user;

  public void init(User user) {
    this.user = user;
    ObservableList<Album> albums = FXCollections.observableArrayList(user.getAlbums());
    albumListView.setItems(albums);
  }

  public Album getSelectedAlbum() {
    return albumListView.getSelectionModel().getSelectedItem();  }
}