package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class SwapRedGreen implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();


        img.setColorChannels(green, red, blue);
        return img;
    }
}
