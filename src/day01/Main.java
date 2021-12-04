package day01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();

        part1(input);
        part2(input);
    }

    private static void part1(List<Integer> input) {
        if (input.size() < 2) {
            System.out.println(0);
        }

        int numIncrements = 0;
        for (int i = 1; i < input.size(); ++i) {
            if (input.get(i) > input.get(i - 1)) {
                numIncrements++;
            }
        }

        System.out.println(numIncrements);
    }

    private static void part2(List<Integer> input) {
        int windowSize = 3;

        if (input.size() <= windowSize) {
            System.out.println(0);
        }

        Integer previousSum = null;
        int numIncrements = 0;
        for (int i = windowSize - 1; i < input.size(); ++i) {
            int sum = 0;
            for (int j = 0; j < windowSize; ++j) {
                sum += input.get(i - j);
            }

            if (previousSum != null && sum > previousSum) {
                numIncrements++;
            }
            previousSum = sum;
        }

        System.out.println(numIncrements);
    }

    private static List<Integer> readInput() throws IOException {
        return Files.lines(Paths.get("src/day01/input.txt")).map(Integer::parseInt).collect(Collectors.toList());
    }
}