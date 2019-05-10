package ch.supsi;

import java.io.File;
import java.util.HashMap;

interface Filter {

    File modify(File file, HashMap<String, Object> parameters);
}
