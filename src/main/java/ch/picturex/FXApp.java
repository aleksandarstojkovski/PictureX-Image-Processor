package ch.picturex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class FXApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = Model.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"),model.getResourceBundle());
        primaryStage.setTitle("PictureX");
        primaryStage.setScene(new Scene(root, 1040, 700));
        primaryStage.getIcons().add(new Image("/icons/icon.png"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
