package Filters;

import java.util.ArrayList;

public class Cluster {
    private Point clusterCenter;
    private ArrayList<Point> clusterPoints = new ArrayList<>();

    public Cluster(short cr, short cg, short cb) {
        clusterCenter = new Point(cr, cg, cb);
    }

    public void calculateCenter() {
        int totalRed = 0;
        int totalGreen = 0;
        int totalBlue = 0;
        for (Point point : clusterPoints) {
            totalRed += point.getR();
            totalGreen += point.getG();
            totalBlue += point.getB();
        }
        if (clusterPoints.size() == 0){return;}
        clusterCenter = new Point((short) (totalRed / clusterPoints.size()), (short) (totalGreen / clusterPoints.size()), (short) (totalBlue / clusterPoints.size()));
    }

    public ArrayList<Point> getClusterPoints() {return clusterPoints;}

    public void addPoint(Point point) {
        clusterPoints.add(point);
    }

    public void clearPoints() {
        clusterPoints.clear();
    }

    public Point getCenter() {
        return clusterCenter;
    }

}
