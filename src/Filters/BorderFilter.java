package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class BorderFilter implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        int borderWidth = Integer.parseInt(JOptionPane.showInputDialog("Enter a border width:"));
        short[][] grid = img.getBWPixelGrid();
        short[][] borderImg = createArray(grid.length + borderWidth * 2, grid[0].length + borderWidth * 2, (short) 40);
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                borderImg[borderWidth + row][borderWidth + col] = grid[row][col];
            }
        }
        img.setPixels(borderImg);
        return img;
    }

    private short[][] createArray(int height, int width, short pixelVal) {
        short[][] grid = new short[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = pixelVal;
            }
        }
        return grid;
    }
}

