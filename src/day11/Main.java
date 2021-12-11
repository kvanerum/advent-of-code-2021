package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<List<Integer>> input = readInput();

        // part 1
        int totalFlashes = 0;
        for (int i = 0; i < 100; ++i) {
            totalFlashes += doStep(input);
        }

        System.out.println(totalFlashes);

        // part 2
        input = readInput();

        int numFlashes;
        int i = 0;
        do {
            ++i;
            numFlashes = doStep(input);
        } while (numFlashes != input.size() * input.size());

        System.out.println(i);
    }

    private static int doStep(List<List<Integer>> input) {
        Set<Coordinate> hasFlashed = new HashSet<>();

        // increment all
        for (int y = 0; y < input.size(); ++y) {
            for (int x = 0; x < input.size(); ++x) {
                increment(new Coordinate(x, y), input, hasFlashed);
            }
        }

        // reset all flashed ones to 0
        hasFlashed.forEach(coordinate -> input.get(coordinate.y).set(coordinate.x, 0));

        return hasFlashed.size();
    }

    private static void flash(Coordinate coordinate, List<List<Integer>> input, Set<Coordinate> hasFlashed) {
        if (hasFlashed.contains(coordinate)) {
            return;
        }

        hasFlashed.add(coordinate);

        // increment surrounding
        for (int y = coordinate.y - 1; y <= coordinate.y + 1; ++y) {
            for (int x = coordinate.x - 1; x <= coordinate.x + 1; ++x) {
                Coordinate c = new Coordinate(x, y);
                increment(c, input, hasFlashed);
            }
        }
    }

    private static void increment(Coordinate coordinate, List<List<Integer>> input, Set<Coordinate> hasFlashed) {
        if (!coordinate.isValid(input.size())) {
            return;
        }

        int value = input.get(coordinate.y).get(coordinate.x);
        value++;
        input.get(coordinate.y).set(coordinate.x, value);

        if (value > 9) {
            flash(coordinate, input, hasFlashed);
        }
    }

    private static List<List<Integer>> readInput() throws IOException {
        return Files.lines(Paths.get("src/day11/input.txt"))
                .map(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .map(Character::getNumericValue)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}