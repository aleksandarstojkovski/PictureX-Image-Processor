package ch.picturex.events;

import java.io.File;

public class EventDirectoryChosen {

    File file;

    public EventDirectoryChosen(File file){
        this.file=file;
    }

    public File getFile() {
        return file;
    }
}
