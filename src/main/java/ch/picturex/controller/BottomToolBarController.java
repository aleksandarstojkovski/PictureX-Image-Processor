package ch.picturex.controller;


import ch.picturex.ImageWrapper;
import de.muspellheim.eventbus.EventBus;
import ch.picturex.events.EventUpdateBottomToolBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;
import java.util.List;

public class BottomToolBarController {

    @FXML
    private Label numberOfFilesLabel;
    @FXML
    private Label totalSizeLabel;
    @FXML
    private Label browseTextField;

    static EventBus bus;

    public void initialize() {
        bus = new EventBus();
        bus.subscribe(EventUpdateBottomToolBar.class, e->populateBottomPane(e.getListOfImageWrappers(), e.getFile()));
    }

    private void populateBottomPane(List<ImageWrapper> listOfImageWrappers, File file){
        browseTextField.setText(file.getAbsolutePath());
        numberOfFilesLabel.setText(listOfImageWrappers.size() + " elementi");
        if (ImageWrapper.getTotalSizeInMegaBytes() <= 1)
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInBytes() + " Bytes");
        else
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInMegaBytes() + " MB");
    }

}
