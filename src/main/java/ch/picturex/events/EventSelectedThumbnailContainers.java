package ch.picturex.events;

import ch.picturex.model.ThumbnailContainer;
import java.util.ArrayList;

public class EventSelectedThumbnailContainers {

    private ArrayList<ThumbnailContainer> selectedThumbnailContainers;

    public EventSelectedThumbnailContainers(ArrayList<ThumbnailContainer> selectedThumbnailContainers){
        this.selectedThumbnailContainers=selectedThumbnailContainers;
    }

    public ArrayList<ThumbnailContainer> getSelectedThumbnailContainers() {
        return selectedThumbnailContainers;
    }
}