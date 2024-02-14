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
        originalImage = new DImage(img);

        short[][] grid = img.getBWPixelGrid();
        ArrayList<Coordinate> allWhitePixels = getAllWhite(grid);
        ArrayList<Coordinate> centers = findCenters(grid, allWhitePixels);

        /*
        List<Coordinate> allWhite = getAllWhite(grid);

        Location randWhitePixel = findRandomWhitePixel(grid);
         */

        //find random white pixel


        for (Coordinate center : centers) {
            for (int r = center.x - 5; r < center.x + 5; r++) {
                for (int c = center.y - 5; c < center.y; c++) {
                    grid[r][c] = 255;
                }
            }
        }
        //with random white pixel loop over a rectangular region around your pixel if no black pixel enlarge region
        //if black pixel find average cords of white pixels


        img.setPixels(grid);
        return img;
    }

    private ArrayList<Coordinate> findCenters(short[][] grid, ArrayList<Coordinate> allWhitePixels) {
        //get random white pixel
        ArrayList<Coordinate> centers = new ArrayList<>();
        Coordinate whitePixel;
        whitePixel = allWhitePixels.get((int) (Math.random() * allWhitePixels.size()));
        //use findcenter() method
        centers.add(findCenter(grid, whitePixel.x, whitePixel.y, allWhitePixels));
        return centers;
    }

    private ArrayList<Coordinate> getAllWhite(short[][] grid) {
        ArrayList<Coordinate> allWhite = new ArrayList<>();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++){
                if (grid[r][c] == 255){
                    allWhite.add(new Coordinate(r, c));
                }
            }
        }
        return allWhite;
    }

    public Coordinate findCenter(short[][] grid, int x, int y, ArrayList<Coordinate> allWhitePixels) {
        int regionRadius = 4;
        boolean exit = false;
        boolean blackFound = false;
        int averageX = 0;
        int averageY = 0;
        int whitePixels = 0;
        boolean done = false;
        ArrayList<Coordinate> deletedCoords = new ArrayList<>();
        while (!done) {
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
            if (averageX == x && averageY == y) {
                done = true;
            }
            x = averageX;
            y = averageY;
        }
        for (int r = x - regionRadius; r < x + regionRadius; r++) {
            for (int c = x - regionRadius; c < x + regionRadius; c++) {
                grid[r][c] = 0;
                for (int i = 0; i < allWhitePixels.size(); i++){
                    if (allWhitePixels.get(i).x == r && allWhitePixels.get(i).y == c){
                        deletedCoords.add(allWhitePixels.get(i));
                    }
                }
            }
        }
        for (Coordinate coord : deletedCoords){
            allWhitePixels.remove(coord);
        }
        return new Coordinate(x, y);
    }
}


