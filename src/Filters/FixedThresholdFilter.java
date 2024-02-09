package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.*;

public class FixedThresholdFilter implements PixelFilter, Interactive {
    private int threshold;

    public FixedThresholdFilter() {
        threshold = 127;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = 255;
                } else {
                    grid[r][c] = 0;
                }
            }
        }

        img.setPixels(grid);
        return img;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            threshold += 5;
        }else if (key == '-'){
            threshold -= 5;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        short targetR = red[mouseX][mouseY];
        short targetG = green[mouseX][mouseY];
        short targetB = blue[mouseX][mouseY];
        System.out.println( targetR+ " " + targetG + " " + targetB);
    }
}


