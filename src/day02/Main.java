package day02;

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
        int depth = 0;
        int horizontalPosition = 0;

        for (String command : input) {
            String[] split = command.split(" ");
            int param = Integer.parseInt(split[1]);

            if (split[0].equals("forward")) {
                horizontalPosition += param;
            } else if (split[0].equals("down")) {
                depth += param;
            } else if (split[0].equals("up")) {
                depth -= param;
            }
        }

        System.out.println(depth * horizontalPosition);
    }

    private static void part2(List<String> input) {
        int depth = 0;
        int aim = 0;
        int horizontalPosition = 0;

        for (String command : input) {
            String[] split = command.split(" ");
            int param = Integer.parseInt(split[1]);

            if (split[0].equals("forward")) {
                horizontalPosition += param;
                depth += param * aim;
            } else if (split[0].equals("down")) {
                aim += param;
            } else if (split[0].equals("up")) {
                aim -= param;
            }
        }

        System.out.println(depth * horizontalPosition);
    }

    private static List<String> readInput() throws IOException {
        return Files.lines(Paths.get("src/day02/input.txt")).collect(Collectors.toList());
    }
}