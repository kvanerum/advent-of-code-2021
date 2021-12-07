package day07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();
        List<Integer> sorted = input.stream().sorted().collect(Collectors.toList());

        part1(sorted);
        part2(sorted);
    }

    private static void part1(List<Integer> input) {
        Integer median = input.size() % 2 == 0 ?
                Math.round(((input.get(input.size() / 2 - 1) + input.get(input.size() / 2))) / 2)
                : input.get(Math.floorDiv(input.size(), 2));

        int totalFuel = input.stream().map(i -> Math.abs(median - i)).reduce(Integer::sum).orElse(0);
        System.out.println(totalFuel);
    }

    private static void part2(List<Integer> input) {
        int minimumFuel = calculateFuel(input.get(0), input, null);

        for (int i = input.get(0) + 1; i <= input.get(input.size() - 1); ++i) {
            int fuelNeeded = calculateFuel(i, input, minimumFuel);

            if (-1 != fuelNeeded) {
                minimumFuel = fuelNeeded;
            }
        }

        System.out.println(minimumFuel);
    }

    private static int calculateFuel(int position, List<Integer> input, Integer currentMinimum) {
        int result = 0;
        for (Integer i : input) {
            int n = Math.abs(i - position);

            result += n * (n + 1) / 2;

            if (currentMinimum != null && result > currentMinimum) {
                return -1;
            }
        }

        return result;
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day07/input.txt")).collect(Collectors.toList());

        return Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}