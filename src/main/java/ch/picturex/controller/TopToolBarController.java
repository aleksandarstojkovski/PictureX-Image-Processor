package ch.picturex.controller;

import ch.picturex.filters.Filters;
import ch.picturex.SingleResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.Notifications;
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
    public Button undoButton;
    @FXML
    public BorderPane i18nButton;
    @FXML
    public Button resizeButton;

    private ResourceBundle resourceBundleService = SingleResourceBundle.getInstance();
    private Preferences preference = Preferences.userNodeForPackage(SingleResourceBundle.class);

    @FXML
    public void initialize() {

        zoomInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"Zoom",Map.of("direction","in")));

        zoomOutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"Zoom",Map.of("direction","out")));

        bNButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"BlackAndWhite",null));

        undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.undo());

        rotateSXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"Rotate", Map.of("direction", "left")));

        rotateDXButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"Rotate", Map.of("direction", "right")));

        resizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->
            Filters.apply(MainController.selectedThumbnailContainers,"Resize", Map.of("width", 50, "height", 50)));

        setI18NComboBox();

    }

    private void setI18NComboBox(){
        ComboBox<Locale> comboBox = new ComboBox<>();
        ObservableList<Locale> options = FXCollections.observableArrayList(Locale.ENGLISH, Locale.ITALIAN);
        comboBox.setItems(options);
        comboBox.setCellFactory(p -> new LanguageListCell());
        comboBox.getSelectionModel().select(SingleResourceBundle.getLocale());
        comboBox.setOnAction(event -> {
            preference.put("language",comboBox.getSelectionModel().getSelectedItem().getLanguage());
            Notifications.create()
                    .title(resourceBundleService.getString("notifica.cambiolingua.titolo"))
                    .text(resourceBundleService.getString("notifica.cambiolingua.testo"))
                    .showInformation();
        });
        i18nButton.setCenter(comboBox);
    }

}
