package day10;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final static Map<Character, Character> characterPairs = Map.of(
            '(', ')',
            '{', '}',
            '<', '>',
            '[', ']'
    );

    private final static Map<Character, Integer> corruptPointsMap = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137
    );

    private final static Map<Character, Integer> incompletePointsMap = Map.of(
            ')', 1,
            ']', 2,
            '}', 3,
            '>', 4
    );

    public static void main(String[] args) throws IOException {
        List<String> input = readInput();

        int points = input.stream().map(Main::checkCorrupted).reduce(Integer::sum).orElseThrow();
        System.out.println(points);

        List<BigInteger> autocompleteScores = input.stream()
                .filter(line -> checkCorrupted(line) == 0) // remove corrupted lines
                .map(Main::calculateAutocompleteScore)
                .sorted()
                .collect(Collectors.toList());

        BigInteger middle = autocompleteScores.get(Math.floorDiv(autocompleteScores.size(), 2));

        System.out.println(middle);
    }

    private static Integer checkCorrupted(String line) {
        Stack<Character> stack = new Stack<>();

        for (Character c : line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())) {
            if (isOpening(c)) {
                stack.push(c);
            } else {
                Character top = stack.pop();

                if (characterPairs.get(top) != c) {
                    return corruptPointsMap.get(c);
                }
            }
        }

        return 0;
    }

    private static BigInteger calculateAutocompleteScore(String line) {
        Stack<Character> stack = new Stack<>();

        for (Character c : line.chars().mapToObj(c -> (char) c).collect(Collectors.toList())) {
            if (isOpening(c)) {
                stack.push(c);
            } else {
                stack.pop();
            }
        }

        BigInteger currentScore = BigInteger.ZERO;
        while (!stack.empty()) {
            Character c = stack.pop();
            currentScore = currentScore.multiply(BigInteger.valueOf(5));
            currentScore = currentScore.add(BigInteger.valueOf(incompletePointsMap.get(characterPairs.get(c))));
        }

        return currentScore;
    }

    private static boolean isOpening(char c) {
        return characterPairs.containsKey(c);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day10/input.txt")).collect(Collectors.toList());
    }
}