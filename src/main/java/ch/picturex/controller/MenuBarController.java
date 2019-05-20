package ch.picturex.controller;

import ch.picturex.Filters;
import ch.picturex.Model;
import ch.picturex.SingleEventBus;
import ch.picturex.events.EventBrowseButtonPressed;
import de.muspellheim.eventbus.EventBus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuBarController implements Initializable {

    @FXML
    private MenuBar menuBar;

    private Model model = Model.getInstance();
    private EventBus bus = SingleEventBus.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void BNFilterMetod(){
        Filters.apply(model.getSelectedThumbnailContainers(),"BlackAndWhite",null);
    }
    public void rotateSXMetod() {
        Filters.apply(model.getSelectedThumbnailContainers(), "Rotate", Map.of("direction", "left"));
    }
    public void rotateDXMetod() {
        Filters.apply(model.getSelectedThumbnailContainers(), "Rotate", Map.of("direction", "right"));
    }
    public void undo() {
        Filters.undo();
    }
    public void handleCloseButtonAction() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleBrowseButton(ActionEvent event){
        bus.publish(new EventBrowseButtonPressed());
    }

}
