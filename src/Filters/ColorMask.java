package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        short[] targetRGB = new short[3];
        targetRGB[0] = 30;
        targetRGB[1] = 255;
        targetRGB[2] = 30;
        double distanceThreshold = 175;
        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[0].length; c++) {
                int redDistance = Math.abs(targetRGB[0] - red[r][c]);
                int greenDistance = Math.abs(targetRGB[1] - green[r][c]);
                int blueDistance = Math.abs(targetRGB[2] - blue[r][c]);
                double distance = Math.sqrt(Math.pow(redDistance, 2) + Math.pow(greenDistance, 2) + Math.pow(blueDistance, 2));
                if (distance > distanceThreshold){
                    red[r][c] = 0;
                    green[r][c] = 0;
                    blue[r][c] = 0;
                }
                else{
                    red[r][c] = 255;
                    green[r][c] = 255;
                    blue[r][c] = 255;
                }
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }
}
