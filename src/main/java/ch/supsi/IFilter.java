package ch.supsi;

import ch.supsi.ThumbnailContainer;

import java.util.HashMap;
import java.util.Map;

public interface IFilter {

    void apply(ThumbnailContainer tc, Map<String, Object> parameters);

}
