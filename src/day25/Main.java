package day25;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        List<List<Character>> input = readInput();
        List<List<Character>> previous;

        int i = 0;
        do {
            i++;
            previous = input;

            input = moveEast(input);
            input = moveSouth(input);
        } while (!isEqual(input, previous));

        System.out.println(i);
    }

    private static boolean isEqual(List<List<Character>> map1, List<List<Character>> map2) {
        for (int i = 0; i < map1.size(); ++i) {
            for (int j = 0; j < map1.get(i).size(); ++j) {
                if (!map1.get(i).get(j).equals(map2.get(i).get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    private static List<List<Character>> moveEast(List<List<Character>> input) {
        List<List<Character>> result = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            result.add(new ArrayList<>());

            for (int j = 0; j < input.get(i).size(); ++j) {
                // it was a dot and there was a > to the left => the > moves to here
                if (input.get(i).get(j).equals('.') && input.get(i).get((j == 0 ? input.get(i).size() : j) - 1).equals('>')) {
                    result.get(i).add(j, '>');
                }
                // it was a > but there is a . to the right => the > becomes .
                else if (input.get(i).get(j).equals('>') && input.get(i).get((j + 1) % input.get(i).size()).equals('.')) {
                    result.get(i).add(j, '.');
                }
                // otherwise, it stays the same
                else {
                    result.get(i).add(j, input.get(i).get(j));
                }
            }
        }

        return result;
    }

    private static List<List<Character>> moveSouth(List<List<Character>> input) {
        List<List<Character>> result = new ArrayList<>();

        for (int i = 0; i < input.size(); ++i) {
            result.add(new ArrayList<>());

            for (int j = 0; j < input.get(i).size(); ++j) {
                // it was a dot and there was a v above => the v moves to here
                if (input.get(i).get(j).equals('.') && input.get((i == 0 ? input.size() : i) - 1).get(j).equals('v')) {
                    result.get(i).add(j, 'v');
                }
                // it was a v but there is a . below => the v becomes .
                else if (input.get(i).get(j).equals('v') && input.get((i + 1) % input.size()).get(j).equals('.')) {
                    result.get(i).add(j, '.');
                }
                // otherwise, it stays the same
                else {
                    result.get(i).add(j, input.get(i).get(j));
                }
            }
        }

        return result;
    }

    private static List<List<Character>> readInput() throws Exception {
        return Files.lines(Paths.get("src/day25/input.txt"))
                .map(line -> line.chars()
                        .mapToObj(c -> (char) c)
                        .collect(Collectors.toList())
                )
                .collect(Collectors.toList());
    }
}
