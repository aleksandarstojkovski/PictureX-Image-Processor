package ch.picturex;

import ch.picturex.controller.MainController;
import ch.picturex.events.EventDirectoryChanged;
import ch.picturex.events.EventSelectedThumbnailContainers;
import de.muspellheim.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class Model {

    private static Model model = null;
    private static EventBus bus = null;
    private File chosenDirectory = null;
    private ArrayList<ThumbnailContainer> selectedThumbnailContainers = null;

    private Model(){}

    public static Model getInstance(){
        if (model == null){
            model=new Model();
            bus=SingleEventBus.getInstance();
            configureBus();
            setPreferences();
        }
        return model;
    }

    private static void configureBus(){
        bus.subscribe(EventDirectoryChanged.class, e->{
            model.chosenDirectory = e.getFile();
            model.setLastDirectoryPreferences(e.getFile());
        });
        bus.subscribe(EventSelectedThumbnailContainers.class, e->{
            if (model.chosenDirectory == null)
                model.selectedThumbnailContainers = e.getSelectedThumbnailContainers();
        });
    }

    private static void setPreferences(){
        Preferences preference = Preferences.userNodeForPackage(MainController.class);
        String filePath = preference.get("filePath", null);
        if (filePath != null)
            model.chosenDirectory = new File(filePath);
    }

    public File getChosenDirectory() {
        return model.chosenDirectory;
    }

    public ArrayList<ThumbnailContainer> getSelectedThumbnailContainers() {
        return model.selectedThumbnailContainers;
    }

    private void setLastDirectoryPreferences(File file){
        Preferences preference = Preferences.userNodeForPackage(MainController.class);
        if (file != null) {
            preference.put("filePath", file.getPath());
        }
    }
}
