package day03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> input = readInput();

        part1(input);
        part2(input);
    }

    private static void part1(List<String> input) {
        int size = input.get(0).length();
        int gamma = 0;
        int epsilon = 0;
        for (int i = 0; i < size; ++i) {
            int zeroCount = 0;
            for (String entry : input) {
                if (entry.charAt(i) == '0') {
                    zeroCount++;
                }
            }

            if (zeroCount > input.size() / 2) {
                epsilon += Math.pow(2, size - 1 - i);
            } else {
                gamma += Math.pow(2, size - 1 - i);
            }
        }

        System.out.println(gamma * epsilon);
    }

    private static void part2(List<String> input) {
        String oxygen = findRating(input, true);
        String co2 = findRating(input, false);

        System.out.println(Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2));
    }

    private static String findRating(List<String> input, boolean keepMost) {
        int i = 0;
        int length = input.get(0).length();

        while (i < length && input.size() > 1) {
            int zeroCount = 0;
            for (String entry : input) {
                if (entry.charAt(i) == '0') {
                    zeroCount++;
                }
            }

            char keepChar;
            if (keepMost) {
                keepChar = zeroCount > input.size() / 2 ? '0' : '1';
            } else {
                keepChar = zeroCount <= input.size() / 2 ? '0' : '1';
            }

            int finalI = i;
            input = input.stream().filter(entry -> entry.charAt(finalI) == keepChar).collect(Collectors.toList());

            ++i;
        }

        return input.get(0);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day03/input.txt")).collect(Collectors.toList());
    }
}