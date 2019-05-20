package ch.picturex.events;

import ch.picturex.ThumbnailContainer;

import java.util.ArrayList;
import java.util.List;

public class EventSelectedThumbnailContainers {

    ArrayList<ThumbnailContainer> selectedThumbnailContainers;

    EventSelectedThumbnailContainers(ArrayList<ThumbnailContainer> selectedThumbnailContainers){
        this.selectedThumbnailContainers=selectedThumbnailContainers;
    }

    public ArrayList<ThumbnailContainer> getSelectedThumbnailContainers() {
        return selectedThumbnailContainers;
    }
}
