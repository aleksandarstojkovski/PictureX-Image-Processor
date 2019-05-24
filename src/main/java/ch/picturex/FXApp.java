package ch.picturex;

import ch.picturex.events.EventLanguageChange;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

@SuppressWarnings("FieldCanBeLocal")

public class FXApp extends Application {

    private Model model = Model.getInstance();
    private Stage primaryStage;
    private String appName="PictureX";
    private String mainFxml="/fxml/main.fxml";
    private String appIcon="/icons/icon.png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        configureBus();
        Parent root = FXMLLoader.load(getClass().getResource(mainFxml),model.getResourceBundle());
        primaryStage.setTitle(appName);
        primaryStage.setScene(new Scene(root, 1040, 700));
        primaryStage.getIcons().add(new Image(appIcon));
        primaryStage.show();
    }

    @Override
    public void stop(){
        model.destroy();
    }

    private void configureBus(){
        model.subscribe(EventLanguageChange.class, e->reloadUI());
    }

    private void reloadUI(){
        primaryStage.close();
        model.destroy();
        model = Model.getInstance();
        try {
            primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource(mainFxml),model.getResourceBundle()), 1040, 700));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Platform.runLater(()->primaryStage.show());
        configureBus();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
