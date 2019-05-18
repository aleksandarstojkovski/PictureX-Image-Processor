package ch.picturex.events;

import ch.picturex.ImageWrapper;

import java.io.File;
import java.util.List;

public class EventUpdateBottomToolBar {

    private List<ImageWrapper> listOfImageWrappers;
    private File file;

    public EventUpdateBottomToolBar(List<ImageWrapper> listOfImageWrappers, File file){
        this.listOfImageWrappers=listOfImageWrappers;
        this.file=file;
    }

    public List<ImageWrapper> getListOfImageWrappers() {
        return listOfImageWrappers;
    }

    public File getFile() {
        return file;
    }
}
