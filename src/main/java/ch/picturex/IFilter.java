package ch.picturex;

import java.util.Map;

public interface IFilter {

    void apply(ThumbnailContainer tc, Map<String, Object> parameters);

}
