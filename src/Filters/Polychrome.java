package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Polychrome implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        String response = JOptionPane.showInputDialog("choose an n value");
        int n = Integer.parseInt( response );
        double intervalLen = 255.0/n;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                int pixelVal = grid[row][col];
                int interval = (int) (pixelVal/intervalLen);
                grid[row][col] = (short) (interval * intervalLen + (intervalLen/2));
            }
        }

        img.setPixels(grid);
        return img;
    }
}

