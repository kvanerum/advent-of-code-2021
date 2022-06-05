package day24;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    private static List<String> input;

    public static void main(String[] args) throws Exception {
        input = readInput();

        String serial = "96299896449997";
        System.out.println(calculateForSerial(serial));

        serial = "31162141116841";
        System.out.println(calculateForSerial(serial));
    }

    public static Long calculateForSerial(String serial) {
        int wPointer = 0;
        HashMap<Character, Long> register = new HashMap<>();
        register.put('w', 0L);
        register.put('x', 0L);
        register.put('y', 0L);
        register.put('z', 0L);

        for (String line : input) {
            if (line.equals("inp w")) {
                register.put('w', (long) (serial.charAt(wPointer) - '0'));
                wPointer++;
            } else if (line.startsWith("add ")) {
                String[] split = line.split(" ");
                register.compute(split[1].charAt(0), (k, v) -> v + getValue(split[2], register));
            } else if (line.startsWith("mul ")) {
                String[] split = line.split(" ");
                register.compute(split[1].charAt(0), (k, v) -> v * getValue(split[2], register));
            } else if (line.startsWith("div ")) {
                String[] split = line.split(" ");
                register.compute(split[1].charAt(0), (k, v) -> v / getValue(split[2], register));
            } else if (line.startsWith("mod ")) {
                String[] split = line.split(" ");
                register.compute(split[1].charAt(0), (k, v) -> v % getValue(split[2], register));
            } else if (line.startsWith("eql ")) {
                String[] split = line.split(" ");
                register.compute(split[1].charAt(0), (k, v) -> Objects.equals(v, getValue(split[2], register)) ? 1L : 0L);
            } else {
                System.out.println("unknown command: " + line);
            }
        }

        return register.get('z');
    }

    private static Long getValue(String str, HashMap<Character, Long> register) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return register.get(str.charAt(0));
        }
    }

    private static List<String> readInput() throws Exception {
        return Files.lines(Paths.get("src/day24/input.txt")).collect(Collectors.toList());
    }
}
