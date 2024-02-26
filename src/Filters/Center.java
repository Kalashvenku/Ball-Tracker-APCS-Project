package Filters;

public class Center {
    private double radius;
    private Coordinate coord;

    public Center(int x, int y, double r) {
        this.coord = new Coordinate(x, y);
        this.radius = r;
    }

    public double getRadius() {
        return radius;
    }

    public Coordinate getCoordinates() {
        return coord;
    }

    public boolean overlaps(Center center) {
        Coordinate location = getCoordinates();
        Coordinate otherCenterLocation = center.getCoordinates();
        int xDist = location.x - otherCenterLocation.x;
        int yDist = location.y - otherCenterLocation.y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist);
        return dist < (center.getRadius() + getRadius());
    }
}
