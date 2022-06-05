package day19;

import java.util.Set;

public class Transformations {
    interface Transformation {
        Coordinate transform(Coordinate b, int dX, int dY, int dZ);
    }

    public static final Set<Transformation> Transformations = Set.of(
            // x,y,z
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY + b.getY(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY + b.getY(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY - b.getY(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY + b.getY(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY - b.getY(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY - b.getY(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY + b.getY(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY - b.getY(), dZ - b.getZ()),

            // x,z,y
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY + b.getZ(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY + b.getZ(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY - b.getZ(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY + b.getZ(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY - b.getZ(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getX(), dY - b.getZ(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY + b.getZ(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getX(), dY - b.getZ(), dZ - b.getY()),

            // y,x,z
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY + b.getX(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY + b.getX(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY - b.getX(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY + b.getX(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY - b.getX(), dZ + b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY - b.getX(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY + b.getX(), dZ - b.getZ()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY - b.getX(), dZ - b.getZ()),

            // y,z,x
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY + b.getZ(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY + b.getZ(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY - b.getZ(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY + b.getZ(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY - b.getZ(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getY(), dY - b.getZ(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY + b.getZ(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getY(), dY - b.getZ(), dZ - b.getX()),

            // z,x,y
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY + b.getX(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY + b.getX(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY - b.getX(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY + b.getX(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY - b.getX(), dZ + b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY - b.getX(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY + b.getX(), dZ - b.getY()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY - b.getX(), dZ - b.getY()),

            // z,y,x
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY + b.getY(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY + b.getY(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY - b.getY(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY + b.getY(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY - b.getY(), dZ + b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX + b.getZ(), dY - b.getY(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY + b.getY(), dZ - b.getX()),
            (b, dX, dY, dZ) -> new Coordinate(dX - b.getZ(), dY - b.getY(), dZ - b.getX())
    );
}
