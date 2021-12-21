package day20;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    private static char infinityChar = '.';

    public static void main(String[] args) throws Exception {
        Input input = readInput();

        Map<Integer, Map<Integer, Character>> image = input.inputImage;

        for (int i = 0; i < 50; ++i) {
            image = processImage(image, input.algorithm);

            if (i == 1) {
                System.out.println(calculateLitPixels(image));
            }

            // update infinity char
            if (input.algorithm.get(0) != '.') {
                if (input.algorithm.get(input.algorithm.size() - 1) == '.') {
                    // blinks
                    infinityChar = (i + 1) % 2 == 0 ? '.' : '#';
                } else {
                    infinityChar = '#';
                }
            }
        }

        System.out.println(calculateLitPixels(image));
    }

    private static long calculateLitPixels(Map<Integer, Map<Integer, Character>> image) {
        return image.values().stream().map(l -> l.values().stream().filter(c -> c == '#').count()).reduce(Long::sum).orElseThrow();
    }

    private static Map<Integer, Map<Integer, Character>> processImage(Map<Integer, Map<Integer, Character>> image, List<Character> algorithm) {
        int yMin = image.keySet().stream().min(Integer::compare).orElseThrow() - 1;
        int yMax = image.keySet().stream().max(Integer::compare).orElseThrow() + 1;
        int xMin = image.values().stream().findFirst().orElseThrow().keySet().stream().min(Integer::compare).orElseThrow() - 1;
        int xMax = image.values().stream().findFirst().orElseThrow().keySet().stream().max(Integer::compare).orElseThrow() + 1;

        Map<Integer, Map<Integer, Character>> result = new HashMap<>();

        for (int i = yMin; i <= yMax; ++i) {
            for (int j = xMin; j <= xMax; ++j) {
                result.computeIfAbsent(i, k -> new HashMap<>()).put(j, calculatePixel(j, i, image, algorithm));
            }
        }

        return result;
    }

    private static char calculatePixel(int x, int y, Map<Integer, Map<Integer, Character>> image, List<Character> algorithm) {
        StringBuilder sb = new StringBuilder();

        for (int i = y - 1; i <= y + 1; ++i) {
            for (int j = x - 1; j <= x + 1; ++j) {
                sb.append(image.getOrDefault(i, Collections.emptyMap()).getOrDefault(j, infinityChar) == '#' ? '1' : '0');
            }
        }

        return algorithm.get(Integer.parseInt(sb.toString(), 2));
    }

    private static Input readInput() throws Exception {
        List<String> input = Files.lines(Paths.get("src/day20/input.txt")).collect(Collectors.toList());
        Input result = new Input();

        result.algorithm.addAll(input.get(0).chars().mapToObj(c -> (char) c).collect(Collectors.toList()));

        for (int i = 2; i < input.size(); ++i) {
            Map<Integer, Character> l = new HashMap<>();

            for (int j = 0; j < input.get(i).length(); ++j) {
                l.put(j, input.get(i).charAt(j));
            }

            result.inputImage.put(i - 2, l);
        }

        return result;
    }
}