package ch.picturex.controller;

import ch.picturex.Filters;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.Map;

public class TopToolBarController {

    @FXML
    public Button zoomInButton;
    @FXML
    public Button zoomOutButton;
    @FXML
    public Button bNButton;
    @FXML
    public Button rotateSXButton;
    @FXML
    public Button rotateDXButton;
    @FXML
    public Button shareButton;
    @FXML
    public Button undoButton;

    @FXML
    public void initialize() {
        zoomInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            // TODO: Filters.
        });
        bNButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            Filters.apply(MainController.selectedThumbnailContainers,"BlackAndWhite",null);
        });
        undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            Filters.undo();
        });
        rotateSXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            Filters.apply(MainController.selectedThumbnailContainers,"Rotate", Map.of("direction", "left"));
        });
        rotateDXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            Filters.apply(MainController.selectedThumbnailContainers,"Rotate", Map.of("direction", "right"));
        });
    }
}
