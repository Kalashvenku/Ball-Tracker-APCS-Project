package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class BallTracker implements PixelFilter {
    ColorMask detectColors = new ColorMask();
    Convolution blur = new Convolution();
    FixedThresholdFilter threshold = new FixedThresholdFilter(200);
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
        centers.add(findCenter(grid, x, y));
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
    public Coordinate findCenter(short[][] grid, int x, int y){
        int regionRadius = 4;
        boolean exit = false;
        boolean blackFound = false;
        int averageX = 0;
        int averageY = 0;
        int whitePixels = 0;
        boolean done = false;
        while(!done) {
            for (int r = x - regionRadius; r < x + regionRadius; r++) {
                for (int c = x - regionRadius; c < x + regionRadius; c++) {
                    if (grid[r][c] == 255) {
                        averageX += r;
                        averageY += c;
                        whitePixels++;
                    }
                    if (grid[r][c] == 0) {
                        regionRadius++;
                        blackFound = true;
                    }
                }
            }
            if (blackFound) {
                averageX /= whitePixels;
                averageY /= whitePixels;
            }
            if (averageX == x && averageY == y){
                done = true;
            }
            x = averageX;
            y = averageY;
        }
        return new Coordinate(x, y);
    }
}
