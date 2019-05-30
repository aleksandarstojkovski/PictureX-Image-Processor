package ch.picturex.controller;

import ch.picturex.events.*;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import ch.picturex.model.Model;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class MenuBarController implements Initializable {

    private Model model = Model.getInstance();
    private Preferences preference = Preferences.userNodeForPackage(Model.class);

    @FXML
    MenuBar menuBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void BNFilterMetod() {
        model.publish(new EventFilterBlackAndWhite());
    }

    public void rotateSXMetod() {
        model.publish(new EventFilterRotate(Direction.LEFT));
    }

    public void rotateDXMetod() {
        model.publish(new EventFilterRotate(Direction.RIGHT));
    }

    public void undo() {
        Filters.undo();
    }

    public void handleCloseButtonAction() {
        Platform.exit();
    }

    public void resizeMetod() {
        model.publish(new EventOpenDialogResize());
    }

    @FXML
    private void handleBrowseButton() {
        model.publish(new EventBrowseButton());
    }

    @FXML
    private void setItaLanguage() {
        preference.put("language", "it");
        model.publish(new EventLanguageChange());
    }

    @FXML
    private void setEngLanguage() {
        preference.put("language", "en");
        model.publish(new EventLanguageChange());
    }

    @FXML
    private void zoomIn() {
        model.publish(new EventZoom("in"));
    }

    @FXML
    private void zoomOut() {
        model.publish(new EventZoom("out"));
    }

    @FXML
    private void zoomReset() {
        model.publish(new EventZoom("reset"));
    }

    @FXML
    private void help(){
        Dialog dialog = new Dialog<>();
        Image image = new Image(getClass().getResource("/icons/icon2.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        dialog.setTitle(model.getResourceBundle().getString("dialog.about.title"));
        dialog.setHeaderText(model.getResourceBundle().getString("dialog.about.appName"));

        // Set the icon (must be included in the project).
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/icons/icon.png"));
        dialog.setResizable(false);
        dialog.getDialogPane().setPrefSize(480, 320);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        GridPane grid = new GridPane();

        grid.setHgap(100);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        Label text = new Label(model.getResourceBundle().getString("dialog.about.text"));
        text.setTextAlignment(TextAlignment.CENTER);
        grid.add(text, 0, 0);
        grid.add(imageView, 1, 0);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHalignment(imageView, HPos.RIGHT);

        dialog.getDialogPane().setContent(grid);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2), grid);
        scaleTransition.setFromX(5);
        scaleTransition.setFromY(5);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);

        scaleTransition.play();
        dialog.showAndWait();


    }


}
