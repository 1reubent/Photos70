package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.File;
import app.model.User;
import java.io.IOException;


public class UserHomeController {
    @FXML private ListView<String> photoList;
    @FXML private Label statusLabel;
    private User user;

    public void init(User user) {
        this.user = user;
        // Additional initialization code here
    }
    @FXML
    public void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Photo");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            photoList.getItems().add(selectedFile.getAbsolutePath());
            statusLabel.setText("Imported: " + selectedFile.getName());
        }
    }
    @FXML
    public void handleAddTag() {
        // Placeholder for tag adding logic
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add Tag - Coming Soon!");
        alert.showAndWait();
    }

    @FXML
    public void handleRemoveTag() {
        // Placeholder for tag removing logic
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Remove Tag - Coming Soon!");
        alert.showAndWait();
    }


    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 320, 240);
            Stage stage = (Stage) photoList.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}