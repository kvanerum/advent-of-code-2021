package day08;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Entry {
    public final List<Set<Character>> patterns = new ArrayList<>();
    public final List<Set<Character>> output = new ArrayList<>();

    @Override
    public String toString() {
        return "Entry{" +
               "patterns=" + patterns +
               ", output=" + output +
               '}';
    }
}
