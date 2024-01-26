package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.ArrayList;

public class DownSizeFilter implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        short[][] smallImg = new short[grid.length / 2][grid[0].length / 2];
        for (int row = 0; row < grid.length - 1; row += 2) {
            for (int col = 0; col < grid[0].length - 1; col += 2) {
                short newPixelVal = (short) ((grid[row][col] + grid[row + 1][col + 1] + grid[row][col + 1] + grid[row + 1][col]) / 4);
                smallImg[row / 2][col / 2] = newPixelVal;
            }
        }
        img.setPixels(smallImg);
        return img;
    }
}

