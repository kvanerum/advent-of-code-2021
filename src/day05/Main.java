package day05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Line> input = readInput();

        calculateLineCollisions(input, false);
        calculateLineCollisions(input, true);
    }

    private static void calculateLineCollisions(List<Line> input, boolean includeDiagonalLines) {
        Map<Integer, Map<Integer, Integer>> pointMap = new HashMap<>();

        // create map
        input.stream()
                .filter(line -> includeDiagonalLines || !line.isDiagonal()) // only use horizontal or vertical lines
                .map(Line::getPoints) // get all the points for each line
                .flatMap(List::stream) // map to single stream of coordinates
                .forEach(point -> {
                    // increment value for each point in the map
                    Map<Integer, Integer> column = pointMap.computeIfAbsent(point.x, k -> new HashMap<>());
                    column.compute(point.y, (k, v) -> (v == null ? 0 : v) + 1);
                });

        // calculate number of point with 2 or more lines
        long count = pointMap.values().stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(n -> n > 1)
                .count();
        System.out.println(count);
    }

    private static List<Line> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day05/input.txt")).collect(Collectors.toList());

        return lines.stream().map(line -> {
            String[] split = line.split(" -> ");
            return new Line(new Coordinate(split[0]), new Coordinate(split[1]));
        }).collect(Collectors.toList());
    }
}