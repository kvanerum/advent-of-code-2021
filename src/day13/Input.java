package day13;

import java.util.ArrayList;
import java.util.List;

public class Input {
    public final List<Coordinate> coordinates = new ArrayList<>();
    public final List<Fold> foldList = new ArrayList<>();

    @Override
    public String toString() {
        return "Input{" +
               "coordinates=" + coordinates +
               ", foldList=" + foldList +
               '}';
    }
}
