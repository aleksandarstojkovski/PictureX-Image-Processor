package ch.picturex;

import ch.picturex.events.EventLanguageChange;
import ch.picturex.model.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

@SuppressWarnings("FieldCanBeLocal")

public class FXApp extends Application {

    private Model model = Model.getInstance();
    private Stage primaryStage;
    private String appName = "PictureX";
    private String mainFxml = "/fxml/main.fxml";
    private String appIcon = "/icons/icon.png";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        model.setPrimaryStage(primaryStage);
        configureBus();
        Parent root = FXMLLoader.load(getClass().getResource(mainFxml), model.getResourceBundle());
        primaryStage.setTitle(appName);
        primaryStage.setScene(new Scene(root, 1040, 700));
        primaryStage.getIcons().add(new Image(appIcon));
        primaryStage.show();
        root.requestFocus();
    }

    @Override
    public void stop() {
        model.destroy();
        Platform.exit();
    }

    private void configureBus() {
        model.subscribe(EventLanguageChange.class, e -> reloadUI());
    }

    private void reloadUI() {
        if (closeWindowEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST), primaryStage)) {
            model.destroy();
            model = Model.getInstance();
            try {
                primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource(mainFxml), model.getResourceBundle()), 1040, 700));
            } catch (IOException e) {
                e.printStackTrace();
            }
            model.setPrimaryStage(primaryStage);
            Platform.runLater(() -> primaryStage.show());
            configureBus();
        }
    }

    private boolean closeWindowEvent(WindowEvent event, Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.CANCEL);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle(model.getResourceBundle().getString("alert.title"));
        alert.setContentText(model.getResourceBundle().getString("alert.text"));
        alert.initOwner(primaryStage.getOwner());
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setResizable(true);
        stage.getIcons().add(new Image(appIcon));
        Optional<ButtonType> res = alert.showAndWait();
        if (res.isPresent()) {
            if (res.get().equals(ButtonType.CANCEL)) {
                event.consume();
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
