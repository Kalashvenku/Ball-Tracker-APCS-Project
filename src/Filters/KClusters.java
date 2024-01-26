package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import javax.swing.*;
import java.util.ArrayList;

public class KClusters implements PixelFilter {
    @Override
    public DImage processImage(DImage img) {
        String response = JOptionPane.showInputDialog("Choose a number of clusters");
        int numClusters = Integer.parseInt(response);
        ArrayList<Cluster> clusters = initClusters(numClusters);
        short[][] red = img.getRedChannel();
        short[][] green = img.getGreenChannel();
        short[][] blue = img.getBlueChannel();
        ArrayList<Point> pointList = makePointList(red, green, blue);
        for (int i = 0; i < 10; i++) {
            clearClusters(clusters);
            assignPointsToClusters(pointList, clusters);
            recenter(clusters);
        }
        for (int r = 0; r < red.length; r++){
            for (int c = 0; c < red[0].length; c++) {
                Point point = new Point(red[r][c], green[r][c], blue[r][c]);
                Cluster cluster = findNearestCluster(clusters, point);
                red[r][c] = cluster.getCenter().getR();
                green[r][c] = cluster.getCenter().getB();
                blue[r][c] = cluster.getCenter().getR();
            }
        }
        img.setColorChannels(red, green, blue);
        return img;
    }

    private boolean pointInCluster(Cluster cluster, Point point) {
        ArrayList<Point> clusterPoints = cluster.getClusterPoints();
        return clusterPoints.contains(point);
    }

    private void recenter(ArrayList<Cluster> clusters) {
        for (Cluster cluster : clusters){
            cluster.calculateCenter();
        }
    }

    private void clearClusters(ArrayList<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            cluster.clearPoints();
        }
    }

    public void assignPointsToClusters(ArrayList<Point> pointList, ArrayList<Cluster> clusterList) {
        for (Point point : pointList){
            Cluster nearest = findNearestCluster(clusterList, point);
            nearest.addPoint(point);
        }
    }
    public Cluster findNearestCluster(ArrayList<Cluster> clusterList, Point point){
        Cluster closestCluster = clusterList.get(0);
        for (Cluster cluster : clusterList){
            Point clusterCenter = cluster.getCenter();
            double distance = point.getDistance(clusterCenter);
            if (distance < point.getDistance(closestCluster.getCenter())){
                closestCluster = cluster;
            }
        }
        return closestCluster;
    }

    public ArrayList<Point> makePointList(short[][] red, short[][] green, short[][] blue) {
        ArrayList<Point> newPointList = new ArrayList<>();
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[0].length; j++) {
                newPointList.add(new Point(red[i][j], green[i][j], blue[i][j]));
            }
        }
        return newPointList;
    }

    public ArrayList<Cluster> initClusters(int numClusters) {
        ArrayList<Cluster> clusters = new ArrayList<>();
        for (int i = 0; i < numClusters; i++) {
            clusters.add(new Cluster((short) (Math.random() * 256), (short) (Math.random() * 256), (short) (Math.random() * 256)));
        }
        return clusters;
    }
}
