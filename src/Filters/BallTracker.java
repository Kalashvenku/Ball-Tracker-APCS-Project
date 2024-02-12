package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTracker implements PixelFilter {
    ColorMask detectColors = new ColorMask();
    Convolution blur = new Convolution();
    FixedThresholdFilter threshold = new FixedThresholdFilter();
    DImage originalImage;

    @Override
    public DImage processImage(DImage img) {
//        originalImage = new DImage(img);
//        detectColors.processImage(img);
//        blur.processImage(img);
//        threshold.processImage(img); creating and cleaning color mask ADD BACK IN LATER
        short[][] grid = img.getBWPixelGrid();

        //find random white pixel
        int x = (int) (Math.random() * grid.length);
        int y = (int) (Math.random() * grid[0].length);
        while (grid[x][y] != 255) {
            x = (int) (Math.random() * grid.length);
            y = (int) (Math.random() * grid[0].length);
        }
        //with random white pixel loop over a rectangular region around your pixel if no black pixel enlarge region
        //if black pixel find average coords of white pixels


//        int getRowLocs = 0, getColLocs = 0;
//        int counter = 0;
//        int centerRow = 0;
//        int centerCol = 0;
//        for (int row = 0; row < red.length; row++) {
//            for (int col = 0; col < red[0].length; col++) {
//                if (grid[row][col] == 0){
//                    getRowLocs+=row;
//                    getColLocs+=col;
//                    counter++;
//                }
//            }
//        }
//        centerRow = getRowLocs/(counter/2);
//        centerCol = getColLocs/(counter/2);
//        System.out.println(centerRow + "  " + centerCol);
        img.setPixels(grid);
        return img;
    }

}
