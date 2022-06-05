package day19;

public class Distance {
    public static double calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.sqrt(Math.pow(c1.getX() - c2.getX(), 2) + Math.pow(c1.getY() - c2.getY(), 2) + Math.pow(c1.getZ() - c2.getZ(), 2));
    }

    public static long manhattanDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY()) + Math.abs(c1.getZ() - c2.getZ());
    }
}
