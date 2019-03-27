package ch.supsi;

import javafx.scene.image.Image;

import java.io.File;

public class ImageWrapper {

    private final File file;
    private String name;
    private Image originalImage = null;
    private Image thumbnail;
    private final long sizeInBytes;
    private static long totalSizeInBytes = 0;
    private String tooltipString;


    public ImageWrapper(File file) {
        this.file=file;
        name=file.getName();
        thumbnail = new Image(file.toURI().toString(),
                100, // requested width
                100, // requested height
                true, // preserve ratio
                false, // smooth rescaling
                true // load in background
        );
        sizeInBytes=file.length()/(long)1024;
        totalSizeInBytes+=sizeInBytes;
        if (getSizeInMegaBytes()<=1){
            tooltipString=String.format("Name:\t%s\nSize:\t\t%d Bytes", this.getName(), this.getSizeInBytes());
        } else {
            tooltipString=String.format("Name:\t%s\nSize:\t\t%d MB", this.getName(), this.getSizeInMegaBytes());
        }
    }

    public File getFile() {
        return file;
    }

    public Image getOriginalImage() {
        if (originalImage == null){
            originalImage=new Image(file.toURI().toString());
        }
        return originalImage;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public long getSizeInMegaBytes() {
        return sizeInBytes/1024;
    }

    public static long getTotalSizeInBytes() {
        return totalSizeInBytes;
    }

    public static long getTotalSizeInMegaBytes() {
        return totalSizeInBytes/1024;
    }

    public String getName() {
        return name;
    }

    public static void clear(){
        totalSizeInBytes=0;
    }

    public String getTooltipString() {
        return tooltipString;
    }
}
