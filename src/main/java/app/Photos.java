package app;

import app.model.UserList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginController;

import java.io.IOException;

public class Photos extends Application {
    private UserList userList;

    @Override
    public void start(Stage stage) throws IOException {
        // Load user list from disk or initialize empty
        try {
            userList = UserList.load(getClass().getResource("/users.json").getPath());
        } catch (IOException e) {
            userList = new UserList();
        }
        userList.initializeStockPhotos();
        System.out.println("Users loaded: " + userList.getAllUsers().keySet());

        FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("/view/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        LoginController loginController = fxmlLoader.getController();
        loginController.setUserList(userList);
        stage.setTitle("Photo Album");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws IOException {
        UserList.save(userList, getClass().getResource("/users.json").getPath());
    }

    public static void main(String[] args) {
        launch(args);
    }
}