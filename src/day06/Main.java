package day06;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final int resetTo = 6;
    private static final int newFishTimer = 8;
    private static final Map<Integer, Map<Integer, BigInteger>> cache = new HashMap<>();

    public static void main(String[] args) throws IOException {
        List<Integer> input = readInput();

        Optional<BigInteger> total = input.stream().map(i -> calculateNumberOfFish(i, 80)).reduce(BigInteger::add);
        total.ifPresent(System.out::println);

        total = input.stream().map(i -> calculateNumberOfFish(i, 256)).reduce(BigInteger::add);
        total.ifPresent(System.out::println);
    }

    private static BigInteger calculateNumberOfFish(int timer, int daysLeft) {
        // get map for timer values
        Map<Integer, BigInteger> timerMap = cache.computeIfAbsent(timer, k -> new HashMap<>());
        if (timerMap.get(daysLeft) != null) {
            // return cached value if present
            return timerMap.get(daysLeft);
        }

        // ...otherwise calculate value recursively
        if (daysLeft <= timer) {
            return BigInteger.ONE;
        } else {
            BigInteger result = calculateNumberOfFish(resetTo, daysLeft - timer - 1).add(
                    calculateNumberOfFish(newFishTimer, daysLeft - timer - 1));

            // save result in cache
            timerMap.put(daysLeft, result);
            return result;
        }
    }

    private static List<Integer> readInput() throws IOException {
        List<String> lines = Files.lines(Paths.get("src/day06/input.txt")).collect(Collectors.toList());

        return Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }
}