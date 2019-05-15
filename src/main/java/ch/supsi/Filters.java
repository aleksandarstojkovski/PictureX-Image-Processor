package ch.supsi;

import events.EventImageChanged;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Filters {

   // private static ArrayList<ThumbnailContainer> versions = new ArrayList<>();
    private static int version=0;
    Map<ArrayList<ThumbnailContainer>,ArrayList<ThumbnailContainer>> versioni = new HashMap<>();

    public static void apply(ArrayList<ThumbnailContainer> listOfTC, String filterName, Map<String, Object> parameters) {
        for (ThumbnailContainer tc : listOfTC) {
            Class<IFilter> cls = null;
            try {
                cls = (Class<IFilter>) Class.forName("filters." + filterName);
                Constructor<IFilter> constructor = cls.getConstructor();
                IFilter instanceOfIFilter = constructor.newInstance();
                Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                method.invoke(instanceOfIFilter,tc, parameters);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(listOfTC.size()==1)
            Controller.bus.publish(new EventImageChanged(listOfTC.get(0)));
    }

}
