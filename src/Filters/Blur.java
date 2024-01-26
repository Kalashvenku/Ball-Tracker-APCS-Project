package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;

public class Blur implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();
        String response = JOptionPane.showInputDialog("choose an n value");
        int blur = Integer.parseInt(response);
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length - blur; c++) {
                int newVal = 0;
                for (int i = 0; i < blur; i++) {
                    newVal += grid[r][c+i];
                }
                newVal = newVal/blur;
                grid[r][c] = (short) newVal;
            }
            for (int i = 4; i > 0; i--){
                int sum = 0;
                for (int j = i; j > 0; j--){
                    sum += grid[r][grid[0].length - j];
                }
                grid[r][grid[0].length - i] = (short) (sum/i);
            }
        }
        img.setPixels(grid);
        return img;
    }
}
