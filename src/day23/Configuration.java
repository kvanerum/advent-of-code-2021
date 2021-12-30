package day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configuration {
    public List<List<Character>> rooms;
    public Map<Integer, Character> hallway;

    public Configuration() {
        rooms = new ArrayList<>();
        hallway = new HashMap<>();
    }

    public Configuration copy() {
        Configuration copy = new Configuration();

        copy.rooms.addAll(rooms.stream().map(ArrayList::new).collect(Collectors.toList()));
        copy.hallway.putAll(hallway);

        return copy;
    }

    public boolean isRoomFinished(int index, int roomSize) {
        try {
            char roomChar = getRoomChar(index);
            return rooms.get(index).size() == roomSize && rooms.get(index).stream().allMatch(c -> c == roomChar);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRoomCorrect(int index) {
        try {
            char roomChar = getRoomChar(index);
            return rooms.get(index).stream().allMatch(c -> c == roomChar);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFinished(int roomSize) {
        return hallway.isEmpty() && rooms.stream().allMatch(r -> isRoomFinished(rooms.indexOf(r), roomSize));
    }

    private static char getRoomChar(int index) throws Exception {
        return switch (index) {
            case 0 -> 'A';
            case 1 -> 'B';
            case 2 -> 'C';
            case 3 -> 'D';
            default -> throw new Exception("invalid room " + index);
        };
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "rooms=" + rooms +
                ", hallway=" + hallway +
                '}';
    }
}
