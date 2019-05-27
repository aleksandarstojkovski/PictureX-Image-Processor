package ch.picturex.controller;

import ch.picturex.model.Model;
import ch.picturex.model.ImageWrapper;
import ch.picturex.events.EventUpdateBottomToolBar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BottomToolBarController implements Initializable {

    @FXML
    private Label numberOfFilesLabel;
    @FXML
    private Label totalSizeLabel;
    @FXML
    private Label browseTextField;

    private Model model = Model.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model.subscribe(EventUpdateBottomToolBar.class, e->populateBottomPane(e.getListOfImageWrappers(), e.getFile()));
    }

    private void populateBottomPane(List<ImageWrapper> listOfImageWrappers, File file){
        browseTextField.setText(file.getAbsolutePath());
        numberOfFilesLabel.setText(listOfImageWrappers.size() + " " + model.getResourceBundle().getString("label.elements"));
        if (ImageWrapper.getTotalSizeInMegaBytes() <= 1)
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInBytes() + " Bytes");
        else
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInMegaBytes() + " MB");
    }

}
