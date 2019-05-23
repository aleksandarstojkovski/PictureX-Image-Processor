package ch.picturex.filters;

import ch.picturex.*;
import ch.picturex.events.EventImageChanged;
import ch.picturex.events.EventLog;
import ch.picturex.model.Severity;
import ch.picturex.model.ThumbnailContainer;
import org.controlsfx.control.Notifications;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Filters {

    private static Model model = Model.getInstance();
    private static List<ArrayList<ThumbnailContainer>> selectionHistory = new ArrayList<>();
    private static boolean success;

    @SuppressWarnings("unchecked")

    public static void apply(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters) {
        success=true;
        saveSelection(thumbnailContainers);
        for (ThumbnailContainer tc : thumbnailContainers) {
            Class<IFilter> cls;
            try {
                cls = (Class<IFilter>) Class.forName("ch.picturex.filters." + filterName);
                Constructor<IFilter> constructor = cls.getConstructor();
                IFilter instanceOfIFilter = constructor.newInstance();
                Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                method.invoke(instanceOfIFilter,tc, parameters);
            } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                success=false;
            }
            if (success){
                if(thumbnailContainers.size()==1)
                    model.publish(new EventImageChanged(thumbnailContainers.get(0)));
                model.publish(new EventLog("Filter " + filterName + " applied on image: " + tc.getImageWrapper().getName(),Severity.INFO));
            } else {
                Notifications.create()
                        .title(model.getResourceBundle().getString("notify.notSupportedFormat.title"))
                        .text(model.getResourceBundle().getString("notify.notSupportedFormat.text"))
                        .showWarning();
                model.publish(new EventLog("Unable to apply filter " + filterName + " to image: " + tc.getImageWrapper().getName(), Severity.ERROR));
            }
        }
    }

    private static void saveSelection(List<ThumbnailContainer> thumbnailContainers){
        if (thumbnailContainers.size()>0)
            selectionHistory.add(new ArrayList<>(thumbnailContainers));
    }

    public static void undo(){
        if (selectionHistory.size()>0) {
            List<ThumbnailContainer> lastSelection = selectionHistory.get(selectionHistory.size()-1);
            for (ThumbnailContainer tc : lastSelection){
                tc.getImageWrapper().undo();
            }
            model.publish(new EventImageChanged(lastSelection.get(0)));
            selectionHistory.remove(selectionHistory.size()-1);
        }
    }

    public static void clearHistory(){
        selectionHistory.clear();
    }

}
