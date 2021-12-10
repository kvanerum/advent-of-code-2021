package day08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Entry> input = readInput();

        part1(input);
        part2(input);
    }

    private static void part1(List<Entry> input) {
        Set<Integer> uniqueLengths = Set.of(2, 4, 3, 7);
        long numUniqueNumberOfSegments = input.stream()
                .flatMap(entry -> entry.output.stream())
                .filter(digit -> uniqueLengths.contains(digit.size())).count();

        System.out.println(numUniqueNumberOfSegments);
    }

    private static void part2(List<Entry> input) {
        int globalResult = 0;
        for (Entry entry : input) {
            Map<Set<Character>, Integer> mapping = calculateMapping(entry.patterns);

            int result = 0;
            for (Set<Character> d : entry.output) {
                result *= 10;
                result += mapping.get(d);
            }

            globalResult += result;
        }

        System.out.println(globalResult);
    }

    private static Map<Set<Character>, Integer> calculateMapping(List<Set<Character>> patterns) {
        Map<Set<Character>, Integer> mapping = new HashMap<>();
        // 1, 4, 7 and 8 have unique lengths
        Set<Character> number1 = patterns.stream().filter(pattern -> pattern.size() == 2).findFirst().orElseThrow();
        mapping.put(number1, 1);
        Set<Character> number4 = patterns.stream().filter(pattern -> pattern.size() == 4).findFirst().orElseThrow();
        mapping.put(number4, 4);
        mapping.put(patterns.stream().filter(pattern -> pattern.size() == 3).findFirst().orElseThrow(), 7);
        mapping.put(patterns.stream().filter(pattern -> pattern.size() == 7).findFirst().orElseThrow(), 8);

        // numbers with length 6 (0, 6 and 9)
        Set<Character> number6 = null;
        List<Set<Character>> length6 = patterns.stream()
                .filter(pattern -> pattern.size() == 6)
                .collect(Collectors.toList());
        for (Set<Character> characters : length6) {
            if (characters.size() == 6) {
                // number 6 is the one without all segments of number 1
                if (!characters.containsAll(number1)) {
                    mapping.put(characters, 6);
                    number6 = characters;
                }
                // number 0 is the one without all segments of number 4
                else if (!characters.containsAll(number4)) {
                    mapping.put(characters, 0);
                }
                // otherwise it's 9
                else {
                    mapping.put(characters, 9);
                }
            }
        }

        // numbers with length 5 (2, 3 and 5)
        // we need the bottom right segment to distinguish between 2 and 5
        List<Set<Character>> length5 = patterns.stream()
                .filter(pattern -> pattern.size() == 5)
                .collect(Collectors.toList());
        assert number6 != null;
        Set<Character> temp = new HashSet<>(number6);
        temp.retainAll(number1);
        Character bottomRight = temp.iterator().next();
        for (Set<Character> characters : length5) {
            // if it contains all segments of number 1, it's 3
            if (characters.containsAll(number1)) {
                mapping.put(characters, 3);
            }
            // it contains the bottom right segment -> 5
            else if (characters.contains(bottomRight)) {
                mapping.put(characters, 5);
            }
            // otherwise it's 2
            else {
                mapping.put(characters, 2);
            }
        }

        return mapping;
    }

    private static List<Entry> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day08/input.txt")).collect(Collectors.toList());

        return lines.stream().map(line -> {
            String[] split = line.split(" \\| ");

            Entry e = new Entry();
            e.patterns.addAll(Arrays.stream(split[0].split(" "))
                    .map(s -> s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()))
                    .collect(Collectors.toList()));
            e.output.addAll(Arrays.stream(split[1].split(" "))
                    .map(s -> s.chars().mapToObj(c -> (char) c).collect(Collectors.toSet()))
                    .collect(Collectors.toList()));

            return e;
        }).collect(Collectors.toList());
    }
}