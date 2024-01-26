package Filters;

public class Point {
    private short r, g, b;
    public Point(short r, short g, short b){
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public double getDistance (Point p){
        short red = p.getR();
        short green = p.getG();
        short blue = p.getB();
        return Math.sqrt(Math.pow((red - r), 2) + Math.pow((green - g), 2) + Math.pow(blue - b, 2));
    }

    public short getR() {
        return r;
    }

    public short getG() {
        return g;
    }

    public short getB() {
        return b;
    }

    public boolean equals(Object o) {
        Point other = (Point)o;
        return other.getR() == getR() && other.getB() == getB() && other.getG() == getG();
    }
}
