package filters;

import ch.supsi.IFilter;
import ch.supsi.ThumbnailContainer;
import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Rotate implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp = null;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(), ImageIO.read(tc.getImageWrapper().getFile()));
            if ( parameters.get("direction").equals("left") ) {
                imp.setProcessor(imp.getProcessor().rotateLeft());
            }
            else {
                imp.setProcessor(imp.getProcessor().rotateRight());
            }
            imp.updateAndDraw();
            imp.updateAndDraw();
            ImageIO.write(imp.getBufferedImage(), tc.getImageWrapper().getExtension(), tc.getImageWrapper().getFile());
            tc.getImageWrapper().set(tc.getImageWrapper().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
