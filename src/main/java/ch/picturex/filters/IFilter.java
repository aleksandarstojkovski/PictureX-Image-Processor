package ch.picturex.filters;

import ch.picturex.ThumbnailContainer;

import java.util.Map;

public interface IFilter {

    void apply(ThumbnailContainer tc, Map<String, Object> parameters);

}
