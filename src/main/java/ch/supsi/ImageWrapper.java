package ch.supsi;

import ij.ImagePlus;
import ij.process.ImageConverter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageWrapper {

    private File file;
    private String name;
    private Image thumbnail;
    private long sizeInBytes;
    private static long totalSizeInBytes = 0;
    private String tooltipString;
    private ImageView thumbnailImageView = new ImageView();
    private Image previewImageView;
    private List<BufferedImage> versionHistory = new ArrayList<>();
    private int index = 0;


    public ImageWrapper(File file) {
        set(file);
    }

    public void set(File file){
        this.file=file;
        name=file.getName();
        thumbnail = new Image(file.toURI().toString(),
                100, // requested width
                100, // requested height
                true, // preserve ratio
                false, // smooth rescaling
                true // load in background
        );
        thumbnailImageView.setImage(thumbnail);
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

    public Image getPreviewImageView() {
        previewImageView=new Image(file.toURI().toString());
        return previewImageView;
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

    public ImageView getThumbnailImageView() {
        return thumbnailImageView;
    }

    public String getExtension(){
        return file.getName().substring(file.getName().lastIndexOf(".")).substring(1);
    }

    public void undoChange(){
        if (index > 0){
            index--;
            try {
                ImageIO.write(versionHistory.get(index), this.getExtension(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            set(file);
        }
    }

    private boolean saveVersion(){
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(image==null)
            return false; // null pointer eliminato per file non immagine
        versionHistory.add(image);
        index++;
        return true;
    }

}
