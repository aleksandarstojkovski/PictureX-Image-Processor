package ch.picturex.filters;


import ch.picturex.model.ThumbnailContainer;
import ij.ImagePlus;
import ij.process.ImageProcessor;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unused")

public class Resize implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp;
        ImageProcessor ip;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(), ImageIO.read(tc.getImageWrapper().getFile()));
            ip = imp.getProcessor();
            ip = ip.resize((int) parameters.get("width"), (int) parameters.get("height"));
            imp.setProcessor(ip);
            tc.getImageWrapper().set(imp.getBufferedImage());
        } catch (IOException ignored) {
        }
    }


}
