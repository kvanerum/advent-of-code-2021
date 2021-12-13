package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Path> input = readInput();

        // create map of paths
        Map<String, Set<String>> pathsMap = createPathsMap(input);

        System.out.println(numPaths(Collections.singletonList("start"), pathsMap, false));
        System.out.println(numPaths(Collections.singletonList("start"), pathsMap, true));
    }

    private static Integer numPaths(List<String> currentPath, Map<String, Set<String>> pathsMap, boolean allowDoubleVisit) {
        String currentCave = currentPath.get(currentPath.size() - 1);

        if (currentCave.equals("end")) {
            return 1;
        }

        return pathsMap.getOrDefault(currentCave, Collections.emptySet()).stream().map(to -> {
            if (!to.equals("end") &&
                to.toLowerCase().equals(to) &&
                currentPath.contains(to) &&
                (!allowDoubleVisit || pathContainsDoubleVisit(currentPath))) {
                return 0;
            }

            List<String> newPath = new ArrayList<>(currentPath);
            newPath.add(to);
            return numPaths(newPath, pathsMap, allowDoubleVisit);
        }).reduce(Integer::sum).orElseThrow();
    }

    private static boolean pathContainsDoubleVisit(List<String> path) {
        List<String> smallCaves = path.stream()
                .filter(cave -> !cave.equals("start") && cave.toLowerCase().equals(cave))
                .collect(Collectors.toList());

        return new HashSet<>(smallCaves).size() != smallCaves.size();
    }

    private static Map<String, Set<String>> createPathsMap(List<Path> input) {
        Map<String, Set<String>> result = new HashMap<>();

        input.forEach(path -> {
            // from -> to
            if (!path.to.equals("start") && !path.from.equals("end")) {
                Set<String> fromSet = result.getOrDefault(path.from, new HashSet<>());
                fromSet.add(path.to);
                result.put(path.from, fromSet);
            }

            // to -> from
            if (!path.from.equals("start") && !path.to.equals("end")) {
                Set<String> toSet = result.getOrDefault(path.to, new HashSet<>());
                toSet.add(path.from);
                result.put(path.to, toSet);
            }
        });

        return result;
    }

    private static List<Path> readInput() throws IOException {
        return Files.lines(Paths.get("src/day12/input.txt")).map(line -> {
            String[] split = line.split("-");
            return new Path(split[0], split[1]);
        }).collect(Collectors.toList());
    }
}