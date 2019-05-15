package events;

import ch.supsi.ThumbnailContainer;

public class EventImageChanged {

    private ThumbnailContainer tc;

    public EventImageChanged(ThumbnailContainer tc) {
        this.tc = tc;
    }

    public ThumbnailContainer getThubnailContainer() {
        return tc;
    }
}


