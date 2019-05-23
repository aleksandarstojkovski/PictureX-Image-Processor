package ch.picturex.controller;

import ch.picturex.Model;
import ch.picturex.events.EventBrowseButton;
import ch.picturex.events.EventFilterBlackAndWhite;
import ch.picturex.events.EventFilterRotate;
import ch.picturex.events.EventOpenDialogResize;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuBarController implements Initializable {

    private Model model = Model.getInstance();

    @FXML
    MenuBar menuBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void BNFilterMetod(){
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

    public void resizeMetod(){
        model.publish(new EventOpenDialogResize());
    }

    @FXML
    private void handleBrowseButton(){
        model.publish(new EventBrowseButton());
    }

}
