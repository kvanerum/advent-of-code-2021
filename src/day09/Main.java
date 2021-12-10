package day09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<List<Integer>> input = readInput();

        List<Coordinate> lowPoints = findLowPoints(input);

        int totalRiskLevel = lowPoints.stream()
                .map(c -> input.get(c.y).get(c.x) + 1)
                .reduce(Integer::sum)
                .orElseThrow();
        System.out.println(totalRiskLevel);

        List<Integer> basinSizes = lowPoints.stream()
                .map(lowPoint -> findBasinSize(lowPoint, input, new HashSet<>(Collections.singleton(lowPoint))))
                .collect(Collectors.toList());

        int result = basinSizes.stream().sorted(Comparator.reverseOrder()).limit(3).reduce(1, (a, b) -> a * b);
        System.out.println(result);
    }

    private static Integer findBasinSize(Coordinate currentCoordinate, List<List<Integer>> input, Set<Coordinate> currentBasin) {
        int currentValue = input.get(currentCoordinate.y).get(currentCoordinate.x);

        Set<Coordinate> nextCoordinates = Set.of(
                new Coordinate(currentCoordinate.x, currentCoordinate.y - 1), // up
                new Coordinate(currentCoordinate.x, currentCoordinate.y + 1), // down
                new Coordinate(currentCoordinate.x - 1, currentCoordinate.y), // left
                new Coordinate(currentCoordinate.x + 1, currentCoordinate.y) // right
        );

        for (Coordinate nextCoordinate : nextCoordinates) {
            if (nextCoordinate.y >= 0 &&
                nextCoordinate.y < input.size() &&
                nextCoordinate.x >= 0 &&
                nextCoordinate.x < input.get(nextCoordinate.y).size() &&
                !currentBasin.contains(nextCoordinate) &&
                input.get(nextCoordinate.y).get(nextCoordinate.x) > currentValue &&
                input.get(nextCoordinate.y).get(nextCoordinate.x) < 9
            ) {
                currentBasin.add(nextCoordinate);
                findBasinSize(nextCoordinate, input, currentBasin);
            }
        }

        return currentBasin.size();
    }

    private static List<Coordinate> findLowPoints(List<List<Integer>> input) {
        List<Coordinate> lowPoints = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i) {
            for (int j = 0; j < input.get(i).size(); ++j) {
                int current = input.get(i).get(j);
                // check up
                if (i - 1 >= 0 && input.get(i - 1).get(j) <= current) {
                    continue;
                }

                // check down
                if (i + 1 < input.size() && input.get(i + 1).get(j) <= current) {
                    continue;
                }

                // check left
                if (j - 1 >= 0 && input.get(i).get(j - 1) <= current) {
                    continue;
                }

                if (j + 1 < input.get(i).size() && input.get(i).get(j + 1) <= current) {
                    continue;
                }

                lowPoints.add(new Coordinate(j, i));
            }
        }

        return lowPoints;
    }

    private static List<List<Integer>> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day09/input.txt")).collect(Collectors.toList());

        return lines.stream()
                .map(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .map(Character::getNumericValue)
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }
}