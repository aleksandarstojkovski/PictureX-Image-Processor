package ch.picturex.events;

import java.io.File;

public class EventDirectoryChanged {

    private File file;

    public EventDirectoryChanged(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}