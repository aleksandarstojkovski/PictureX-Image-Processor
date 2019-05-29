package ch.picturex.filters;

import ch.picturex.model.ThumbnailContainer;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unused")

public class Rotate implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(), ImageIO.read(tc.getImageWrapper().getFile()));
            if (parameters.get("direction").equals("left")) {
                imp.setProcessor(imp.getProcessor().rotateLeft());
            } else {
                imp.setProcessor(imp.getProcessor().rotateRight());
            }
            imp.updateAndDraw();
            tc.getImageWrapper().set(imp.getBufferedImage());
        } catch (IOException ignored) {
        }
    }

}
