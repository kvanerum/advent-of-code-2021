package day19;

import java.util.Objects;

public class Coordinate {
    private final int x;
    private final int y;
    private final int z;
    private final int hash;

    Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.hash = Objects.hash(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate beacon = (Coordinate) o;
        return x == beacon.x && y == beacon.y && z == beacon.z;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }
}
