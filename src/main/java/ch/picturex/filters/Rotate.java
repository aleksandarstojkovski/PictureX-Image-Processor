package ch.picturex.filters;

import ch.picturex.IFilter;
import ch.picturex.ThumbnailContainer;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Rotate implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp = null;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(), ImageIO.read(tc.getImageWrapper().getFile()));
            if (parameters.get("direction").equals("left")) {
                imp.setProcessor(imp.getProcessor().rotateLeft());
            }
            else {
                imp.setProcessor(imp.getProcessor().rotateRight());
            }
            imp.updateAndDraw();
            tc.getImageWrapper().set(imp.getBufferedImage());
        } catch (IOException e){}
    }

}
