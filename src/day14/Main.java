package day14;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Map<Character, Map<Character, Map<Integer, Map<Character, Long>>>> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Input input = readInput();

        calculate(input.polymer, input.rules, 10);
        calculate(input.polymer, input.rules, 40);
    }

    private static void calculate(List<Character> polymer, Map<Character, Map<Character, Character>> rules, int iterations) {
        Map<Character, Long> result = new HashMap<>();
        for (int i = 0; i < polymer.size() - 1; ++i) {
            Character char1 = polymer.get(i);
            Character char2 = polymer.get(i + 1);
            Map<Character, Long> yields = yields(char1, char2, iterations, rules);
            yields.forEach((key, value) -> result.merge(key, value, Long::sum));

            // don't count middle char twice
            if (i < polymer.size() - 2) {
                result.computeIfPresent(char2, (k, v) -> v - 1);
            }
        }

        printResult(result);
    }

    private static Map<Character, Long> yields(Character char1, Character char2, int steps, Map<Character, Map<Character, Character>> rules) {
        Map<Character, Map<Integer, Map<Character, Long>>> char1Map = cache.computeIfAbsent(char1, k -> new HashMap<>());
        Map<Integer, Map<Character, Long>> char2Map = char1Map.computeIfAbsent(char2, k -> new HashMap<>());
        Map<Character, Long> stepsMap = char2Map.get(steps);

        if (stepsMap != null) {
            // already calculated
            return stepsMap;
        } else if (steps == 1) {
            // get from rule
            Map<Character, Long> result = new HashMap<>();
            result.compute(char1, (k, v) -> v == null ? 1 : v + 1);
            result.compute(char2, (k, v) -> v == null ? 1 : v + 1);
            result.compute(rules.get(char1).get(char2), (k, v) -> v == null ? 1 : v + 1);
            char2Map.put(steps, result);
            return result;
        } else {
            // go deeper
            Character middle = rules.get(char1).get(char2);
            Map<Character, Long> yield1 = yields(char1, middle, steps - 1, rules);
            Map<Character, Long> yield2 = yields(middle, char2, steps - 1, rules);

            // merge
            Map<Character, Long> result = Stream.concat(yield1.entrySet().stream(), yield2.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
            result.computeIfPresent(middle, (k, v) -> v - 1); // don't count middle char twice
            char2Map.put(steps, result);
            return result;
        }
    }

    private static void printResult(Map<Character, Long> counts) {
        List<Long> sortedCounts = counts.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        System.out.println(sortedCounts.get(sortedCounts.size() - 1) - sortedCounts.get(0));
    }

    private static Input readInput() throws Exception {
        List<String> lines = Files.lines(Paths.get("src/day14/input.txt")).collect(Collectors.toList());
        Input result = new Input();

        for (char c : lines.get(0).toCharArray()) {
            result.polymer.add(c);
        }

        lines.stream().skip(2).forEach(rule -> {
            String[] split = rule.split(" -> ");

            Map<Character, Character> firstCharMap = result.rules.computeIfAbsent(split[0].charAt(0), k -> new HashMap<>());
            firstCharMap.put(split[0].charAt(1), split[1].charAt(0));
        });

        return result;
    }
}