package ch.picturex.controller;

import ch.picturex.model.Model;
import ch.picturex.events.*;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
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

    @FXML
    private void setItaLanguage(){
        preference.put("language","it");
        model.publish(new EventLanguageChange());
    }

    @FXML
    private void setEngLanguage(){
        preference.put("language","en");
        model.publish(new EventLanguageChange());
    }


}
