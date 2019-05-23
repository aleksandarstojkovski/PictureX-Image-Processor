package ch.picturex.controller;

import ch.picturex.Model;
import ch.picturex.events.*;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.Notifications;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

class LanguageListCell extends ListCell<Locale> {
    @Override protected void updateItem(Locale item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText(item.getLanguage());
        }
    }
}

public class TopToolBarController implements Initializable {

    @FXML
    public Button zoomInButton;
    @FXML
    public Button zoomOutButton;
    @FXML
    public Button blackAndWhiteButton;
    @FXML
    public Button rotateLeftButton;
    @FXML
    public Button rotateRightButton;
    @FXML
    public Button undoButton;
    @FXML
    public BorderPane i18nButton;
    @FXML
    public Button resizeButton;

    private Preferences preference = Preferences.userNodeForPackage(Model.class);
    private Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureBus();
        installTooltips();
        setHandlers();
        setI18NComboBox();
    }

    private void installTooltips(){
        Tooltip.install(zoomInButton, new Tooltip("Zoom In"));
        Tooltip.install(zoomOutButton, new Tooltip("Zoom Out"));
        Tooltip.install(blackAndWhiteButton, new Tooltip("Black And White Filter"));
        Tooltip.install(rotateLeftButton, new Tooltip("Rotate Left"));
        Tooltip.install(rotateRightButton, new Tooltip("Rotate Right"));
        Tooltip.install(undoButton, new Tooltip("Undo"));
        Tooltip.install(i18nButton, new Tooltip("Language"));
    }

    private void setHandlers(){
        zoomInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterZoom(Direction.IN)));
        zoomOutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterZoom(Direction.OUT)));
        blackAndWhiteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterBlackAndWhite()));
        undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterUndo()));
        rotateLeftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterRotate(Direction.LEFT)));
        rotateRightButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterRotate(Direction.RIGHT)));
        resizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterResize()));
    }

    private void configureBus(){
        model.subscribe(EventFilterRotate.class, e->{
            if (e.getDirection() == Direction.LEFT){
                rotateLeft();
            } else {
                rotateRight();
            }
        });
        model.subscribe(EventFilterZoom.class, e->{
            if (e.getDirection() == Direction.IN){
                zoomInButton();
            } else {
                zoomOutButton();
            }
        });
        model.subscribe(EventFilterUndo.class, e->undoButton());
        model.subscribe(EventFilterBlackAndWhite.class, e->blackAndWhiteButton());
        model.subscribe(EventFilterResize.class, e->resizeButton());
    }

    private void setI18NComboBox(){
        ComboBox<Locale> comboBox = new ComboBox<>();
        ObservableList<Locale> options = FXCollections.observableArrayList(Locale.ENGLISH, Locale.ITALIAN);
        comboBox.setItems(options);
        comboBox.setCellFactory(p -> new LanguageListCell());
        comboBox.getSelectionModel().select(model.getLocale());
        comboBox.setOnAction(event -> {
            preference.put("language",comboBox.getSelectionModel().getSelectedItem().getLanguage());
            Notifications.create()
                    .title(model.getResourceBundle().getString("notifica.cambiolingua.titolo"))
                    .text(model.getResourceBundle().getString("notifica.cambiolingua.testo"))
                    .showInformation();
        });
        i18nButton.setCenter(comboBox);
    }

    private void zoomInButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Zoom",Map.of("direction","in"));
    }

    private void zoomOutButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Zoom",Map.of("direction","out"));
    }

    private void blackAndWhiteButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"BlackAndWhite",null);
    }

    private void undoButton(){
        Filters.undo();
    }

    private void rotateLeft(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Rotate", Map.of("direction", "left"));
    }

    private void rotateRight(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Rotate", Map.of("direction", "right"));
    }

    private void resizeButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Resize", Map.of("width", 50, "height", 50));
    }


}
