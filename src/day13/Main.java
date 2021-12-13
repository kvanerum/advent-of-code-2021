package day13;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern foldPattern = Pattern.compile("^fold along ([xy])=(\\d+)$");

    public static void main(String[] args) throws Exception {
        Input input = readInput();

        Map<Integer, Set<Integer>> paper = new HashMap<>();
        addDotsToPaper(paper, input.coordinates);

        // do first fold
        fold(paper, input.foldList.get(0));

        int numDots = countDots(paper);
        System.out.println(numDots);

        // continue folding
        input.foldList.stream().skip(1).forEach(fold -> fold(paper, fold));

        printPaper(paper);
    }

    private static void addDotsToPaper(Map<Integer, Set<Integer>> paper, List<Coordinate> coordinates) {
        for (Coordinate coordinate : coordinates) {
            Set<Integer> x = paper.computeIfAbsent(coordinate.x, k -> new HashSet<>());
            x.add(coordinate.y);
        }
    }

    private static void fold(Map<Integer, Set<Integer>> paper, Fold fold) {
        if (fold.direction == 'x') {
            foldX(paper, fold.position);
        } else if (fold.direction == 'y') {
            foldY(paper, fold.position);
        }
    }

    private static void foldX(Map<Integer, Set<Integer>> paper, int position) {
        // remove all for x = position
        paper.remove(position);

        // merge all where x > position to the left
        Set<Integer> toMerge = paper.keySet().stream().filter(key -> key > position).collect(Collectors.toSet());
        toMerge.forEach(x -> {
            Set<Integer> yValues = paper.get(x);
            int mergeToPos = position - (x - position);
            Set<Integer> mergeTarget = paper.computeIfAbsent(mergeToPos, k -> new HashSet<>());
            mergeTarget.addAll(yValues);

            paper.remove(x);
        });
    }

    private static void foldY(Map<Integer, Set<Integer>> paper, int position) {
        // loop through x...
        paper.values().forEach(x -> {
            // remove y = position
            x.remove(position);

            // merge all where y > position up
            Set<Integer> toMerge = x.stream().filter(y -> y > position).collect(Collectors.toSet());

            toMerge.forEach(y -> {
                x.add(position - (y - position));
                x.remove(y);
            });
        });
    }

    private static int countDots(Map<Integer, Set<Integer>> paper) {
        return paper.values().stream().map(Set::size).reduce(Integer::sum).orElseThrow();
    }

    private static void printPaper(Map<Integer, Set<Integer>> paper) {
        int maxX = paper.keySet().stream().mapToInt(x -> x).max().orElseThrow();
        int maxY = paper.values().stream().flatMap(Set::stream).mapToInt(y -> y).max().orElseThrow();

        for (int y = 0; y <= maxY; ++y) {
            for (int x = 0; x <= maxX; ++x) {
                char c = paper.getOrDefault(x, Collections.emptySet()).contains(y) ? '#' : '.';
                System.out.print(c);
            }
            System.out.println();
        }
    }

    private static Input readInput() throws Exception {
        List<String> lines = Files.lines(Paths.get("src/day13/input.txt")).collect(Collectors.toList());
        Input result = new Input();

        boolean parseFolds = false;
        for (String line : lines) {
            if (line.isBlank()) {
                parseFolds = true;
            } else if (parseFolds) {
                result.foldList.add(parseFold(line));
            } else {
                result.coordinates.add(parseCoordinate(line));
            }
        }

        return result;
    }

    private static Coordinate parseCoordinate(String line) {
        String[] split = line.split(",");
        return new Coordinate(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    private static Fold parseFold(String line) throws Exception {
        Matcher m = foldPattern.matcher(line);
        if (!m.matches()) {
            throw new Exception("invalid fold");
        }

        return new Fold(m.group(1).charAt(0), Integer.parseInt(m.group(2)));
    }
}