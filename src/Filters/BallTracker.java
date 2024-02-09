package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTracker implements PixelFilter {
    ColorMask detectColors = new ColorMask();
    Convolution blur = new Convolution();
    @Override
    public DImage processImage(DImage img) {
        DImage maskedImg = getMaskedImg(img);
        maskedImg = blur.processImage(maskedImg);
        //threshold values below 200 to be black and above 200 to be white
        short[][] grid = img.getBWPixelGrid();
        short[][] red = img.getRedChannel();
        int getRowLocs = 0, getColLocs = 0;

        int counter = 0;
        int centerRow = 0;
        int centerCol = 0;
        for (int row = 0; row < red.length; row++) {
            for (int col = 0; col < red[0].length; col++) {
                if (grid[row][col] == 0){
                    getRowLocs+=row;
                    getColLocs+=col;
                    counter++;
                }
            }
        }
        centerRow = getRowLocs/(counter/2);
        centerCol = getColLocs/(counter/2);
        System.out.println(centerRow + "  " + centerCol);
        img.setPixels(grid);
        return img;
    }

    private DImage getMaskedImg(DImage img) {
        return detectColors.processImage(img);
    }
}
