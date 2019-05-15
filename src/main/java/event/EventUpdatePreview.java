package event;

import ch.supsi.ThumbnailContainer;

public class EventUpdatePreview {

    private ThumbnailContainer tc;

    public EventUpdatePreview(ThumbnailContainer tc) {
        this.tc = tc;
    }

    public ThumbnailContainer getThubnailContainer() {
        return tc;
    }
}


