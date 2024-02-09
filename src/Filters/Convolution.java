package Filters;

import Interfaces.PixelFilter;
import core.DImage;

public class Convolution implements PixelFilter {
    private double[][] boxBlurKernel = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private double[][] PrewittEdgeKernel = {{-1, -1, -1}, {-1, 8, -1}, {-1, -1, -1}};
    private double[][] GaussianBlur = {{0, 0, 0, 5, 0, 0, 0}, {0, 5, 18, 32, 18, 5, 0}, {0, 18, 64, 100, 64, 18, 0}, {5, 32, 100, 100, 100, 32, 5}, {0, 0, 0, 5, 0, 0, 0}, {0, 5, 18, 32, 18, 5, 0}, {0, 18, 64, 100, 64, 18, 0}};
    private double[][] Gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    private double[][] Gy = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};
    /*  you can define others here */

    public Convolution() {
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();

//        red = doSobel(red, Gx, Gy);
//        green = doSobel(green, Gx, Gy);
//        blue = doSobel(blue, Gx, Gy);
        red = doConvolution(red, GaussianBlur);
        green = doConvolution(green,GaussianBlur);
        blue = doConvolution(blue, GaussianBlur);
        img.setColorChannels(red, green, blue);
        return img;
    }

    private short[][] doSobel(short[][] grid, double[][] Gx, double[][] Gy) {
        int threshold = 127;
        short newGrid[][] = new short[grid.length][grid[0].length];
        short gridX[][];
        short gridY[][];
        gridX = doConvolution(grid, Gx);
        gridY = doConvolution(grid, Gy);

        for (int i = 0; i < newGrid.length; i++) {
            for (int j = 0; j < newGrid[0].length; j++) {
                short x = gridX[i][j];
                short y = gridY[i][j];
                short newVal = (short) Math.sqrt(x * x + y * y);
                if (newVal > threshold){newVal = 255;}
                else{newVal = 0;}
                newGrid[i][j] = newVal;
            }
        }
        return newGrid;
    }

    private short[][] doConvolution(short[][] grid, double[][] kernel) {
        short[][] newGrid = new short[grid.length][grid[0].length];
        for (int row = kernel.length / 2; row < grid.length - kernel[0].length / 2; row++) {
            for (int col = kernel[0].length/2; col < grid[0].length - kernel[0].length/2; col++) {
                int newVal = computeOutput(grid, kernel, row, col);
                newGrid[row][col] = (short) newVal;
            }
        }

        return newGrid;
    }

    private int computeOutput(short[][] grid, double[][] kernel, int startR, int startC) {
        int output = 0;
        int kernelWeight = 0;
        for (int row = 0; row < kernel.length; row++) {
            for (int col = 0; col < kernel[0].length; col++) {
                int weight = (int) kernel[row][col];
                kernelWeight += weight;
                int pixelVal = grid[row + startR - kernel.length / 2][col + startC - kernel[0].length / 2];
                output += pixelVal * weight;
            }
        }
        if (kernelWeight != 0) {
            output /= kernelWeight;
        }
        if (output > 255) output = 255;
        if (output < 0) output = 0;
        return output;
    }
}

