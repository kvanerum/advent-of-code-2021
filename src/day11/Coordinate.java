package day11;

import java.util.Objects;

public class Coordinate {
    final public int x;
    final public int y;

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isValid(int sizeX, int sizeY) {
        return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
    }

    public boolean isValid(int size) {
        return isValid(size, size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
               "x=" + x +
               ", y=" + y +
               '}';
    }
}
