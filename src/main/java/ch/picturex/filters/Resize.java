package ch.picturex.filters;


import ch.picturex.ThumbnailContainer;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

public class Resize implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp = null;
        ImageProcessor ip = null;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(), ImageIO.read(tc.getImageWrapper().getFile()));
            ip = imp.getProcessor();
            ip.resize((int)parameters.get("width"), (int)parameters.get("height"));
            System.out.println((int)parameters.get("width") +" "+ (int)parameters.get("height"));
            imp.setProcessor(ip);
            imp.updateAndDraw();
            tc.getImageWrapper().set(imp.getBufferedImage());
        } catch (IOException ignored){}
    }


}
