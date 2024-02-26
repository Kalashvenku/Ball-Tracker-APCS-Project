package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.util.ArrayList;

public class BallTracker implements PixelFilter {
    ColorMask detectColors = new ColorMask();
    Convolution blur = new Convolution();
    FixedThresholdFilter threshold = new FixedThresholdFilter(200);
    DImage originalImage;

    @Override
    public DImage processImage(DImage img) {
        originalImage = new DImage(img);
//        detectColors.processImage(img);
//        blur.processImage(img);
        threshold.processImage(img);
        short[][] grid = img.getBWPixelGrid();
        //short[][] newGrid = new short[grid.length][grid[0].length];
        ArrayList<Coordinate> allWhitePixels = getAllWhite(grid);
        ArrayList<Center> centers = findCenters(grid, allWhitePixels);
        /*
        List<Coordinate> allWhite = getAllWhite(grid);

        Location randWhitePixel = findRandomWhitePixel(grid);
         */

        //find random white pixel


        for (Center center : centers) {
            for (int r = center.getCoordinates().x - 5; r < center.getCoordinates().x + 5; r++) {
                for (int c = center.getCoordinates().y - 5; c < center.getCoordinates().y; c++) {
                    grid[r][c] = 255;
                }
            }
            drawOutline(center, grid);
        }
        //with random white pixel loop over a rectangular region around your pixel if no black pixel enlarge region
        //if black pixel find average cords of white pixels


        img.setPixels(grid);
        return img;
    }

    private void drawOutline(Center center, short[][] grid) {
        int r = center.getCoordinates().x;
        int c = center.getCoordinates().y;
        for (int row = (int) (r - center.getRadius() - 4); row <= r + center.getRadius() + 4; row++) {
            for (int col = (int) (c - center.getRadius() - 4); col <= c + center.getRadius() + 4; col++) {
                if (nPixelsAway(row, col, r, c, center.getRadius())) {
                    grid[row][col] = 255;
                }
            }
        }
    }

    private boolean nPixelsAway(int x1, int y1, int x2, int y2, double n) {
        double xDistSquared = Math.pow(x1 - x2, 2);
        double yDistSquared = Math.pow(y1 - y2, 2);
        int distance = (int) Math.sqrt(xDistSquared + yDistSquared);
        return Math.abs(distance - (int) n) <= 4;
    }

    private ArrayList<Center> findCenters(short[][] grid, ArrayList<Coordinate> allWhitePixels) {
        ArrayList<Center> centers = new ArrayList<>();
        // while we still have white
        while (allWhitePixels.size() > 0) {
            //get random white pixel
            Coordinate whitePixel;
            whitePixel = allWhitePixels.get((int) (Math.random() * allWhitePixels.size()));
            //use findcenter() method
            Center newCenter = findCenter(grid, whitePixel.x, whitePixel.y, allWhitePixels);
            if (newCenter == null) {
                continue;
            }
            centers.add(newCenter);
        }
//        condenseCenters(centers);
        return centers;
    }

    private void condenseCenters(ArrayList<Center> centers) {
        for (int count = 0; count < 4; count++) {
            ArrayList<Center> newCentersList = centers;
            for (int i = 0; i < centers.size(); i++) {
                for (int j = i; j < centers.size(); j++) {
                    Center center1 = centers.get(i);
                    Center center2 = centers.get(j);
                    if (center1.overlaps(center2)) {
                        int newX = (center1.getCoordinates().x + center2.getCoordinates().y) / 2;
                        int newY = (center1.getCoordinates().y + center2.getCoordinates().y) / 2;
                        Center newCenter = new Center(newX, newY, Math.max(center1.getRadius(), center2.getRadius()));
                        newCentersList.add(newCenter);
                        newCentersList.remove(center1);
                        newCentersList.remove(center2);
                    }
                }
            }
            centers = newCentersList;
        }
    }

    private ArrayList<Coordinate> getAllWhite(short[][] grid) {
        ArrayList<Coordinate> allWhite = new ArrayList<>();
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 255) {
                    allWhite.add(new Coordinate(r, c));
                }
            }
        }
        return allWhite;
    }

    public Center findCenter(short[][] grid, int x, int y, ArrayList<Coordinate> allWhitePixels) {
        int regionRadius = 4;
        boolean blackFound;
        int averageX;
        int averageY;
        int whitePixels;
        int failedCount = 0;
        int sameCenterCount = 0;
        boolean foundRealCenter;
        ArrayList<Coordinate> deletedCoords = new ArrayList<>();
        while (true) {
            averageX = 0;
            averageY = 0;
            whitePixels = 0;
            blackFound = false;
            for (int r = x - regionRadius; r <= x + regionRadius; r++) {
                if (r < 0 || r >= grid.length) {
                    continue;
                }
                for (int c = y - regionRadius; c <= y + regionRadius; c++) {
                    if (c < 0 || c >= grid[0].length) {
                        continue;
                    }
                    if (grid[r][c] == 255) {
                        averageX += r;
                        averageY += c;
                        whitePixels++;
                    } else {
                        blackFound = true;
                    }
                }
            }
            if (blackFound) {
                if (whitePixels == 0) {
                    whitePixels = 1;
                }
                averageX /= whitePixels;
                averageY /= whitePixels;
//                int changeX = averageX - x;
//                int changeY = averageY - y;
                if (averageX == x && averageY == y) {
                    if (regionRadius > 30) {
                        sameCenterCount++;
                    } else {
                        failedCount++;
                    }
                }
                if (failedCount >= 6) {
                    foundRealCenter = false;
                    break;
                }
                if (sameCenterCount >= 3) {
                    foundRealCenter = true;
                    break;
                }
                x = averageX;
                y = averageY;
            } else {
                regionRadius += 10;
            }
        }
        for (Coordinate whitePixel : allWhitePixels) {
            if (withinNPixels(whitePixel.x, whitePixel.y, x, y, regionRadius * Math.sqrt(2))) {
                deletedCoords.add(whitePixel);
                grid[whitePixel.x][whitePixel.y] = 0;
            }
        }

        for (Coordinate coord : deletedCoords) {
            allWhitePixels.remove(coord);
        }
        if (foundRealCenter) {
            return new Center(x, y, regionRadius * Math.sqrt(2));
        } else {
            return null;
        }
    }

    private boolean withinNPixels(int x1, int y1, int x2, int y2, double n) {
        double xDistSquared = Math.pow(x1 - x2, 2);
        double yDistSquared = Math.pow(y1 - y2, 2);
        int distance = (int) Math.sqrt(xDistSquared + yDistSquared);
        return distance <= n;
    }

    private int clamp(int num, int lower, int higher) {
        if (num < lower) {
            return lower;
        }
        if (num > higher) {
            return higher;
        }
        return num;
    }
}


