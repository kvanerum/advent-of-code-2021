package day18;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private final static Pattern numberPairPattern = Pattern.compile("^\\[(\\d+),(\\d+)].*");
    private final static Pattern numberPattern = Pattern.compile("^(\\d+).*");
    private static int parseIndex = 0;

    public static void main(String[] args) throws Exception {
        List<String> input = readInput();

        String current = addNumbers(input.get(0), input.get(1));
        for (int i = 2; i < input.size(); ++i) {
            current = addNumbers(current, input.get(i));
        }

        SnailNumber snailNumber = toSnailNumber(current);
        System.out.println(calculateMagnitude(snailNumber));

        int highestMagnitude = 0;
        for (int i = 0; i < input.size(); ++i) {
            for (int j = 0; j < input.size(); ++j) {
                if (i == j) {
                    continue;
                }
                String sum = addNumbers(input.get(i), input.get(j));
                parseIndex = 0;
                SnailNumber sn = toSnailNumber(sum);
                int magnitude = calculateMagnitude(sn);
                if (magnitude > highestMagnitude) {
                    highestMagnitude = magnitude;
                }
            }
        }

        System.out.println(highestMagnitude);
    }

    private static SnailNumber toSnailNumber(String input) {
        SnailNumber result = new SnailNumber();

        if (input.charAt(parseIndex) == '[') {
            parseIndex += 1;
            result.left = toSnailNumber(input);

            // skip ','
            parseIndex += 1;
            result.right = toSnailNumber(input);
        } else {
            result.value = Character.getNumericValue(input.charAt(parseIndex));
        }
        parseIndex += 1;

        return result;
    }

    private static int calculateMagnitude(SnailNumber snailNumber) {
        if (snailNumber.value != null) {
            return snailNumber.value;
        } else {
            return 3 * calculateMagnitude(snailNumber.left) + 2 * calculateMagnitude(snailNumber.right);
        }
    }

    private static String addNumbers(String n1, String n2) {
        String result = "[" + n1 + "," + n2 + "]";

        String previous;
        do {
            previous = result;
            result = reduceNumber(result);
        } while (result != null);

        return previous;
    }

    private static String reduceNumber(String number) {
        int depth = 0;
        int i = 0;
        while (i < number.length()) {
            if (number.charAt(i) == '[') {
                depth++;
            } else if (number.charAt(i) == ']') {
                depth--;
            }

            if (depth > 4 && number.substring(i).matches("^\\d+,\\d+.*")) {
                return explode(number, i - 1);
            }

            ++i;
        }

        i = 0;
        while (i < number.length()) {
            if (Character.isDigit(number.charAt(i)) && Character.isDigit(number.charAt(i + 1))) {
                return split(number, i);
            }

            ++i;
        }

        return null;
    }

    private static String explode(String number, int position) {
        Matcher m = numberPairPattern.matcher(number.substring(position));
        m.matches();

        int n1 = Integer.parseInt(m.group(1));
        int n2 = Integer.parseInt(m.group(2));

        String left = number.substring(0, position);
        String right = number.substring(position + 3 + m.group(1).length() + m.group(2).length());

        // left
        int i = position - 1;
        while (i >= 0) {
            if (Character.isDigit(left.charAt(i))) {
                while (Character.isDigit(left.charAt(i - 1))) {
                    i--;
                }

                Matcher m2 = numberPattern.matcher(left.substring(i));

                if (m2.matches()) {
                    // add
                    String l1 = left.substring(0, i);
                    String l2 = left.substring(i + m2.group(1).length());
                    int newValue = n1 + Integer.parseInt(m2.group(1));
                    left = l1 + newValue + l2;
                    break;
                }
            }
            --i;
        }

        // right
        i = 0;
        while (i < right.length()) {
            Matcher m3 = numberPattern.matcher(right.substring(i));
            if (m3.matches()) {
                // add
                String r1 = right.substring(0, i);
                String r2 = right.substring(i + m3.group(1).length());
                int newValue = n2 + Integer.parseInt(m3.group(1));
                right = r1 + newValue + r2;
                break;
            }
            ++i;
        }

        // replace with 0
        number = left + '0' + right;

        return number;
    }

    private static String split(String number, int position) {
        String left = number.substring(0, position);
        String right = number.substring(position + 2);
        Integer n = Integer.parseInt(number, position, position + 2, 10);

        return left + "[" + (int) Math.floor(n / 2.0) + "," + (int) Math.ceil(n / 2.0) + "]" + right;
    }

    private static List<String> readInput() throws Exception {
        return Files.lines(Paths.get("src/day18/input.txt")).collect(Collectors.toList());
    }
}