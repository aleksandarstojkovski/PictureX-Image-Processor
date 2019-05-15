package filters;


import ch.supsi.IFilter;
import ch.supsi.ThumbnailContainer;
import ij.ImagePlus;
import ij.process.ImageConverter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlackAndWhite implements IFilter {

    @Override
    public void apply(ThumbnailContainer tc, Map<String, Object> parameters) {
        ImagePlus imp = null;
        try {
            imp = new ImagePlus(tc.getImageWrapper().getFile().getName(),  ImageIO.read(tc.getImageWrapper().getFile()));
            ImageConverter ic = new ImageConverter(imp);
            ic.convertToGray8();
            imp.updateAndDraw();
            ImageIO.write(imp.getBufferedImage(), tc.getImageWrapper().getExtension(), tc.getImageWrapper().getFile());
            tc.getImageWrapper().set(tc.getImageWrapper().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
