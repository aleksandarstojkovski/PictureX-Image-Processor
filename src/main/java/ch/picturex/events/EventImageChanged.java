package ch.picturex.events;

import ch.picturex.model.ThumbnailContainer;

public class EventImageChanged {

    private ThumbnailContainer tc;

    public EventImageChanged(ThumbnailContainer tc) {
        this.tc = tc;
    }

    public ThumbnailContainer getThubnailContainer() {
        return tc;
    }
}


