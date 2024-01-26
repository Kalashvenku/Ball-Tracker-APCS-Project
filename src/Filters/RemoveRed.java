package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class RemoveRed implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        red = new short[img.getHeight()][img.getWidth()];


        img.setColorChannels(red, green, blue);
        return img;
    }
}
