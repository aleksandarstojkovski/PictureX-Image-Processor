package ch.picturex.controller;

import ch.picturex.Model;
import ch.picturex.events.*;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.Notifications;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;
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
        resizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventOpenDialogResize()));
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
        model.subscribe(EventOpenDialogResize.class, e->resizeButton());
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
                    .title(model.getResourceBundle().getString("notify.changeLanguage.title"))
                    .text(model.getResourceBundle().getString("notify.changeLanguage.text"))
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
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle(model.getResourceBundle().getString("dialog.title"));
        dialog.setHeaderText(model.getResourceBundle().getString("dialog.text"));

// Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("icons/resize.png").toString()));

// Set the button types.
        ButtonType modifyButton = new ButtonType(model.getResourceBundle().getString("dialog.title"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButton, ButtonType.CANCEL);

// Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        String INITAL_VALUE = "100";
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };
        TextFormatter<Integer> widthFormatter = new TextFormatter<>(
                new IntegerStringConverter(), 0, filter);
        Spinner<Integer> widthSpinner = new Spinner<>();
        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 10000, Integer.parseInt(INITAL_VALUE)));
        widthSpinner.setEditable(true);
        widthSpinner.getEditor().setTextFormatter(widthFormatter);

        TextFormatter<Integer> heightFormatter = new TextFormatter<>(
                new IntegerStringConverter(), 0, filter);
        Spinner<Integer> heightSpinner = new Spinner<>();
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 10000, Integer.parseInt(INITAL_VALUE)));
        heightSpinner.setEditable(true);
        heightSpinner.getEditor().setTextFormatter(heightFormatter);

        grid.add(new Label(model.getResourceBundle().getString("dialog.heightLabel")), 0, 0);
        grid.add(widthSpinner, 1, 0);
        grid.add(new Label(model.getResourceBundle().getString("dialog.widthLabel")), 0, 1);
        grid.add(heightSpinner, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> widthSpinner.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButton) {
                return new Pair<>(widthSpinner.getValue(), heightSpinner.getValue());
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(e -> {
            Filters.apply(model.getSelectedThumbnailContainers(),"Resize", Map.of("width", result.get().getKey(), "height", result.get().getValue()));
            System.out.println("width=" + e.getValue() + ", height=" + e.getValue());
        });
    }


}
