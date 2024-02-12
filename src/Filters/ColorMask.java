package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

public class ColorMask implements PixelFilter, Interactive {
    private int threshold;
    short[][] targetColors;
    Convolution blur = new Convolution();

    public ColorMask(){
        threshold = 30;
        targetColors = new short[3][3];
        targetColors[0][0] = 61;
        targetColors[0][1] = 121;
        targetColors[0][2] = 54;
        targetColors[1][0] = 20;
        targetColors[1][1] = 43;
        targetColors[1][2] = 139;
        targetColors[2][0] = 213;
        targetColors[2][1] = 175;
        targetColors[2][2] = 47;
    }


    @Override
    public DImage processImage(DImage img) {
        System.out.println(threshold);
        img = blur.processImage(img);
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        short[][] newRed = new short[red.length][red[0].length];
        short[][] newGreen = new short[red.length][red[0].length];
        short[][] newBlue = new short[blue.length][blue[0].length];

        for (int r = 0; r < red.length; r++) {
            for (int c = 0; c < red[0].length; c++) {
                for (int color = 0; color < targetColors.length; color++) {
                    int redDistance = Math.abs(targetColors[color][0] - red[r][c]);
                    int greenDistance = Math.abs(targetColors[color][1] - green[r][c]);
                    int blueDistance = Math.abs(targetColors[color][2] - blue[r][c]);
                    double distance = Math.sqrt(Math.pow(redDistance, 2) + Math.pow(greenDistance, 2) + Math.pow(blueDistance, 2));
                    if (distance < threshold){
                        newRed[r][c] = (short) (255);
                        newGreen[r][c] = (short) (255);
                        newBlue[r][c] = (short )(255);
                    }
                }
            }
        }
        img.setColorChannels(newRed, newGreen, newBlue);
        return img;
    }


    public void gerCenter(){}

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        short newR = red[mouseY][mouseX];
        short newG = green[mouseY][mouseX];
        short newB = blue[mouseY][mouseX];
        targetColors[0][0] = newR;
        targetColors[0][1] = newG;
        targetColors[0][2] = newB;
        System.out.println(newR + " " + newG + " " + newB);

    }

    @Override
    public void keyPressed(char key) {
        if (key == '+') {
            threshold += 5;
        }else if (key == '-'){
            threshold -= 5;
        }
    }

}
