package ch.supsi;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class FXApp extends Application {

    private static final String ROOT_FOLDER = "c:/";
    @Override

    public void start(Stage stage) throws Exception {

        try{

            FXMLLoader loader = new FXMLLoader();
            URL fxmlLocation = getClass().getResource("/fxml/guiApp.fxml");
            loader.setLocation(fxmlLocation);
            //GuiAppController controller = loader.<GuiAppController>getController();

            BorderPane rootLayout = loader.load();
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
