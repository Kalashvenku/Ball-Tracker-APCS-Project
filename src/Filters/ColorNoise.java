package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class ColorNoise implements PixelFilter {
    private double n;
    public ColorNoise(){
        String r = JOptionPane.showInputDialog("enter a noise probability");
        n = Double.parseDouble(r);
    }
    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[0].length; c++) {
                if (Math.random() < n){
                    red[r][c] = (short) (Math.random() * 256);
                    green[r][c] = (short) (Math.random() * 256);
                    blue[r][c] = (short) (Math.random() * 256);
                }
            }
        }

        img.setColorChannels(green, red, blue);
        return img;
    }
}
