package day15;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        List<List<Integer>> input = readInput();

        Map<Coordinate, Map<Coordinate, Integer>> transitionMap = createTransitionMap(input);
        findShortestPath(new Coordinate(0, 0), transitionMap, new Coordinate(input.size() - 1, input.size() - 1));

        List<List<Integer>> bigMap = createBigMap(input);
        transitionMap = createTransitionMap(bigMap);
        findShortestPath(new Coordinate(0, 0), transitionMap, new Coordinate(bigMap.size() - 1, bigMap.size() - 1));
    }

    private static void findShortestPath(Coordinate start, Map<Coordinate, Map<Coordinate, Integer>> transitions, Coordinate target) {
        PriorityQueue<Map.Entry<Coordinate, Integer>> toVisit = new PriorityQueue<>(Map.Entry.comparingByValue());
        Map<Coordinate, Integer> visited = new HashMap<>();

        Coordinate current = start;
        visited.put(start, 0);

        while (!visited.containsKey(target)) {
            // check neighbors of current
            Map<Coordinate, Integer> neighbors = transitions.get(current);
            Integer currentDistance = visited.get(current);

            for (Map.Entry<Coordinate, Integer> entry : neighbors.entrySet()) {
                if (visited.containsKey(entry.getKey())) {
                    continue;
                }

                Optional<Map.Entry<Coordinate, Integer>> inToVisit = toVisit.stream().filter(e -> e.getKey().equals(entry.getKey())).findFirst();
                if (inToVisit.isPresent() && inToVisit.get().getValue() > currentDistance + entry.getValue()) {
                    toVisit.remove(inToVisit.get());
                    toVisit.add(new AbstractMap.SimpleEntry<>(entry.getKey(), currentDistance + entry.getValue()));
                } else if (inToVisit.isEmpty()) {
                    toVisit.add(new AbstractMap.SimpleEntry<>(entry.getKey(), currentDistance + entry.getValue()));
                }
            }

            Map.Entry<Coordinate, Integer> minimum = toVisit.poll();
            current = minimum.getKey();
            visited.put(current, minimum.getValue());
        }

        System.out.println(visited.get(target));
    }

    private static Map<Coordinate, Map<Coordinate, Integer>> createTransitionMap(List<List<Integer>> input) {
        Map<Coordinate, Map<Coordinate, Integer>> result = new HashMap<>();

        for (int y = 0; y < input.size(); ++y) {
            for (int x = 0; x < input.size(); ++x) {
                Coordinate start = new Coordinate(x, y);
                Map<Coordinate, Integer> coordinateMap = new HashMap<>();
                Set.of(
                        new Coordinate(x + 1, y),
                        new Coordinate(x - 1, y),
                        new Coordinate(x, y - 1),
                        new Coordinate(x, y + 1)
                ).forEach(target -> {
                    if (target.isValid(input.size())) {
                        coordinateMap.put(target, input.get(target.y).get(target.x));
                    }
                });
                result.put(start, coordinateMap);
            }
        }

        return result;
    }

    private static List<List<Integer>> createBigMap(List<List<Integer>> input) {
        List<List<Integer>> result = new ArrayList<>();
        int multiplier = 5;

        for (int i = 0; i < multiplier; ++i) {
            for (int j = 0; j < multiplier; ++j) {
                // copy input...
                int add = i + j;
                for (int y = 0; y < input.size(); ++y) {
                    int targetY = input.size() * i + y;
                    if (targetY >= result.size()) {
                        result.add(targetY, new ArrayList<>());
                    }
                    for (int x = 0; x < input.size(); ++x) {
                        int targetX = input.size() * j + x;
                        int value = input.get(y).get(x) + add;
                        if (value > 9) {
                            value = 1 + value % 10;
                        }
                        result.get(targetY).add(targetX, value);
                    }
                }
            }
        }

        return result;
    }

    private static List<List<Integer>> readInput() throws Exception {
        return Files.lines(Paths.get("src/day15/input.txt"))
                .map(line -> line.chars().mapToObj(c -> (char) c).map(Character::getNumericValue).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}