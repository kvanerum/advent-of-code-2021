package day14;

import java.util.*;

public class Input {
    public final List<Character> polymer = new ArrayList<>();
    public final Map<Character, Map<Character, Character>> rules = new HashMap<>();

    @Override
    public String toString() {
        return "Input{" +
               "polymer=" + polymer +
               ", rules=" + rules +
               '}';
    }
}
