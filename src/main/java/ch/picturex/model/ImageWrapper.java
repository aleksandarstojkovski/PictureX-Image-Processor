package ch.picturex.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")

public class ImageWrapper {

    private File file;
    private String name;
    private Image thumbnail;
    private long sizeInBytes;
    private static long totalSizeInBytes = 0;
    private String tooltipString;
    private ImageView thumbnailImageView = new ImageView();
    private List<BufferedImage> versionHistory = new ArrayList<>();
    private int index = 0;


    public ImageWrapper(File file) {
        init(file);
    }

    private void init(File file){
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

    public void set(BufferedImage bufferedImage){
        saveVersion();
        try {
            ImageIO.write(bufferedImage, getExtension(), getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        init(file);
    }

    public File getFile() {
        return file;
    }

    public Image getPreviewImageView() {
        return new Image(file.toURI().toString());
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    private long getSizeInBytes() {
        return sizeInBytes;
    }

    private long getSizeInMegaBytes() {
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

    String getTooltipString() {
        return tooltipString;
    }

    ImageView getThumbnailImageView() {
        return thumbnailImageView;
    }

    private BufferedImage getBufferedImage(){
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    private String getExtension(){
        return file.getName().substring(file.getName().lastIndexOf(".")).substring(1);
    }

    public void undo(){
        if (index > 0){
            index--;
            try {
                ImageIO.write(versionHistory.get(index), getExtension(), getFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            init(file);
        }
    }

    private void saveVersion(){
        versionHistory.add(getBufferedImage());
        index++;
    }

}
