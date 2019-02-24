package ch.supsi;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;

public class FXApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        try{

            FXMLLoader loader = new FXMLLoader();
            URL fxmlLocation = getClass().getResource("/fxml/guiApp.fxml");
            loader.setLocation(fxmlLocation);
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout, 400, 400);
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
