package ch.supsi;

import java.io.File;
import java.util.HashMap;

public class Filters {
    public void apply(File file, String filterName, HashMap<String, Object> parameters) throws ClassNotFoundException {
        Class filter = Class.forName("ch.supsi"+filterName);

    }
}
