package ch.supsi;

import com.sun.javafx.scene.traversal.Algorithm;

import java.io.File;
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

    public static void apply(ArrayList<ThumbnailContainer> listOfTC, String filterName, HashMap<String, Object> parameters) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        for (ThumbnailContainer tc : listOfTC) {
            Class<IFilter> cls = (Class<IFilter>) Class.forName("ch.supsi." + filterName);
            Constructor<IFilter> c = cls.getConstructor();
            IFilter o = c.newInstance();
            Method m = cls.getMethod("apply", ThumbnailContainer.class, HashMap.class);
            m.invoke(o,tc, null);
        }
    }

//    public static void undo() {
//        version--;
//
//        Class filter = Class.forName("ch.supsi"+filterName);
//    }
//
//    private static void saveVersion(ThumbnailContainer tc){
//        versions.add(tc);
//        version++;
//    }

}
