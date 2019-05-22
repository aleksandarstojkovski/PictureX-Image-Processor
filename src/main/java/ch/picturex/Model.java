package ch.picturex;

import ch.picturex.events.EventDirectoryChanged;
import ch.picturex.events.EventSelectedThumbnailContainers;
import ch.picturex.model.ThumbnailContainer;
import de.muspellheim.eventbus.EventBus;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

@SuppressWarnings("unused")

public class Model {

    private static Model model = null;
    private static EventBus bus = null;
    private File chosenDirectory = null;
    private ArrayList<ThumbnailContainer> selectedThumbnailContainers = null;
    private ResourceBundle resourceBundle = null;
    private Locale locale;

    private Model(){}

    public static Model getInstance(){
        if (model == null){
            model=new Model();
            bus=new EventBus();
            configureBus();
            setPreferences();
            setResourceBundle();
        }
        return model;
    }

    private static void configureBus(){
        bus.subscribe(EventDirectoryChanged.class, e->{
            model.chosenDirectory = e.getFile();
            model.setLastDirectoryPreferences(e.getFile());
        });
        bus.subscribe(EventSelectedThumbnailContainers.class, e->
            model.selectedThumbnailContainers = e.getSelectedThumbnailContainers()
        );
    }

    private static void setPreferences(){
        Preferences preference = Preferences.userNodeForPackage(Model.class);
        String filePath = preference.get("directory", null);
        if (filePath != null)
            model.chosenDirectory = new File(filePath);
    }

    private static void setResourceBundle(){
        Preferences preference = Preferences.userNodeForPackage(Model.class);
        String language = preference.get("language", "en");
        if(language.equals("en")){
            model.locale = Locale.ENGLISH;
        } else {
            model.locale = Locale.ITALIAN;
        }
        Locale.setDefault(model.locale);
        model.resourceBundle = ResourceBundle.getBundle("i18n/stringhe");
    }

    public Locale getLocale(){
        return  model.locale;
    }

    public File getChosenDirectory() {
        return model.chosenDirectory;
    }

    public ArrayList<ThumbnailContainer> getSelectedThumbnailContainers() {
        return model.selectedThumbnailContainers;
    }

    private void setLastDirectoryPreferences(File file){
        Preferences preference = Preferences.userNodeForPackage(Model.class);
        if (file != null) {
            preference.put("directory", file.getPath());
        }
    }

    public <T> void subscribe(Class<? extends T> eventType, Consumer<T> subscriber){
        bus.subscribe(eventType,subscriber);
    }

    public void publish(Object event){
        bus.publish(event);
    }

    public ResourceBundle getResourceBundle() {
        return model.resourceBundle;
    }

}