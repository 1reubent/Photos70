module photos70.photos70 {
  requires javafx.controls;
  requires javafx.fxml;


  opens photos70.photos70 to javafx.fxml;
  exports photos70.photos70;
}