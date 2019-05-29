package ch.picturex.filters;

import ch.picturex.events.EventZoom;
import ch.picturex.model.Model;
import ch.picturex.model.ThumbnailContainer;

import java.util.Map;

@SuppressWarnings("unused")

public class Zoom implements IFilter {

    private Model model = Model.getInstance();

    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {

        model.publish(new EventZoom((String) parameters.get("direction")));

    }

}
