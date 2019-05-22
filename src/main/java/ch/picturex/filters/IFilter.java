package ch.picturex.filters;

import ch.picturex.model.ThumbnailContainer;

import java.util.Map;

public interface IFilter {

    void apply(ThumbnailContainer tc, Map<String, Object> parameters);

}
