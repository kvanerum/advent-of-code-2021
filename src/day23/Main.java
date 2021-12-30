package day23;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static int currentMin = Integer.MAX_VALUE;
    private static final Set<Integer> forbiddenHallwayPositions = Set.of(2, 4, 6, 8);
    private static final Map<String, Integer> cache = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Configuration input = readInput();

        // part 1
        doStep(input, 0, 2);
        System.out.println(currentMin);

        // part 2
        currentMin = Integer.MAX_VALUE;
        cache.clear();
        input.rooms.get(0).addAll(1, List.of('D', 'D'));
        input.rooms.get(1).addAll(1, List.of('B', 'C'));
        input.rooms.get(2).addAll(1, List.of('A', 'B'));
        input.rooms.get(3).addAll(1, List.of('C', 'A'));
        doStep(input, 0, 4);
        System.out.println(currentMin);
    }

    private static void doStep(Configuration config, int currentScore, int totalRoomSize) {
        String s = config.toString();
        Integer previousScore = cache.get(s);
        if (previousScore != null && previousScore < currentScore) {
            // already had this config with a lower score
            return;
        } else {
            cache.put(s, currentScore);
        }

        if (config.isFinished(totalRoomSize)) {
            if (currentScore < currentMin) {
                currentMin = currentScore;
            }
            return;
        } else if (currentScore > currentMin) {
            return;
        }

        // try all moves...

        // amphipods in rooms
        for (int i = 0; i < config.rooms.size(); ++i) {
            List<Character> room = config.rooms.get(i);
            if (room.isEmpty() || config.isRoomFinished(i, totalRoomSize) || config.isRoomCorrect(i)) {
                continue;
            }

            // move top to all possible hallway positions
            char topChar = room.get(room.size() - 1);

            for (int target = 0; target <= 10; ++target) {
                // check if target is possible
                int currentHallwayPosition = 2 * i + 2;
                int finalTarget = target;
                if (forbiddenHallwayPositions.contains(target) ||
                        config.hallway.containsKey(target) ||
                        (target < currentHallwayPosition && config.hallway.keySet().stream().anyMatch(k -> k > finalTarget && k < currentHallwayPosition)) ||
                        (target > currentHallwayPosition && config.hallway.keySet().stream().anyMatch(k -> k < finalTarget && k > currentHallwayPosition))) {
                    continue;
                }

                int numSteps = totalRoomSize - room.size() + 1; // num steps to top
                numSteps += Math.abs(target - currentHallwayPosition);
                int score = numSteps * getScoreMultiplier(topChar);

                Configuration copy = config.copy();
                copy.hallway.put(target, topChar);
                copy.rooms.get(i).remove(room.size() - 1);
                doStep(copy, currentScore + score, totalRoomSize);
            }
        }

        // amphipods in hallway
        for (Integer i : config.hallway.keySet()) {
            // check if this amphipod can move to the destination room
            char c = config.hallway.get(i);
            int destinationRoomIndex = getDestinationIndex(c);
            List<Character> destinationRoom = config.rooms.get(destinationRoomIndex);
            int destinationHallwayPosition = 2 * destinationRoomIndex + 2;

            if (destinationRoom.size() == totalRoomSize ||
                    (destinationRoom.size() < totalRoomSize && !config.isRoomCorrect(destinationRoomIndex)) ||
                    (destinationHallwayPosition < i && config.hallway.keySet().stream().anyMatch(k -> k > destinationHallwayPosition && k < i)) ||
                    (destinationHallwayPosition > i && config.hallway.keySet().stream().anyMatch(k -> k < destinationHallwayPosition && k > i))) {
                continue;
            }

            // move into room
            int numSteps = totalRoomSize - destinationRoom.size();
            numSteps += Math.abs(destinationHallwayPosition - i);
            int score = numSteps * getScoreMultiplier(c);

            Configuration copy = config.copy();
            copy.hallway.remove(i);
            copy.rooms.get(destinationRoomIndex).add(c);
            doStep(copy, currentScore + score, totalRoomSize);
        }
    }

    private static int getScoreMultiplier(char c) {
        return switch (c) {
            case 'A' -> 1;
            case 'B' -> 10;
            case 'C' -> 100;
            case 'D' -> 1000;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static int getDestinationIndex(char c) {
        return switch (c) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    private static Configuration readInput() throws Exception {
        List<String> lines = Files.lines(Paths.get("src/day23/input.txt")).collect(Collectors.toList());
        Configuration result = new Configuration();

        // room 1
        List<Character> room = new ArrayList<>();
        room.add(lines.get(3).charAt(3));
        room.add(lines.get(2).charAt(3));
        result.rooms.add(room);

        // room 2
        room = new ArrayList<>();
        room.add(lines.get(3).charAt(5));
        room.add(lines.get(2).charAt(5));
        result.rooms.add(room);

        // room 3
        room = new ArrayList<>();
        room.add(lines.get(3).charAt(7));
        room.add(lines.get(2).charAt(7));
        result.rooms.add(room);

        // room 4
        room = new ArrayList<>();
        room.add(lines.get(3).charAt(9));
        room.add(lines.get(2).charAt(9));
        result.rooms.add(room);

        return result;
    }
}