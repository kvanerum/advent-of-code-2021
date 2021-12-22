package day21;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final Map<String, long[]> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        int[] input = readInput();

        part1(input[0], input[1]);
        long[] wins = part2(input.clone(), new int[] { 0, 0 }, 0, 3, 0);

        System.out.println(Math.max(wins[0], wins[1]));
    }

    private static void part1(int p1Pos, int p2Pos) {
        int[] positions = new int[] { p1Pos, p2Pos };
        int[] scores = new int[] { 0, 0 };
        int i = 0;
        int dice = 1;
        while (true) {
            int result = dice;
            dice = updateDice(dice);
            result += dice;
            dice = updateDice(dice);
            result += dice;
            dice = updateDice(dice);

            int player = i % 2;
            int newPosition = calculatePosition(positions[player], result);
            scores[player] += newPosition;
            positions[player] = newPosition;

            i += 3;

            if (scores[player] >= 1000) {
                break;
            }
        }

        if (scores[0] >= 1000) {
            System.out.println(scores[1] * i);
        } else {
            System.out.println(scores[0] * i);
        }
    }

    private static long[] part2(int[] positions, int[] scores, int currentPlayer, int diceRollsLeft, int diceTotal) {
        String hash = createHash(positions, scores, currentPlayer, diceRollsLeft, diceTotal);
        if (cache.containsKey(hash)) {
            return cache.get(hash);
        }

        if (diceRollsLeft == 0) {
            int newPos = calculatePosition(positions[currentPlayer], diceTotal);
            scores[currentPlayer] += newPos;
            positions[currentPlayer] = newPos;

            if (scores[currentPlayer] >= 21) {
                // player wins
                long[] result = new long[] { currentPlayer == 0 ? 1 : 0, currentPlayer == 1 ? 1 : 0 };
                cache.put(hash, result);
                return result;
            } else {
                // continue with other player
                return part2(positions.clone(), scores.clone(), currentPlayer == 0 ? 1 : 0, 3, 0);
            }
        } else {
            // spawn 3 new universes
            long[] wins1 = part2(positions.clone(), scores.clone(), currentPlayer, diceRollsLeft - 1, diceTotal + 1);
            long[] wins2 = part2(positions.clone(), scores.clone(), currentPlayer, diceRollsLeft - 1, diceTotal + 2);
            long[] wins3 = part2(positions.clone(), scores.clone(), currentPlayer, diceRollsLeft - 1, diceTotal + 3);

            long[] total = new long[] { wins1[0] + wins2[0] + wins3[0], wins1[1] + wins2[1] + wins3[1] };
            cache.put(hash, total);

            return total;
        }
    }

    private static String createHash(int[] positions, int[] scores, int currentPlayer, int diceRollsLeft, int diceTotal) {
        return Stream.of(positions[0], positions[1], scores[0], scores[1], currentPlayer, diceRollsLeft, diceTotal)
                .map(i -> Integer.toString(i))
                .collect(Collectors.joining("-"));
    }

    private static int calculatePosition(int pos, int move) {
        while (move > 0) {
            if (pos + move > 10) {
                move -= 11 - pos;
                pos = 1;
            } else {
                pos += move;
                move = 0;
            }
        }

        return pos;
    }

    private static int updateDice(int dice) {
        return dice == 100 ? 1 : dice + 1;
    }

    private static int[] readInput() throws Exception {
        List<String> input = Files.lines(Paths.get("src/day21/input.txt")).collect(Collectors.toList());

        String[] split1 = input.get(0).split(": ");
        String[] split2 = input.get(1).split(": ");

        return new int[] { Integer.parseInt(split1[1]), Integer.parseInt(split2[1]) };
    }
}