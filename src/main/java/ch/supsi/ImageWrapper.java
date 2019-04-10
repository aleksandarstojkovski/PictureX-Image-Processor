package ch.supsi;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
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
    private List<BufferedImage> history = new ArrayList<>();
    private int index = 0;


    public ImageWrapper(File file) {
        set(file);
    }

    private void set(File newFile){
        this.file=newFile;
        name=newFile.getName();
        thumbnail = new Image(newFile.toURI().toString(),
                100, // requested width
                100, // requested height
                true, // preserve ratio
                false, // smooth rescaling
                true // load in background
        );
        thumbnailImageView.setImage(thumbnail);
        sizeInBytes=newFile.length()/(long)1024;
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

    public void applyBlackAndWhiteFilter(){
        try {
            BufferedImage image = ImageIO.read(file);
            addToHistory(image);

            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);
            graphic.dispose();
            ImageIO.write(result, this.getExtension(), file);
        }  catch (IOException e) {
            e.printStackTrace();
        }
        set(file);
    }

    public void undo(){
        if (index > 0){
            index--;
            try {
                ImageIO.write(history.get(index), this.getExtension(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            set(file);
        }
    }

    private void addToHistory(BufferedImage image){
        history.add(image);
        index++;
    }
}
