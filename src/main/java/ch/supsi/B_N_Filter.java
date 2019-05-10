package ch.supsi;

import ij.ImagePlus;
import ij.process.ImageConverter;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class B_N_Filter implements Filter{
    @Override
    public File modify(File file, HashMap<String, Object> parameters) {
        ImagePlus imp = new ImagePlus(file.toURI().toString());
        ImageConverter ic = new ImageConverter(imp);
        ic.convertToGray8();
        imp.updateAndDraw();
        try {
            ImageIO.write(imp.getBufferedImage(), (String)parameters.get("extention"), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
