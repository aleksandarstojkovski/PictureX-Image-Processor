package ch.picturex.filters;

import ch.picturex.Model;
import ch.picturex.model.ThumbnailContainer;
import ch.picturex.events.EventZoom;
import java.util.Map;

@SuppressWarnings("unused")

public class Zoom implements IFilter {

    private Model model = Model.getInstance();

    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {

        model.publish(new EventZoom((String)parameters.get("direction")));

    }

}
