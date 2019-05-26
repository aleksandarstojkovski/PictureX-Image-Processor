package ch.picturex.controller;

import ch.picturex.model.Model;
import ch.picturex.events.*;
import ch.picturex.filters.Filters;
import ch.picturex.model.Direction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;

@SuppressWarnings("unused")

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
    public Button resizeButton;
    @FXML
    public Button zoomResetButton;

    private Preferences preference = Preferences.userNodeForPackage(Model.class);
    private Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureBus();
        installTooltips();
        setHandlers();
    }

    private void installTooltips(){
        Tooltip.install(zoomInButton, new Tooltip("Zoom In"));
        Tooltip.install(zoomOutButton, new Tooltip("Zoom Out"));
        Tooltip.install(blackAndWhiteButton, new Tooltip("Black And White Filter"));
        Tooltip.install(rotateLeftButton, new Tooltip("Rotate Left"));
        Tooltip.install(rotateRightButton, new Tooltip("Rotate Right"));
        Tooltip.install(undoButton, new Tooltip("Undo"));
        Tooltip.install(zoomResetButton, new Tooltip("Reset Zoom"));
    }

    private void setHandlers(){
        zoomInButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterZoom(Direction.IN)));
        zoomOutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterZoom(Direction.OUT)));
        blackAndWhiteButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterBlackAndWhite()));
        undoButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterUndo()));
        rotateLeftButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterRotate(Direction.LEFT)));
        rotateRightButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterRotate(Direction.RIGHT)));
        resizeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventOpenDialogResize()));
        zoomResetButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e-> model.publish(new EventFilterZoom(Direction.RESET)));
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
            } else if (e.getDirection() == Direction.OUT){
                zoomOutButton();
            } else {
                zoomResetButton();
            }
        });
        model.subscribe(EventFilterUndo.class, e->undoButton());
        model.subscribe(EventFilterBlackAndWhite.class, e->blackAndWhiteButton());
        model.subscribe(EventOpenDialogResize.class, e->resizeButton());
    }

    private void zoomInButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Zoom",Map.of("direction","in"));
    }

    private void zoomResetButton(){
        Filters.apply(model.getSelectedThumbnailContainers(),"Zoom",Map.of("direction","reset"));
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
        Stage stage = (Stage)dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/icons/icon.png"));


        // Set the button types.
        ButtonType modifyButton = new ButtonType(model.getResourceBundle().getString("dialog.title"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButton, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        String INITAL_VALUE = "1";
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

        TextFormatter<Integer> widthFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        Spinner<Integer> widthSpinner = new Spinner<>();
        TextFormatter<Integer> heightFormatter = new TextFormatter<>(new IntegerStringConverter(), 0, filter);
        Spinner<Integer> heightSpinner = new Spinner<>();

        widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, Integer.parseInt(INITAL_VALUE)));
        widthSpinner.setEditable(true);
        widthSpinner.getEditor().setTextFormatter(widthFormatter);
        heightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, Integer.parseInt(INITAL_VALUE)));
        heightSpinner.setEditable(true);
        heightSpinner.getEditor().setTextFormatter(heightFormatter);

        grid.add(new Label(model.getResourceBundle().getString("dialog.heightLabel")), 0, 0);
        grid.add(widthSpinner, 1, 0);
        grid.add(new Label(model.getResourceBundle().getString("dialog.widthLabel")), 0, 1);
        grid.add(heightSpinner, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(widthSpinner::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButton) {
                return new Pair<>(widthSpinner.getValue(), heightSpinner.getValue());
            }
            return null;
        });

        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();

        result.ifPresent(e ->
            Filters.apply(model.getSelectedThumbnailContainers(),"Resize", Map.of("width", result.get().getKey(), "height", result.get().getValue()))
        );
    }


}
