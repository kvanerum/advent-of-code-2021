package day05;

public class Coordinate {
    public final int x;
    public final int y;

    Coordinate(String input) {
        String[] split = input.split(",");
        this.x = Integer.parseInt(split[0]);
        this.y = Integer.parseInt(split[1]);
    }

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
