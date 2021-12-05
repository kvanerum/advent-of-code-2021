package day05;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public final Coordinate from;
    public final Coordinate to;

    Line(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public boolean isDiagonal() {
        return from.x != to.x && from.y != to.y;
    }

    public List<Coordinate> getPoints() {
        int deltaX = Integer.compare(to.x, from.x);
        int deltaY = Integer.compare(to.y, from.y);

        int currentX = from.x;
        int currentY = from.y;
        List<Coordinate> result = new ArrayList<>();

        while (currentX != to.x || currentY != to.y) {
            result.add(new Coordinate(currentX, currentY));
            currentX += deltaX;
            currentY += deltaY;
        }

        result.add(new Coordinate(currentX, currentY));

        return result;
    }
}
