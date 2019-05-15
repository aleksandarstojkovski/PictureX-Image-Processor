package ch.supsi;

import java.io.File;
import java.util.HashMap;

interface IFilter {

    void apply(ThumbnailContainer tc, HashMap<String, Object> parameters);
}
