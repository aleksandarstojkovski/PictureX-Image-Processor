package ch.picturex;

import ch.picturex.controller.MainController;
import ch.picturex.events.EventImageChanged;
import ch.picturex.events.EventLog;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Filters {

    private static ResourceBundle resourceBundle = ResourceBundleService.getInstance();
    private static List<ArrayList<ThumbnailContainer>> selectionHistory = new ArrayList<>();

    public static void apply(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters) {
        saveSelection(thumbnailContainers);
        for (ThumbnailContainer tc : thumbnailContainers) {
            Class<IFilter> cls = null;
            try {
                cls = (Class<IFilter>) Class.forName("ch.picturex.filters." + filterName);
                Constructor<IFilter> constructor = cls.getConstructor();
                IFilter instanceOfIFilter = constructor.newInstance();
                Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                method.invoke(instanceOfIFilter,tc, parameters);
            } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                Notifications.create()
                        .title(resourceBundle.getString("notifica.formatononsupport.titolo"))
                        .text(resourceBundle.getString("notifica.formatononsupport.testo"))
                        .showWarning();
            }
            MainController.bus.publish(new EventLog("Filter "+ filterName + " applied on image: " + tc.getImageWrapper().getName(), Severity.INFO));
        }
        if(thumbnailContainers.size()==1)
            MainController.bus.publish(new EventImageChanged(thumbnailContainers.get(0)));
    }

    public static void saveSelection(List<ThumbnailContainer> thumbnailContainers){
        selectionHistory.add(new ArrayList<>(thumbnailContainers));
    }

    public static void undo(){
        if (selectionHistory.size()>0) {
            List<ThumbnailContainer> lastSelection = selectionHistory.get(selectionHistory.size()-1);
            for (ThumbnailContainer tc : lastSelection){
                tc.getImageWrapper().undo();
            }
            MainController.bus.publish(new EventImageChanged(lastSelection.get(0)));
            selectionHistory.remove(selectionHistory.size()-1);
        }
    }

}
