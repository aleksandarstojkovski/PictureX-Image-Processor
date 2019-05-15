package ch.supsi;

import ij.ImagePlus;
import ij.process.ImageConverter;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class BlackAndWhiteFilter implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, HashMap<String, Object> parameters) {
        ImagePlus imp = null;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(),  ImageIO.read(tc.getImageWrapper().getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        imp.updateAndDraw();
        try {
            ImageIO.write(imp.getBufferedImage(), tc.getImageWrapper().getExtension(), tc.getImageWrapper().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tc.getImageWrapper().set(tc.getImageWrapper().getFile());
    }


}
