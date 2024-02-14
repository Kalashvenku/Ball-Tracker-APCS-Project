package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.sql.Array;
import java.util.ArrayList;

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
        ArrayList<Coordinate> centers = new ArrayList<>();
        //find random white pixel
        int x = (int) (Math.random() * grid.length);
        int y = (int) (Math.random() * grid[0].length);
        while (grid[x][y] != 255) {
            x = (int) (Math.random() * grid.length);
            y = (int) (Math.random() * grid[0].length);
        }
        centers.add(findCenter(grid, x, y));
        //with random white pixel loop over a rectangular region around your pixel if no black pixel enlarge region
        //if black pixel find average cords of white pixels



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


