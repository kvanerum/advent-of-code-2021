package day13;

public class Fold {
    public final char direction;
    public final int position;

    public Fold(char direction, int position) {
        this.direction = direction;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Fold{" +
               "direction=" + direction +
               ", position=" + position +
               '}';
    }
}
