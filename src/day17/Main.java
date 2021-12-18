package day17;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        Target input = readInput();

        Map<Integer, Map<Integer, Long>> results = new HashMap<>();

        for (int vX = input.maxX; vX >= 0; --vX) {
            for (int vY = input.minY; vY < 1000; ++vY) {
                Long maxHeight = simulate(vX, vY, input);
                if (maxHeight != null) {
                    Map<Integer, Long> vxMap = results.computeIfAbsent(vX, k -> new HashMap<>());
                    vxMap.put(vY, maxHeight);
                }
            }
        }

        System.out.println(results.values()
                .stream()
                .flatMap(v -> v.values().stream())
                .max(Long::compare)
                .orElseThrow());

        System.out.println(results.values().stream().map(Map::size).reduce(Integer::sum).orElseThrow());
    }

    private static Long simulate(int vX, int vY, Target target) {
        int x = 0;
        long y = 0;
        long maxHeight = 0;

        while (!isInTarget(x, y, target) && canReachTarget(x, y, vX, target)) {
            x += vX;
            y += vY;

            if (y > maxHeight) {
                maxHeight = y;
            }

            vX += Integer.compare(0, vX);
            vY -= 1;
        }

        return isInTarget(x, y, target) ? maxHeight : null;
    }

    private static boolean canReachTarget(int x, long y, int vX, Target target) {
        return x <= target.maxX && (x >= target.minX || vX > 0) && y >= target.minY;
    }

    private static boolean isInTarget(int x, long y, Target target) {
        return target.minX <= x && target.maxX >= x && target.minY <= y && target.maxY >= y;
    }

    private static Target readInput() throws Exception {
        String input = Files.lines(Paths.get("src/day17/input.txt")).findFirst().orElseThrow();

        Pattern pattern = Pattern.compile("^target area: x=(-?\\d+)..(-?\\d+), y=(-?\\d+)..(-?\\d+)$");
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            throw new Exception("invalid input");
        }

        Target result = new Target();
        result.minX = Integer.parseInt(matcher.group(1));
        result.maxX = Integer.parseInt(matcher.group(2));
        result.minY = Integer.parseInt(matcher.group(3));
        result.maxY = Integer.parseInt(matcher.group(4));

        return result;
    }
}